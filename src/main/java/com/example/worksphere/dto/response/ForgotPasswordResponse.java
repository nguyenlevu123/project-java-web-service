//Chuẩn hóa phản hồi tạo token đặt lại mật khẩu.
package com.example.worksphere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Trả token đặt lại mật khẩu cho demo kiểm thử bằng Postman.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordResponse {

    //Thông báo kết quả tạo token.
    private String message;

    //Token đặt lại mật khẩu dùng cho demo khóa học.
    private String resetToken;

    //Thời điểm token hết hạn.
    private LocalDateTime expiresAt;
}
