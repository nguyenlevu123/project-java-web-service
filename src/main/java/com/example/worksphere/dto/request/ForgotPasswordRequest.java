//Quản lý yêu cầu tạo token quên mật khẩu.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa email cần tạo token đặt lại mật khẩu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordRequest {

    //Email của tài khoản cần đặt lại mật khẩu.
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
}
