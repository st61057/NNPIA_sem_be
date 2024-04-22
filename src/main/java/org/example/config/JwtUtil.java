package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.dto.UserLoginDto;
import org.example.entity.UserLogin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final String secretKey = "mysecretkey";
    private long accessTokenValidity = 60 * 60 * 1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(UserLoginDto userLogin) {
        Claims claims = Jwts.claims().setSubject(userLogin.getUsername());
        claims.put("username", userLogin.getUsername());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userLogin) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userLogin.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = jwtParser.parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
