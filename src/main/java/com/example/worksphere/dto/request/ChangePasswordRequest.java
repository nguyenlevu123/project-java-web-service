//Quản lý yêu cầu đổi mật khẩu của người dùng.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa dữ liệu client gửi lên để đổi mật khẩu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    //Mật khẩu hiện tại dùng để xác minh.
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    //Mật khẩu mới cần lưu sau khi mã hóa.
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;

    //Mật khẩu nhập lại để kiểm tra khớp.
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
