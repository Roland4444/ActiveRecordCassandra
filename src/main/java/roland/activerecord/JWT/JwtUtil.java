package roland.activerecord.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "64ea3540-f64f-4aa7-92ad-40dfb7bf7e37";
    private static final String FIELD_NAME = "USER_ID";

    public static String generateToken(UUID userId) {
        String compact = Jwts.builder()
                .claim(FIELD_NAME, userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 900000))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact(); return compact;
    }

    private static Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static UUID getUserUUID(String token) {
        return UUID.fromString(parseToken(token).get(FIELD_NAME, String.class));
    }

}