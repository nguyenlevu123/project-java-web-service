//Quản lý yêu cầu đặt lại mật khẩu bằng token.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa token và mật khẩu mới để đặt lại mật khẩu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    //Token đặt lại mật khẩu do hệ thống cấp.
    @NotBlank(message = "Reset token is required")
    private String resetToken;

    //Mật khẩu mới cần lưu sau khi mã hóa.
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;

    //Mật khẩu nhập lại để kiểm tra khớp.
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
