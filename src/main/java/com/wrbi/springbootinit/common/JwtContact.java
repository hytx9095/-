package com.wrbi.springbootinit.common;

public class JwtContact {
    /**
     * JWT存储的请求头
     */
    public final static String TOKEN_HEADER = "Authorization";
    /**
     * jwt加解密使用的密钥
     */
    public final static String SECRET = "mall-jwt-test";
    /**
     * JWT的超时时间
     */
    public final static long EXPIRATION = 3600000000L;
    /**
     * JWT负载中拿到的开头
     */
    public final static String TOKEN_HEAD = "Bearer";
}
