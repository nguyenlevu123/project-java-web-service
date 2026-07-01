//Quản lý yêu cầu làm mới token đăng nhập.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa refresh token client gửi lên để lấy token mới.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequest {

    //Refresh token dùng để cấp lại access token.
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
