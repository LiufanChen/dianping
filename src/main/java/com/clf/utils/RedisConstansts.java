package com.clf.utils;

/**
 * ClassName: RedisConstansts
 * Package: com.clf.utils
 * Description:
 *
 * @Author clf
 * @Create 2025/6/16 21:33
 * @Version 1.0
 */
public class RedisConstansts {

    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final long LOGIN_CODE_TTL = 2L;   //验证码时间设置为2分钟
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final long LOGIN_TOKEN_TTL = 30L;


}
