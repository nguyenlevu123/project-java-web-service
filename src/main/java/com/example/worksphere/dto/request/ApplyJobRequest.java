//Nhận dữ liệu ứng tuyển công việc từ ứng viên.
package com.example.worksphere.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin ứng viên gửi khi nộp hồ sơ.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobRequest {

    //Mã tin tuyển dụng ứng viên muốn nộp hồ sơ.
    @NotNull(message = "Job posting id is required")
    private Long jobPostingId;

    //Thư giới thiệu ngắn của ứng viên.
    @Size(max = 2000, message = "Cover letter must not exceed 2000 characters")
    private String coverLetter;

    //Đường dẫn CV đã tải lên hoặc có sẵn.
    @NotBlank(message = "CV URL is required")
    private String cvUrl;
}
