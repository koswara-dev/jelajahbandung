package id.pariwisata.jelajahbandung.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Should be in application.properties: jjwt.secret
    // AES-256 key
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        String userId = "";
        if (userDetails instanceof id.pariwisata.jelajahbandung.model.User) {
            id.pariwisata.jelajahbandung.model.User user = (id.pariwisata.jelajahbandung.model.User) userDetails;
            extraClaims.put("role", user.getRole());
            userId = String.valueOf(user.getId());
        }
        return generateToken(extraClaims, userId, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, String userId, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userIdStr = extractUserId(token);
        if (userDetails instanceof id.pariwisata.jelajahbandung.model.User) {
            Long userId = ((id.pariwisata.jelajahbandung.model.User) userDetails).getId();
            return (userIdStr.equals(String.valueOf(userId))) && !isTokenExpired(token);
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
