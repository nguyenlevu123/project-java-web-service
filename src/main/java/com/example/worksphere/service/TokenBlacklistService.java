//Quản lý danh sách access token đã bị thu hồi.
package com.example.worksphere.service;

import com.example.worksphere.entity.TokenBlacklist;
import com.example.worksphere.exception.BadRequestException;
import com.example.worksphere.repository.TokenBlacklistRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//Cung cấp chức năng chặn access token sau khi logout.
@Service
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    //Nhận repository để lưu và kiểm tra token bị chặn.
    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    //Đưa access token vào danh sách bị thu hồi.
    public void blacklistToken(String token) {
        if (token == null || token.isBlank()) {
            throw new BadRequestException("Token is required");
        }

        //Bỏ qua nếu token đã bị chặn trước đó.
        if (tokenBlacklistRepository.existsByTokenString(token)) {
            return;
        }

        TokenBlacklist tokenBlacklist = TokenBlacklist.builder()
                .tokenString(token)
                .revokedAt(LocalDateTime.now())
                .build();

        tokenBlacklistRepository.save(tokenBlacklist);
    }

    //Kiểm tra access token đã bị thu hồi chưa.
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByTokenString(token);
    }
}
