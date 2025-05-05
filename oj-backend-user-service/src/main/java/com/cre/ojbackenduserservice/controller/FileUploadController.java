package com.cre.ojbackenduserservice.controller;


import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.constant.MessageConstant;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendcommon.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @return 文件访问url地址
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        // 获取原始文件名
        String filename = multipartFile.getOriginalFilename();
        // 使用UUID保证文件名唯一
        if (filename == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, MessageConstant.FILENAME_NULL);
        }
        filename = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
        String fileUrl = AliOssUtil.uploadFile(filename, multipartFile.getInputStream());
        return BaseResponse.success(fileUrl);
    }
}
