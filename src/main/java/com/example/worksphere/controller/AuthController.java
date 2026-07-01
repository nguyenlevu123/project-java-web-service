//Quản lý yêu cầu đăng nhập và đăng ký tài khoản.
package com.example.worksphere.controller;

import com.example.worksphere.dto.request.LoginRequest;
import com.example.worksphere.dto.request.RegisterRequest;
import com.example.worksphere.dto.request.RefreshTokenRequest;
import com.example.worksphere.dto.request.ChangePasswordRequest;
import com.example.worksphere.dto.request.ForgotPasswordRequest;
import com.example.worksphere.dto.request.ResetPasswordRequest;
import com.example.worksphere.dto.response.ApiResponse;
import com.example.worksphere.dto.response.AuthResponse;
import com.example.worksphere.dto.response.ForgotPasswordResponse;
import com.example.worksphere.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Cung cấp API công khai cho đăng ký và đăng nhập.
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    //Nhận service xử lý nghiệp vụ xác thực.
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Đăng ký tài khoản ứng viên hoặc nhà tuyển dụng.
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Register successfully", authResponse));
    }

    //Đăng nhập và nhận access token.
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", authResponse));
    }

    //Làm mới access token bằng refresh token.
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Refresh token successfully", authResponse));
    }

    //Đăng xuất và thu hồi access token hiện tại.
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        authService.logout(authorizationHeader);
        return ResponseEntity.ok(ApiResponse.<Void>success("Logout successfully", null));
    }

    //Đổi mật khẩu cho người dùng đã đăng nhập.
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Change password successfully", null));
    }

    //Tạo token đặt lại mật khẩu cho email hợp lệ.
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        ForgotPasswordResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Reset token generated successfully", response));
    }

    //Đặt lại mật khẩu bằng token hợp lệ.
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Reset password successfully", null));
    }
}
