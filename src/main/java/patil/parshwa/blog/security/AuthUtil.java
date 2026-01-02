package patil.parshwa.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import patil.parshwa.blog.models.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 minute
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String jwt) {
        Claims claims=  Jwts.parser()
                            .verifyWith(getSecretKey())
                            .build()
                            .parseSignedClaims(jwt)
                            .getPayload();

        return claims.getSubject();
    }
}
