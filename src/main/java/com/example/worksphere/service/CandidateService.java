//Xử lý nghiệp vụ dành cho ứng viên.
package com.example.worksphere.service;

import com.example.worksphere.dto.request.ApplyJobRequest;
import com.example.worksphere.dto.response.ApplicationResponse;
import com.example.worksphere.dto.response.CvUploadResponse;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.entity.Application;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.entity.User;
import com.example.worksphere.enums.ApplicationStatus;
import com.example.worksphere.enums.JobStatus;
import com.example.worksphere.enums.Role;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//Cung cấp chức năng xem việc, tải CV và ứng tuyển cho ứng viên.
@Service
public class CandidateService {

    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostingMapper jobPostingMapper;
    private final ApplicationMapper applicationMapper;
    private final FileStorageService fileStorageService;

    //Nhận repository, mapper và service lưu file cho nghiệp vụ ứng viên.
    public CandidateService(
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository,
            ApplicationRepository applicationRepository,
            JobPostingMapper jobPostingMapper,
            ApplicationMapper applicationMapper,
            FileStorageService fileStorageService
    ) {
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.jobPostingMapper = jobPostingMapper;
        this.applicationMapper = applicationMapper;
        this.fileStorageService = fileStorageService;
    }

    //Lấy danh sách tin tuyển dụng đã được duyệt.
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getApprovedJobs() {
        return jobPostingRepository.findByStatus(JobStatus.APPROVED)
                .stream()
                .map(jobPostingMapper::toResponse)
                .toList();
    }

    //Lấy chi tiết tin tuyển dụng đã được duyệt.
    @Transactional(readOnly = true)
    public JobPostingResponse getApprovedJobById(Long id) {
        JobPosting jobPosting = findApprovedJobById(id);
        return jobPostingMapper.toResponse(jobPosting);
    }

    //Tải CV của ứng viên hiện tại lên hệ thống.
    @Transactional(readOnly = true)
    public CvUploadResponse uploadCv(MultipartFile file) {
        getCurrentCandidate();
        return fileStorageService.uploadCv(file);
    }

    //Nộp hồ sơ ứng tuyển vào tin đã được duyệt.
    @Transactional
    public ApplicationResponse applyJob(ApplyJobRequest request) {
        User candidate = getCurrentCandidate();
        JobPosting jobPosting = findApprovedJobById(request.getJobPostingId());

        if (applicationRepository.existsByCandidateIdAndJobPostingId(candidate.getId(), jobPosting.getId())) {
            throw new ConflictException("Candidate already applied to this job");
        }

        Application application = Application.builder()
                .candidate(candidate)
                .jobPosting(jobPosting)
                .coverLetter(request.getCoverLetter())
                .cvUrl(request.getCvUrl())
                .status(ApplicationStatus.PENDING)
                .build();

        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(savedApplication);
    }

    //Lấy danh sách hồ sơ ứng tuyển của ứng viên hiện tại.
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {
        User candidate = getCurrentCandidate();

        return applicationRepository.findByCandidateId(candidate.getId())
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    //Lấy ứng viên hiện tại từ SecurityContext.
    private User getCurrentCandidate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Authentication is required");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Authentication is required"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new ForbiddenException("Only candidates can access this resource");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException("User account is inactive");
        }

        return user;
    }

    //Tìm tin tuyển dụng đã được duyệt hoặc báo không tồn tại.
    private JobPosting findApprovedJobById(Long id) {
        return jobPostingRepository.findByIdAndStatus(id, JobStatus.APPROVED)
                .orElseThrow(() -> new ResourceNotFoundException("Approved job posting not found"));
    }
}
