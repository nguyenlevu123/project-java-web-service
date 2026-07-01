//Xử lý nghiệp vụ dành cho nhà tuyển dụng.
package com.example.worksphere.service;

import com.example.worksphere.dto.request.JobPostingRequest;
import com.example.worksphere.dto.request.UpdateApplicationStatusRequest;
import com.example.worksphere.dto.response.ApplicationResponse;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.entity.Application;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.entity.User;
import com.example.worksphere.enums.ApplicationStatus;
import com.example.worksphere.enums.JobStatus;
import com.example.worksphere.enums.Role;
import com.example.worksphere.exception.BadRequestException;
import com.example.worksphere.exception.ConflictException;
import com.example.worksphere.exception.ForbiddenException;
import com.example.worksphere.exception.ResourceNotFoundException;
import com.example.worksphere.exception.UnauthorizedException;
import com.example.worksphere.mapper.ApplicationMapper;
import com.example.worksphere.mapper.JobPostingMapper;
import com.example.worksphere.repository.ApplicationRepository;
import com.example.worksphere.repository.JobPostingRepository;
import com.example.worksphere.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Cung cấp các thao tác quản lý tin tuyển dụng cho nhà tuyển dụng.
@Service
public class EmployerService {

    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostingMapper jobPostingMapper;
    private final ApplicationMapper applicationMapper;

    //Nhận repository và mapper cần cho nghiệp vụ nhà tuyển dụng.
    public EmployerService(
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository,
            ApplicationRepository applicationRepository,
            JobPostingMapper jobPostingMapper,
            ApplicationMapper applicationMapper
    ) {
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.jobPostingMapper = jobPostingMapper;
        this.applicationMapper = applicationMapper;
    }

    //Tạo tin tuyển dụng mới và gửi chờ admin duyệt.
    @Transactional
    public JobPostingResponse createJob(JobPostingRequest request) {
        User employer = getCurrentEmployer();

        JobPosting jobPosting = JobPosting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .salaryRange(request.getSalaryRange())
                .status(JobStatus.PENDING_APPROVAL)
                .employer(employer)
                .build();

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Lấy danh sách tin tuyển dụng của nhà tuyển dụng hiện tại.
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getMyJobs() {
        User employer = getCurrentEmployer();

        return jobPostingRepository.findByEmployerId(employer.getId())
                .stream()
                .map(jobPostingMapper::toResponse)
                .toList();
    }

    //Lấy chi tiết tin tuyển dụng thuộc nhà tuyển dụng hiện tại.
    @Transactional(readOnly = true)
    public JobPostingResponse getMyJobById(Long id) {
        User employer = getCurrentEmployer();
        JobPosting jobPosting = findMyJobById(id, employer.getId());
        return jobPostingMapper.toResponse(jobPosting);
    }

    //Cập nhật tin tuyển dụng và chuyển về trạng thái chờ duyệt.
    @Transactional
    public JobPostingResponse updateMyJob(Long id, JobPostingRequest request) {
        User employer = getCurrentEmployer();
        JobPosting jobPosting = findMyJobById(id, employer.getId());

        if (jobPosting.getStatus() == JobStatus.CLOSED) {
            throw new ConflictException("Closed job posting cannot be updated");
        }

        jobPosting.setTitle(request.getTitle());
        jobPosting.setDescription(request.getDescription());
        jobPosting.setSalaryRange(request.getSalaryRange());
        jobPosting.setStatus(JobStatus.PENDING_APPROVAL);

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Đóng tin tuyển dụng thuộc nhà tuyển dụng hiện tại.
    @Transactional
    public JobPostingResponse closeMyJob(Long id) {
        User employer = getCurrentEmployer();
        JobPosting jobPosting = findMyJobById(id, employer.getId());

        if (jobPosting.getStatus() == JobStatus.CLOSED) {
            throw new ConflictException("Job posting is already closed");
        }

        jobPosting.setStatus(JobStatus.CLOSED);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Lấy hồ sơ ứng tuyển của một tin thuộc nhà tuyển dụng hiện tại.
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByJob(Long jobId) {
        User employer = getCurrentEmployer();
        JobPosting jobPosting = findMyJobById(jobId, employer.getId());

        return applicationRepository.findByJobPostingId(jobPosting.getId())
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    //Cập nhật trạng thái hồ sơ ứng tuyển thuộc tin của nhà tuyển dụng hiện tại.
    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request) {
        User employer = getCurrentEmployer();
        Application application = applicationRepository.findByIdAndJobPostingEmployerId(applicationId, employer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (request.getStatus() == ApplicationStatus.PENDING) {
            throw new BadRequestException("Employer cannot set application status to PENDING");
        }

        application.setStatus(request.getStatus());
        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(savedApplication);
    }

    //Lấy nhà tuyển dụng hiện tại từ SecurityContext.
    private User getCurrentEmployer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Authentication is required");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Authentication is required"));

        if (user.getRole() != Role.EMPLOYER) {
            throw new ForbiddenException("Only employers can access this resource");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        return user;
    }

    //Tìm tin tuyển dụng theo id và chủ sở hữu.
    private JobPosting findMyJobById(Long id, Long employerId) {
        return jobPostingRepository.findByIdAndEmployerId(id, employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
    }
}
