package com.edu.hcmute.utils;

import com.edu.hcmute.dto.CredentialDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import com.google.api.client.json.Json;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;

import java.util.Base64;
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


    public static CredentialDTO decodeToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        Gson gson = new Gson();

        return gson.fromJson(payload, CredentialDTO.class);
    }

}
