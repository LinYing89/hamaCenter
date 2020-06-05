package com.bairock.iot.hamaCenter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    public static final String SECRET = "shenyun";
    private static final long EXPIRE_TIME = 15 * 60 * 60 * 1000;
    private static final long CHAIN_EXPIRE_TIME = 30L * 24 * 3600 * 1000;

    static boolean verify(String token, String username, String secret) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getCompanyId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("company_id").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String sign(Long userId, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
    }

    public static Long getChainId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("chain_id").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String signChain(Long userId) {
        Date date = new Date(System.currentTimeMillis() + CHAIN_EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create().withClaim("chain_id", userId).withExpiresAt(date).sign(algorithm);
    }

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNDI0NSwiZXhwIjoxNTc0OTg5NTc2fQ.5VHmUHnKqlaRU168svZPmV_dYeRj5872uiexy9HpCM8";
        DecodedJWT jwt = JWT.decode(token);
        Long luser_id = jwt.getClaim("user_id").asLong();
        Claim user_id = jwt.getClaim("user_id");
        String company_id = jwt.getClaim("company_id").asString();
        Object obj = jwt.getClaims();
        String str = jwt.getClaim("username").asString();

//        System.out.printf(signChain(1L));
    }



}
