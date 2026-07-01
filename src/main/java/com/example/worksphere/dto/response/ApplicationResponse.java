//Chuẩn hóa dữ liệu hồ sơ ứng tuyển trả về cho nhà tuyển dụng.
package com.example.worksphere.dto.response;

import com.example.worksphere.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Chứa thông tin hồ sơ ứng tuyển dạng phẳng, không trả về entity.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    //Mã định danh của hồ sơ ứng tuyển.
    private Long id;

    //Mã định danh của ứng viên.
    private Long candidateId;

    //Email của ứng viên.
    private String candidateEmail;

    //Tên hiển thị của ứng viên.
    private String candidateName;

    //Mã định danh của tin tuyển dụng.
    private Long jobPostingId;

    //Tiêu đề tin tuyển dụng.
    private String jobTitle;

    //Thư giới thiệu của ứng viên.
    private String coverLetter;

    //Đường dẫn CV của ứng viên.
    private String cvUrl;

    //Trạng thái xử lý hồ sơ.
    private ApplicationStatus status;

    //Thời gian ứng viên nộp hồ sơ.
    private LocalDateTime appliedAt;

    //Thời gian cập nhật hồ sơ.
    private LocalDateTime updatedAt;
}
