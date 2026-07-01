//Quản lý API dành cho nhà tuyển dụng.
package com.example.worksphere.controller;

import com.example.worksphere.dto.request.JobPostingRequest;
import com.example.worksphere.dto.request.UpdateApplicationStatusRequest;
import com.example.worksphere.dto.response.ApiResponse;
import com.example.worksphere.dto.response.ApplicationResponse;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Cung cấp API quản lý tin tuyển dụng và hồ sơ ứng tuyển cho nhà tuyển dụng.
@RestController
@RequestMapping("/api/v1/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    //Tạo tin tuyển dụng mới.
    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<JobPostingResponse>> createJob(
            @Valid @RequestBody JobPostingRequest request
    ) {
        JobPostingResponse jobPosting = employerService.createJob(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create job posting successfully", jobPosting));
    }

    //Lấy danh sách tin tuyển dụng của nhà tuyển dụng hiện tại.
    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getMyJobs() {
        List<JobPostingResponse> jobPostings = employerService.getMyJobs();
        return ResponseEntity.ok(ApiResponse.success("Get my job postings successfully", jobPostings));
    }

    //Lấy chi tiết tin tuyển dụng của nhà tuyển dụng hiện tại.
    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getMyJobById(@PathVariable Long id) {
        JobPostingResponse jobPosting = employerService.getMyJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Get my job posting successfully", jobPosting));
    }

    //Cập nhật tin tuyển dụng của nhà tuyển dụng hiện tại.
    @PutMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> updateMyJob(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingRequest request
    ) {
        JobPostingResponse jobPosting = employerService.updateMyJob(id, request);
        return ResponseEntity.ok(ApiResponse.success("Update job posting successfully", jobPosting));
    }

    //Đóng tin tuyển dụng của nhà tuyển dụng hiện tại.
    @PatchMapping("/jobs/{id}/close")
    public ResponseEntity<ApiResponse<JobPostingResponse>> closeMyJob(@PathVariable Long id) {
        JobPostingResponse jobPosting = employerService.closeMyJob(id);
        return ResponseEntity.ok(ApiResponse.success("Close job posting successfully", jobPosting));
    }

    //Lấy hồ sơ ứng tuyển theo tin tuyển dụng của nhà tuyển dụng hiện tại.
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getApplicationsByJob(@PathVariable Long jobId) {
        List<ApplicationResponse> applications = employerService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Get applications successfully", applications));
    }

    //Cập nhật trạng thái hồ sơ ứng tuyển thuộc tin của nhà tuyển dụng hiện tại.
    @PatchMapping("/applications/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        ApplicationResponse application = employerService.updateApplicationStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Update application status successfully", application));
    }
}
