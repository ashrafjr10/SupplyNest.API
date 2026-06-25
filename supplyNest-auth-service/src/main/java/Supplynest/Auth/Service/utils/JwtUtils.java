package Supplynest.Auth.Service.utils;

import SupplyNest.Common.CurrentUser;
import SupplyNest.Common.dtos.RoleResponseDTO;
import Supplynest.Auth.Service.dtos.UserDO;
import Supplynest.Auth.Service.models.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
@RequiredArgsConstructor
public class JwtUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey jwtSecretKey;
    private final RoleFormatterForUI roleFormatterForUI;

    @PostConstruct
    public void init() {
        this.jwtSecretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(
            String phoneOrEmail,
            UUID userId,
            String userType,
            String businessGroupCode,
            String businessCode,
            Role role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneOrEmail", phoneOrEmail);
        claims.put("userType", userType);
        claims.put("businessCode", businessCode);
        claims.put("businessGroupCode", businessGroupCode);
        claims.put("role", roleFormatterForUI.formatRole(role));

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(jwtSecretKey)
                .compact();
    }

    public String generateRefreshToken(
            String phoneOrEmail,
            String businessCode,
            Role role
    ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneOrEmail", phoneOrEmail);
        claims.put("businessCode", businessCode);
        claims.put("role", roleFormatterForUI.formatRole(role));

        return Jwts.builder()
                .claims(claims)
                .subject(phoneOrEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(jwtSecretKey)
                .compact();
    }

    public CurrentUser getUserDataFromToken(String token) {

        Claims claims = extractAllClaims(token);

        Map<String, Object> roleMap = claims.get("role", Map.class);

        RoleResponseDTO role = objectMapper.convertValue(roleMap, RoleResponseDTO.class);

        return CurrentUser.builder()
                .userId(UUID.fromString(claims.getSubject()))
                .phoneOrEmail(claims.get("phoneOrEmail", String.class))
                .userType(claims.get("userType", String.class))
                .businessCode(claims.get("businessCode", String.class))
                .businessGroupCode(claims.get("businessGroupCode", String.class))
                .role(role)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}