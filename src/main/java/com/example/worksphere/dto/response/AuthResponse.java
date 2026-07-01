//Chuẩn hóa dữ liệu phản hồi sau khi xác thực thành công.
package com.example.worksphere.dto.response;

import com.example.worksphere.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin người dùng và token trả về cho client.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    //Mã định danh của người dùng.
    private Long userId;

    //Email của người dùng.
    private String email;

    //Tên đầy đủ của người dùng.
    private String fullName;

    //Vai trò của người dùng.
    private Role role;

    //JWT access token dùng cho các request cần xác thực.
    private String accessToken;

    //JWT refresh token dùng để làm mới access token.
    private String refreshToken;

    //Thời gian hết hạn của access token tính bằng mili giây.
    private Long accessTokenExpiresIn;

    //Thời gian hết hạn của refresh token tính bằng mili giây.
    private Long refreshTokenExpiresIn;
}
