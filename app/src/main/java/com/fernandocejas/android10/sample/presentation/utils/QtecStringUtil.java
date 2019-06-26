package com.fernandocejas.android10.sample.presentation.utils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * Created by l2h on 17-6-12.
 * Desc:
 */
public class QtecStringUtil {
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        if (isBlank(mobile)){
            return false;
        }
            return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 手机号脱敏处理.13557121203处理成135****1203.
     * @param mobile 手机号
     * @return 如果不是手机号,返回null
     */
    public static  String mobileHide(String mobile){
        if (!isMobile(mobile)){
            return null;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public  static   boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    public static boolean isNotBlank(final CharSequence cs){
        return !isBlank(cs);
    }
    public static String randomValidCode(int length) throws IllegalStateException{
        if (length<4){
            throw new IllegalStateException("验证码长度应该大于4");
        }
        // 返回一个0~(指定数-1)之间的随机值
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        int [] code = new int[length];
        for (int i= 0;i<length;i++){
            builder.append(random.nextInt(10)) ;
        }
        return builder.toString();
    }

//    /**
//     * MD5加密
//     */
//    public static String md5Hex(String data){
//        return DigestUtils.md5(data);
//    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        String uniqueKey = uuid.toString().replaceAll("-","");
        return uniqueKey;
    }
}
