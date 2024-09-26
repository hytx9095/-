package com.wrbi.springbootinit.utils;

import com.wrbi.springbootinit.common.ErrorCode;
import com.wrbi.springbootinit.common.JwtContact;
import com.wrbi.springbootinit.common.JwtProperties;
import com.wrbi.springbootinit.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static JwtProperties jwtProperties;

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        JwtUtils.jwtProperties = jwtProperties;
    }
    public static <T> String createToken(T userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId.toString());
        System.out.println();
        return Jwts.builder()
                .addClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + JwtContact.EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, JwtContact.SECRET)// 设置签名算法
//                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())// 设置签名算法
                .compact();
    }

    public static Claims parseJwtToken(String token) {

        if (!StringUtils.hasLength(token)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"token为空");
        }
        Claims claims = null;
        // 根据哪个密钥解密
        try{
            claims = Jwts.parser()
                    .setSigningKey(JwtContact.SECRET)
                    // 设置要解析的Token
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"token无效");
        }

        if (claims == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"token无效");
        }
        return claims;
    }

    public static long getUserIdByToken(String token) {
        Claims claims = parseJwtToken(token);
        String userinfo = (String) claims.get("userId");
        long userId = Long.parseLong(userinfo);
        return userId;
    }
}
