//Quản lý API dành cho ứng viên.
package com.example.worksphere.controller;

import com.example.worksphere.dto.request.ApplyJobRequest;
import com.example.worksphere.dto.response.ApiResponse;
import com.example.worksphere.dto.response.ApplicationResponse;
import com.example.worksphere.dto.response.CvUploadResponse;
import com.example.worksphere.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//Cung cấp API tải CV và quản lý hồ sơ ứng tuyển cho ứng viên.
@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    //Tải file CV PDF lên hệ thống.
    @PostMapping("/cv/upload")
    public ResponseEntity<ApiResponse<CvUploadResponse>> uploadCv(@RequestParam("file") MultipartFile file) {
        CvUploadResponse cvUpload = candidateService.uploadCv(file);
        return ResponseEntity.ok(ApiResponse.success("Upload CV successfully", cvUpload));
    }

    //Nộp hồ sơ ứng tuyển vào một tin tuyển dụng đã duyệt.
    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyJob(
            @Valid @RequestBody ApplyJobRequest request
    ) {
        ApplicationResponse application = candidateService.applyJob(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Apply job successfully", application));
    }

    //Lấy danh sách hồ sơ ứng tuyển của ứng viên hiện tại.
    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getMyApplications() {
        List<ApplicationResponse> applications = candidateService.getMyApplications();
        return ResponseEntity.ok(ApiResponse.success("Get my applications successfully", applications));
    }
}
