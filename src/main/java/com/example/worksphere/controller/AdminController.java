//Quản lý API dành cho quản trị viên.
package com.example.worksphere.controller;

import com.example.worksphere.dto.request.AdminCreateUserRequest;
import com.example.worksphere.dto.request.AdminJobPostingRequest;
import com.example.worksphere.dto.request.AdminUpdateUserRequest;
import com.example.worksphere.dto.request.UpdateUserStatusRequest;
import com.example.worksphere.dto.response.ApiResponse;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.dto.response.UserResponse;
import com.example.worksphere.enums.JobStatus;
import com.example.worksphere.enums.Role;
import com.example.worksphere.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Cung cấp các API quản trị người dùng và tin tuyển dụng.
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //Tạo tài khoản mới cho hệ thống.
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {
        UserResponse user = adminService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create user successfully", user));
    }

    //Lấy danh sách người dùng theo bộ lọc tùy chọn.
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean isActive
    ) {
        List<UserResponse> users = adminService.getUsers(role, isActive);
        return ResponseEntity.ok(ApiResponse.success("Get users successfully", users));
    }

    //Lấy chi tiết một người dùng theo id.
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = adminService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("Get user successfully", user));
    }

    //Cập nhật đầy đủ thông tin tài khoản.
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        UserResponse user = adminService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Update user successfully", user));
    }

    //Cập nhật trạng thái kích hoạt của người dùng.
    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        UserResponse user = adminService.updateUserStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Update user status successfully", user));
    }

    //Xóa tài khoản theo id.
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Delete user successfully", null));
    }

    //Tạo tin tuyển dụng mới cho nhà tuyển dụng.
    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<JobPostingResponse>> createJob(
            @Valid @RequestBody AdminJobPostingRequest request
    ) {
        JobPostingResponse job = adminService.createJob(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create job posting successfully", job));
    }

    //Lấy danh sách tin tuyển dụng theo trạng thái tùy chọn.
    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getJobs(
            @RequestParam(required = false) JobStatus status
    ) {
        List<JobPostingResponse> jobs = adminService.getJobs(status);
        return ResponseEntity.ok(ApiResponse.success("Get job postings successfully", jobs));
    }

    //Lấy chi tiết một tin tuyển dụng theo id.
    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getJobById(@PathVariable Long id) {
        JobPostingResponse job = adminService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Get job posting successfully", job));
    }

    //Cập nhật đầy đủ thông tin tin tuyển dụng.
    @PutMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody AdminJobPostingRequest request
    ) {
        JobPostingResponse job = adminService.updateJob(id, request);
        return ResponseEntity.ok(ApiResponse.success("Update job posting successfully", job));
    }

    //Duyệt một tin tuyển dụng.
    @PatchMapping("/jobs/{id}/approve")
    public ResponseEntity<ApiResponse<JobPostingResponse>> approveJob(@PathVariable Long id) {
        JobPostingResponse job = adminService.approveJob(id);
        return ResponseEntity.ok(ApiResponse.success("Approve job posting successfully", job));
    }

    //Từ chối một tin tuyển dụng.
    @PatchMapping("/jobs/{id}/reject")
    public ResponseEntity<ApiResponse<JobPostingResponse>> rejectJob(@PathVariable Long id) {
        JobPostingResponse job = adminService.rejectJob(id);
        return ResponseEntity.ok(ApiResponse.success("Reject job posting successfully", job));
    }

    //Xóa tin tuyển dụng theo id.
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Delete job posting successfully", null));
    }
}
