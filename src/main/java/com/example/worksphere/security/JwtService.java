//Tạo và kiểm tra JWT access token và refresh token.
package com.example.worksphere.security;

import com.example.worksphere.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//Cung cấp chức năng sinh và đọc JWT cho xác thực.
@Service
public class JwtService {

    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";

    private final String secretKey;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    //Nhận cấu hình JWT từ application.yml.
    public JwtService(
            @Value("${app.jwt.secret-key}") String secretKey,
            @Value("${app.jwt.access-token-expiration}") Long accessTokenExpiration,
            @Value("${app.jwt.refresh-token-expiration}") Long refreshTokenExpiration
    ) {
        this.secretKey = secretKey;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    //Tạo access token cho request cần xác thực.
    public String generateAccessToken(User user) {
        return buildToken(user, TOKEN_TYPE_ACCESS, accessTokenExpiration);
    }

    //Tạo refresh token để làm mới access token.
    public String generateRefreshToken(User user) {
        return buildToken(user, TOKEN_TYPE_REFRESH, refreshTokenExpiration);
    }

    //Lấy email người dùng từ token.
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    //Lấy thời điểm hết hạn từ token.
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    //Lấy loại token từ claims.
    public String extractTokenType(String token) {
        return extractAllClaims(token).get("tokenType", String.class);
    }

    //Kiểm tra token có khớp người dùng và còn hạn không.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    //Kiểm tra token đã hết hạn chưa.
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Kiểm tra token có phải access token không.
    public boolean isAccessToken(String token) {
        return TOKEN_TYPE_ACCESS.equals(extractTokenType(token));
    }

    //Kiểm tra token có phải refresh token không.
    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(extractTokenType(token));
    }

    //Trả về thời gian sống của access token.
    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    //Trả về thời gian sống của refresh token.
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    //Tạo token theo loại và thời gian hết hạn.
    private String buildToken(User user, String tokenType, Long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("tokenType", tokenType)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    //Đọc toàn bộ claims từ token.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Tạo khóa ký HMAC từ secret key cấu hình.
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
