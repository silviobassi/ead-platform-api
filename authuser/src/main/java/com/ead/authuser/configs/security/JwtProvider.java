package com.ead.authuser.configs.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

@Log4j2
@Component
public class JwtProvider {

    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${ead.auth.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwt(Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException {

        UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .claim("authorities", userPrincipal.getAuthorities())
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(convertStringToSecretKey(jwtSecret))
                .compact();
    }

    private static SecretKey convertStringToSecretKey(String encodedKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedKey));
    }

}
