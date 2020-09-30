package com.android.base.utils;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class StringChecker {

    private StringChecker() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    private static final String CHINA_PHONE_REG = "^1\\d{10}$";
    private static final String ID_CARD_REG = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    private static final String EMAIL_REG = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private static final String EMAIL_REG2 = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\.][A-Za-z]{2,3}([\\\\.][A-Za-z]{2})?$";
    private static final String DIGIT_REG = "-?[1-9]\\d+\"";
    private static final String URL_REG = "(https?:// (w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    private static final String CHINESE_REGEX = "^[\u4E00-\u9FA5]+$";
    private static final String CHINESE_LIMIT_LENGTH_REGEX = "^[\u4e00-\u9fa5\u36c3\u4DAE]{2,15}$";
    private static final String DECIMALS_REG = "-?[1-9]\\d+(\\.\\d+)?";
    private static final String LETTERS_REG = ".*[a-zA-Z]++.*";
    private static final String DIGITAL_REG = ".*[0-9]++.*";
    private static final String DIGITAL_LETTER_ONLY_REG = "^[A-Za-z0-9]+$";
    // 允许字母+数字或字母+8种特殊字符中一个其中一种组合 （[0-9A-Za-z\\W]里面的\\W 则表示全部特殊字符）
    private static final String DIGITAL_LETTER_WITH_SPECIAL_CHAR_ONLY_REG = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z[#@!~%`&*]]{8,16}$";
    // 真实姓名正则表达式: 中文(不可以包含空格)或 英文(连个连续英文间只能一个空格), 不可以包含特殊字符
    private static final String REAL_NAME_WITH_CHINESE_AND_ENGLISH_REG = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";

    /**
     * 验证中国的手机号
     *
     * @return 验证成功返回 true，验证失败返回 false
     */
    public static boolean isChinaPhoneNumber(@Nullable String mobile) {
        return !isEmpty(mobile) && Pattern.matches(CHINA_PHONE_REG, mobile);
    }

    public static boolean containsLetter(@Nullable String text) {
        return !isEmpty(text) && Pattern.matches(LETTERS_REG, text);
    }

    /**
     * 必须包含其中一种组合，字母+数字 或 字母+8个特殊字符（#@!~%`&*）其中一个
     * @param text 要匹配的字符串
     * @return true 符合规范
     */
    public static boolean containsLetterWithSpecialChar(@Nullable String text) {
        return !isEmpty(text) && Pattern.matches(DIGITAL_LETTER_WITH_SPECIAL_CHAR_ONLY_REG, text);
    }

    public static boolean containsDigital(@Nullable String text) {
        return !isEmpty(text) && Pattern.matches(DIGITAL_REG, text);
    }

    public static boolean containsDigtalLetterOnly(@Nullable String text) {
        return !isEmpty(text) && Pattern.matches(DIGITAL_LETTER_ONLY_REG, text);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isIdCard(@Nullable String idCard) {
        return !isEmpty(idCard) && Pattern.matches(ID_CARD_REG, idCard);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isEmail(@Nullable String email) {
        return !isEmpty(email) && Pattern.matches(EMAIL_REG2, email);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDigit(@Nullable String digit) {
        return !isEmpty(digit) && Pattern.matches(DIGIT_REG, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDecimals(@Nullable String decimals) {
        return !isEmpty(decimals) && Pattern.matches(DECIMALS_REG, decimals);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isChinese(@Nullable String chinese) {
        return !isEmpty(chinese) && Pattern.matches(CHINESE_REGEX, chinese);
    }

    /**
     * 验证URL地址
     *
     * @param url url
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isURL(@Nullable String url) {
        return !isEmpty(url) && Pattern.matches(URL_REG, url);
    }

    /**
     * 获取字符串的字符个数
     */
    public static int getCharLength(@Nullable String string) {
        if (isEmpty(string)) {
            return 0;
        }
        return string.trim().toCharArray().length;
    }

    public static boolean isCharLength(@Nullable String string, int length) {
        return null != string && ((isEmpty(string) && length == 0) || (string.trim().toCharArray().length == length));
    }

    public static boolean isLengthIn(@Nullable String string, int min, int max) {
        int length = string == null ? 0 : string.length();
        return length <= max && length >= min;
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
