package com.ucsal.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:chave_secreta_compartilhada_de_producao_256bits_minimo}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000; 

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Integer userId, String email, String role, String nomeCompleto) {
        return Jwts.builder()
                .claims(Map.of("id", userId, "role", role, "nomeCompleto", nomeCompleto))
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
}
