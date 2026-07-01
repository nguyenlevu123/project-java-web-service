//Quản lý dữ liệu yêu cầu đăng ký tài khoản.
package com.example.worksphere.dto.request;

import com.example.worksphere.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin client gửi lên khi đăng ký.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    //Email dùng để đăng nhập.
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    //Mật khẩu thô trước khi mã hóa.
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    //Tên hiển thị của người dùng.
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    //Vai trò đăng ký công khai.
    private Role role;
}
