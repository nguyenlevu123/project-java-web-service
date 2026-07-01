//Nhận dữ liệu tạo và cập nhật tin tuyển dụng từ admin.
package com.example.worksphere.dto.request;

import com.example.worksphere.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin admin dùng để quản lý tin tuyển dụng.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminJobPostingRequest {

    //Tiêu đề tin tuyển dụng.
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    //Mô tả công việc.
    @NotBlank(message = "Description is required")
    private String description;

    //Khoảng lương hiển thị cho ứng viên.
    @Size(max = 100, message = "Salary range must not exceed 100 characters")
    private String salaryRange;

    //Trạng thái của tin tuyển dụng.
    @NotNull(message = "Job status is required")
    private JobStatus status;

    //Mã nhà tuyển dụng sở hữu tin.
    @NotNull(message = "Employer id is required")
    private Long employerId;
}
