package org.zerock.mallapi.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Log4j2
public class JWTUtil {
    private static String key = "1234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min){
        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
        return jwtStr;

    }

    public static Map<String, Object> validateToken(String token){
        Map<String, Object> claim = null;

        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));

            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)  //파싱 및 검증, 실패시 에러
                    .getBody();
        } catch (WeakKeyException e) {
            throw new CustomJWTException("WeakKey");
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException("Expired");
        } catch (UnsupportedJwtException e) {
            throw new CustomJWTException("UnsupportedJWT");
        } catch (MalformedJwtException e) {
            throw new CustomJWTException("MalFormed");
        } catch (SignatureException e) {
            throw new CustomJWTException("SignatureError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
        return claim;
    }
}
