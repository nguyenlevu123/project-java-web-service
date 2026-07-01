//Nhận dữ liệu cập nhật trạng thái tài khoản từ admin.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa trạng thái hoạt động mới của người dùng.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserStatusRequest {

    //Trạng thái kích hoạt của tài khoản.
    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
