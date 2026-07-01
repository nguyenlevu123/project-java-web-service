//Quản lý dữ liệu yêu cầu đăng nhập tài khoản.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin client gửi lên khi đăng nhập.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    //Email dùng để xác thực.
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    //Mật khẩu dùng để xác thực.
    @NotBlank(message = "Password is required")
    private String password;
}
