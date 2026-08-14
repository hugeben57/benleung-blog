package com.ben.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private static String secretKey = "benleung";

    private static Long expireTime=7200000L;

    public static String getToken(Map<String,Object> claim){
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                .withClaim("claim",claim)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static Map<String, Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("claim")
                .asMap();
    }
}
