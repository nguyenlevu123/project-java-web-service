//Xử lý nghiệp vụ đăng ký, đăng nhập và quản lý token.
package com.example.worksphere.service;

import com.example.worksphere.dto.request.LoginRequest;
import com.example.worksphere.dto.request.RegisterRequest;
import com.example.worksphere.dto.request.RefreshTokenRequest;
import com.example.worksphere.dto.request.ChangePasswordRequest;
import com.example.worksphere.dto.request.ForgotPasswordRequest;
import com.example.worksphere.dto.request.ResetPasswordRequest;
import com.example.worksphere.dto.response.AuthResponse;
import com.example.worksphere.dto.response.ForgotPasswordResponse;
import com.example.worksphere.entity.PasswordResetToken;
import com.example.worksphere.entity.User;
import com.example.worksphere.enums.Role;
import com.example.worksphere.exception.BadRequestException;
import com.example.worksphere.exception.ConflictException;
import com.example.worksphere.exception.ForbiddenException;
import com.example.worksphere.exception.ResourceNotFoundException;
import com.example.worksphere.exception.UnauthorizedException;
import com.example.worksphere.repository.PasswordResetTokenRepository;
import com.example.worksphere.repository.UserRepository;
import com.example.worksphere.security.CustomUserDetailsService;
import com.example.worksphere.security.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

//Cung cấp chức năng xác thực công khai cho người dùng.
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    //Nhận các thành phần cần cho xác thực và quản lý token.
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            TokenBlacklistService tokenBlacklistService,
            PasswordResetTokenRepository passwordResetTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    //Đăng ký tài khoản công khai và trả về cặp token.
    public AuthResponse register(RegisterRequest request) {
        //Kiểm tra email đã tồn tại trước khi đăng ký.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        Role role = request.getRole() == null ? Role.CANDIDATE : request.getRole();

        if (role == Role.ADMIN) {
            throw new BadRequestException("Public registration does not allow ADMIN role");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(role)
                .isActive(true)
                .build();

        //Lưu người dùng mới vào database.
        User savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser);
    }

    //Đăng nhập tài khoản và trả về cặp token.
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (DisabledException exception) {
            throw new ForbiddenException("User account is inactive");
        }

        //Tải lại người dùng sau khi xác thực thành công.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ForbiddenException("User account is inactive"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        return buildAuthResponse(user);
    }

    //Làm mới access token bằng refresh token hợp lệ.
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("Refresh token is required");
        }

        try {
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new UnauthorizedException("Invalid or expired token");
            }

            String email = jwtService.extractUsername(refreshToken);

            //Tìm người dùng theo email trong refresh token.
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("Invalid or expired token"));

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                throw new ForbiddenException("User account is inactive");
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                throw new UnauthorizedException("Invalid or expired token");
            }

            return buildAuthResponse(user);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    //Đăng xuất bằng cách đưa access token vào blacklist.
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header is required");
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            if (!jwtService.isAccessToken(accessToken)) {
                throw new BadRequestException("Logout requires access token");
            }

            tokenBlacklistService.blacklistToken(accessToken);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    //Đổi mật khẩu cho người dùng đang đăng nhập.
    public void changePassword(ChangePasswordRequest request) {
        String email = getCurrentAuthenticatedEmail();

        //Tìm tài khoản hiện tại từ SecurityContext.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not authenticated"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        //Kiểm tra mật khẩu hiện tại trước khi đổi.
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        validatePasswordConfirmation(request.getNewPassword(), request.getConfirmPassword());

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    //Tạo token đặt lại mật khẩu cho tài khoản theo email.
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        //Lưu token mới để dùng cho bước đặt lại mật khẩu.
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(resetToken)
                .user(user)
                .expiresAt(expiresAt)
                .used(false)
                .build();
        passwordResetTokenRepository.save(passwordResetToken);

        return ForgotPasswordResponse.builder()
                .message("Reset token generated successfully")
                .resetToken(resetToken)
                .expiresAt(expiresAt)
                .build();
    }

    //Đặt lại mật khẩu bằng token hợp lệ.
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        validatePasswordConfirmation(request.getNewPassword(), request.getConfirmPassword());

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.getResetToken())
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        //Chặn token đã dùng hoặc đã hết hạn.
        if (Boolean.TRUE.equals(passwordResetToken.getUsed())) {
            throw new BadRequestException("Reset token has already been used");
        }

        if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        User user = passwordResetToken.getUser();

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    //Tạo dữ liệu phản hồi xác thực từ người dùng.
    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtService.getAccessTokenExpiration())
                .refreshTokenExpiresIn(jwtService.getRefreshTokenExpiration())
                .build();
    }

    //Lấy email của người dùng hiện tại từ SecurityContext.
    private String getCurrentAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new UnauthorizedException("User not authenticated");
        }

        return authentication.getName();
    }

    //Kiểm tra mật khẩu mới và mật khẩu xác nhận phải khớp nhau.
    private void validatePasswordConfirmation(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New password and confirm password do not match");
        }
    }
}
