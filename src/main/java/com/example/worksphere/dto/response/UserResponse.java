//Chuẩn hóa dữ liệu người dùng trả về cho admin.
package com.example.worksphere.dto.response;

import com.example.worksphere.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Chứa thông tin người dùng an toàn, không gồm mật khẩu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    //Mã định danh của người dùng.
    private Long id;

    //Email đăng nhập của người dùng.
    private String email;

    //Tên hiển thị của người dùng.
    private String fullName;

    //Vai trò của người dùng.
    private Role role;

    //Trạng thái hoạt động của tài khoản.
    private Boolean isActive;

    //Thời gian tạo tài khoản.
    private LocalDateTime createdAt;

    //Thời gian cập nhật tài khoản.
    private LocalDateTime updatedAt;
}
