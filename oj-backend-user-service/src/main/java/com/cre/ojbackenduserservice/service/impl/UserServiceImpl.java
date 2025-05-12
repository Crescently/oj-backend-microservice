package com.cre.ojbackenduserservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendcommon.common.DeleteRequest;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.constant.CommonConstant;
import com.cre.ojbackendcommon.constant.MessageConstant;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendcommon.utils.Md5Util;
import com.cre.ojbackendcommon.utils.SqlUtils;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.enums.UserRoleEnum;
import com.cre.ojbackendmodel.model.request.admin.UserAddRequest;
import com.cre.ojbackendmodel.model.request.admin.UserInfoUpdateRequest;
import com.cre.ojbackendmodel.model.request.admin.UserQueryRequest;
import com.cre.ojbackendmodel.model.request.user.UserRegisterRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdateAvatarRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdateInfoRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdatePwdRequest;
import com.cre.ojbackendmodel.model.vo.LoginUserVO;
import com.cre.ojbackendmodel.model.vo.UserVO;
import com.cre.ojbackenduserservice.mapper.UserMapper;
import com.cre.ojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.cre.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;


    @Resource
    private RedissonClient redissonClient;


    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        String userEmail = userRegisterRequest.getUserEmail();
        // 检查两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }
        String lockKey = "user:register:lock:" + userAccount;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁（最多等待5秒，锁自动释放时间30秒）
            boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "系统繁忙，请稍后重试");
            }
            // 在锁的保护下执行注册逻辑
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_account", userAccount);
            User existingUser = userMapper.selectOne(wrapper);
            if (existingUser != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.USERNAME_ALREADY_EXIST);
            }
            String md5Password = Md5Util.getMD5String(userPassword);
            User newUser = User.builder().userAccount(userAccount).userPassword(md5Password).username(username).userEmail(userEmail).userRole(UserRoleEnum.USER.getValue()).build();
            userMapper.insert(newUser);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册服务暂时不可用");
        } finally {
            // 确保释放锁且不误删其他线程的锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request) {
        // 根据用户名查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User loginUser = userMapper.selectOne(wrapper);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.USERNAME_NOT_EXIST);
        }
        // 判断密码正确性
        if (Md5Util.getMD5String(userPassword).equals(loginUser.getUserPassword())) {
            // 封装返回值
            LoginUserVO loginUserVO = new LoginUserVO();
            BeanUtils.copyProperties(loginUser, loginUserVO);
            // 记录用户的登录态
            request.getSession().setAttribute(USER_LOGIN_STATE, loginUser);
            return loginUserVO;
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.PASSWORD_ERROR);
    }

    /**
     * 用户注销t
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        String userAccount = userQueryRequest.getUserAccount();
        String username = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getUserRole();
        String telephone = userQueryRequest.getTelephone();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(telephone), "telephone", telephone);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public void deleteUser(DeleteRequest deleteRequest) {
        userMapper.deleteById(deleteRequest.getId());
    }

    @Override
    public void addUser(UserAddRequest userAddRequest) {
        String userAccount = userAddRequest.getUserAccount();
        String username = userAddRequest.getUsername();
        String userEmail = userAddRequest.getUserEmail();
        String telephone = userAddRequest.getTelephone();
        String address = userAddRequest.getAddress();
        String userRole = userAddRequest.getUserRole();

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUsername(username);
        user.setUserEmail(userEmail);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setUserRole(userRole);
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UserInfoUpdateRequest userInfoUpdateRequest) {
        Long id = userInfoUpdateRequest.getId();
        String userAccount = userInfoUpdateRequest.getUserAccount();
        String username = userInfoUpdateRequest.getUsername();
        String telephone = userInfoUpdateRequest.getTelephone();
        String address = userInfoUpdateRequest.getAddress();
        String userEmail = userInfoUpdateRequest.getUserEmail();
        String userRole = userInfoUpdateRequest.getUserRole();

        User user = new User();
        user.setId(id);
        user.setUserAccount(userAccount);
        user.setUsername(username);
        user.setUserEmail(userEmail);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setUserRole(userRole);

        userMapper.updateById(user);

    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    @Override
    public void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest) {
        Long id = userUpdateInfoRequest.getId();
        String username = userUpdateInfoRequest.getUsername();
        String userEmail = userUpdateInfoRequest.getUserEmail();
        String signature = userUpdateInfoRequest.getSignature();
        String telephone = userUpdateInfoRequest.getTelephone();
        String address = userUpdateInfoRequest.getAddress();
        String description = userUpdateInfoRequest.getDescription();

        User user = User.builder().id(id).username(username).userEmail(userEmail).signature(signature).telephone(telephone).address(address).description(description).build();
        userMapper.updateById(user);
    }

    /**
     * 更新头像
     */
    @Override
    public void updateAvatar(UserUpdateAvatarRequest userUpdateAvatarRequest) {
        String avatarUrl = userUpdateAvatarRequest.getAvatarUrl();
        Long userId = userUpdateAvatarRequest.getUserId();

        User user = User.builder().id(userId).userPic(avatarUrl).build();
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest) {
        String oldPassword = userUpdatePwdRequest.getOldPassword();
        String newPassword = userUpdatePwdRequest.getNewPassword();
        String rePassword = userUpdatePwdRequest.getRePassword();
        Long userId = userUpdatePwdRequest.getUserId();
        // 原密码是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("*").eq("id", userId);
        User loginUser = userMapper.selectOne(wrapper);
        if (!Md5Util.checkPassword(oldPassword, loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.OLD_PWD_ERROR);
        }
        if (!newPassword.equals(rePassword)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, MessageConstant.TWO_PWD_NOT_MATCH);
        }

        String md5String = Md5Util.getMD5String(newPassword);
        // 2.更新数据库
        User user = User.builder().id(userId).userPassword(md5String).build();
        userMapper.updateById(user);
    }
}
