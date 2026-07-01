//Chuyển đổi dữ liệu hồ sơ ứng tuyển sang DTO trả về.
package com.example.worksphere.mapper;

import com.example.worksphere.dto.response.ApplicationResponse;
import com.example.worksphere.entity.Application;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.entity.User;
import org.springframework.stereotype.Component;

//Ánh xạ Application sang ApplicationResponse cho nhà tuyển dụng.
@Component
public class ApplicationMapper {

    //Chuyển Application entity sang response và làm phẳng dữ liệu liên quan.
    public ApplicationResponse toResponse(Application application) {
        User candidate = application.getCandidate();
        JobPosting jobPosting = application.getJobPosting();

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(candidate.getId())
                .candidateEmail(candidate.getEmail())
                .candidateName(candidate.getFullName())
                .jobPostingId(jobPosting.getId())
                .jobTitle(jobPosting.getTitle())
                .coverLetter(application.getCoverLetter())
                .cvUrl(application.getCvUrl())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
