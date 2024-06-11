package org.example.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.entity.UserLogin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtService {

    private final String secretKey = "mysecretkey";
    private final long accessTokenValidity = 60 * 60 * 1000;

    private final JwtParser jwtParser;

    public JwtService() {
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(UserLogin userLogin) {
        String token = Jwts
                .builder()
                .setSubject(userLogin.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
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
