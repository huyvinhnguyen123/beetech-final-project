package com.beetech.finalproject.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * This file with give us some method to work with jwt token:
 * Create new jwt token with time expiration
 * Verify token is valid
 * Extract information from token
 */
@Component
public class JwtUtils {
    private final String SECRET_KEY = "Secret Key";

    public String createToken(UserDetails userDetails) {
        // create token from build token method below
        return buildToken(new HashMap<>(), userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> claims, String subject) {
        // we will build token with some properties down here
        // we will set claim, set subject, set issued at, set expiration (1 hour)
        // and sign with (algorithm hs256 with secret key we had declared above)
        // combine all step there we have built new token from username
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String extractUsername(String token) {
        // extract username from token
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        // check token if token is expired before present
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        // generate token from build token method
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // validate token if token is expired or none
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}