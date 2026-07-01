//Quản lý API công khai cho tin tuyển dụng.
package com.example.worksphere.controller;

import com.example.worksphere.dto.response.ApiResponse;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Cung cấp API xem tin tuyển dụng đã duyệt cho người dùng công khai.
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class PublicJobController {

    private final CandidateService candidateService;

    //Lấy danh sách tin tuyển dụng đã được duyệt.
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getApprovedJobs() {
        List<JobPostingResponse> jobPostings = candidateService.getApprovedJobs();
        return ResponseEntity.ok(ApiResponse.success("Get approved job postings successfully", jobPostings));
    }

    //Lấy chi tiết tin tuyển dụng đã được duyệt.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getApprovedJobById(@PathVariable Long id) {
        JobPostingResponse jobPosting = candidateService.getApprovedJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Get approved job posting successfully", jobPosting));
    }
}
