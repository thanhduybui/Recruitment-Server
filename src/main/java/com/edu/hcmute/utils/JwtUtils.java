package com.edu.hcmute.utils;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    private final AppUserRepository appUserRepository;
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    private static final String SECRET_KEY = "Bui Thanh Duy";

    public static String generateToken(CustomClaim claim) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(claim.getEmail())
                .claim("role", claim.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.error("Token hết hạn {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token không đúng định dạng {}", e.getMessage());
        }
        return null;
    }

    public static String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return (claims != null) ? claims.getSubject() : null;
    }

    public static boolean isTokenValid(String token) {
        return parseToken(token) != null;
    }

}
