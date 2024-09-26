package com.wrbi.springbootinit.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * JWT存储的请求头
     */
//    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    /**
     * jwt加解密使用的密钥
     */
//    @Value("${jwt.secret}")
    private String secret;
    /**
     * JWT的超时时间
     */
//    @Value("${jwt.expiration}")
    private long expiration;
    /**
     * JWT负载中拿到的开头
     */
//    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public JwtProperties() {
    }

    public JwtProperties(String tokenHeader, String secret, long expiration, String tokenHead) {
        this.tokenHeader = tokenHeader;
        this.secret = secret;
        this.expiration = expiration;
        this.tokenHead = tokenHead;
    }
}
