package com.cre.ojbackendcommon.utils;

public class StringUtil {
    public static String removeInvisibleCharacters(String input) {
        if (input == null) return null;
        return input.replaceAll("\\p{Cntrl}", "");
    }
}
