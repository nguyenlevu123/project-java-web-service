//Nhận dữ liệu cập nhật tài khoản từ admin.
package com.example.worksphere.dto.request;

import com.example.worksphere.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin admin dùng để cập nhật tài khoản.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateUserRequest {

    //Email đăng nhập mới của tài khoản.
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    //Mật khẩu mới, bỏ trống nếu không đổi.
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    //Tên hiển thị mới của người dùng.
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    //Vai trò mới của tài khoản.
    @NotNull(message = "Role is required")
    private Role role;

    //Trạng thái kích hoạt mới của tài khoản.
    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
