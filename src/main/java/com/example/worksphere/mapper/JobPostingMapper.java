//Chuyển đổi dữ liệu tin tuyển dụng sang DTO trả về.
package com.example.worksphere.mapper;

import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.entity.User;
import org.springframework.stereotype.Component;

//Ánh xạ JobPosting sang JobPostingResponse cho admin.
@Component
public class JobPostingMapper {

    //Chuyển JobPosting entity sang response và làm phẳng thông tin nhà tuyển dụng.
    public JobPostingResponse toResponse(JobPosting jobPosting) {
        User employer = jobPosting.getEmployer();

        return JobPostingResponse.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle())
                .description(jobPosting.getDescription())
                .salaryRange(jobPosting.getSalaryRange())
                .status(jobPosting.getStatus())
                .employerId(employer.getId())
                .employerEmail(employer.getEmail())
                .employerName(employer.getFullName())
                .createdAt(jobPosting.getCreatedAt())
                .updatedAt(jobPosting.getUpdatedAt())
                .build();
    }
}
