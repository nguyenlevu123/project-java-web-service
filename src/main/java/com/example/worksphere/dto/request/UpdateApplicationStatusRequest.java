//Nhận dữ liệu cập nhật trạng thái hồ sơ ứng tuyển.
package com.example.worksphere.dto.request;

import com.example.worksphere.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa trạng thái mới của hồ sơ ứng tuyển.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateApplicationStatusRequest {

    //Trạng thái xử lý mới của hồ sơ.
    @NotNull(message = "Application status is required")
    private ApplicationStatus status;
}
