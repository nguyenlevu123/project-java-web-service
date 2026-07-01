//Chuẩn hóa dữ liệu tin tuyển dụng trả về cho admin.
package com.example.worksphere.dto.response;

import com.example.worksphere.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Chứa thông tin tin tuyển dụng và dữ liệu nhà tuyển dụng dạng phẳng.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingResponse {

    //Mã định danh của tin tuyển dụng.
    private Long id;

    //Tiêu đề tin tuyển dụng.
    private String title;

    //Mô tả công việc.
    private String description;

    //Khoảng lương hiển thị.
    private String salaryRange;

    //Trạng thái duyệt tin tuyển dụng.
    private JobStatus status;

    //Mã định danh của nhà tuyển dụng.
    private Long employerId;

    //Email của nhà tuyển dụng.
    private String employerEmail;

    //Tên hiển thị của nhà tuyển dụng.
    private String employerName;

    //Thời gian tạo tin tuyển dụng.
    private LocalDateTime createdAt;

    //Thời gian cập nhật tin tuyển dụng.
    private LocalDateTime updatedAt;
}
