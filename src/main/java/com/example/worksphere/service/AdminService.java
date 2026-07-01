//Xử lý nghiệp vụ quản trị người dùng và tin tuyển dụng.
package com.example.worksphere.service;

import com.example.worksphere.dto.request.AdminCreateUserRequest;
import com.example.worksphere.dto.request.AdminJobPostingRequest;
import com.example.worksphere.dto.request.AdminUpdateUserRequest;
import com.example.worksphere.dto.request.UpdateUserStatusRequest;
import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.dto.response.UserResponse;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.entity.User;
import com.example.worksphere.enums.JobStatus;
import com.example.worksphere.enums.Role;
import com.example.worksphere.exception.BadRequestException;
import com.example.worksphere.exception.ConflictException;
import com.example.worksphere.exception.ResourceNotFoundException;
import com.example.worksphere.mapper.JobPostingMapper;
import com.example.worksphere.mapper.UserMapper;
import com.example.worksphere.repository.ApplicationRepository;
import com.example.worksphere.repository.JobPostingRepository;
import com.example.worksphere.repository.PasswordResetTokenRepository;
import com.example.worksphere.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Cung cấp các thao tác quản trị cho admin.
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserMapper userMapper;
    private final JobPostingMapper jobPostingMapper;
    private final PasswordEncoder passwordEncoder;

    //Nhận repository, mapper và bộ mã hóa cần cho nghiệp vụ admin.
    public AdminService(
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository,
            ApplicationRepository applicationRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserMapper userMapper,
            JobPostingMapper jobPostingMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userMapper = userMapper;
        this.jobPostingMapper = jobPostingMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //Tạo tài khoản mới theo dữ liệu admin cung cấp.
    @Transactional
    public UserResponse createUser(AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .isActive(request.getIsActive() == null || request.getIsActive())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    //Lấy danh sách người dùng theo bộ lọc vai trò hoặc trạng thái.
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(Role role, Boolean isActive) {
        return getUsers(role, isActive, null);
    }

    //Lấy danh sách người dùng theo bộ lọc và từ khóa tìm kiếm.
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(Role role, Boolean isActive, String keyword) {
        List<User> users = hasKeyword(keyword)
                ? userRepository.searchByKeywordAndFilters(keyword.trim(), role, isActive)
                : findUsersByFilters(role, isActive);

        return mapUsers(users);
    }

    //Lấy chi tiết người dùng theo id.
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    //Cập nhật đầy đủ thông tin tài khoản theo id.
    @Transactional
    public UserResponse updateUser(Long id, AdminUpdateUserRequest request) {
        User user = findUserById(id);

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Email already exists");
        }

        validateRoleChange(user, request.getRole());

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setIsActive(request.getIsActive());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    //Cập nhật trạng thái kích hoạt của người dùng.
    @Transactional
    public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {
        User user = findUserById(id);
        user.setIsActive(request.getIsActive());

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    //Xóa người dùng nếu không còn dữ liệu nghiệp vụ phụ thuộc.
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);

        if (jobPostingRepository.existsByEmployerId(user.getId())) {
            throw new ConflictException("User owns job postings and cannot be deleted");
        }

        if (applicationRepository.existsByCandidateId(user.getId())) {
            throw new ConflictException("User owns applications and cannot be deleted");
        }

        passwordResetTokenRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
    }

    //Tạo tin tuyển dụng mới cho một nhà tuyển dụng.
    @Transactional
    public JobPostingResponse createJob(AdminJobPostingRequest request) {
        User employer = findActiveEmployerById(request.getEmployerId());

        JobPosting jobPosting = JobPosting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .salaryRange(request.getSalaryRange())
                .status(request.getStatus())
                .employer(employer)
                .build();

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Lấy danh sách tin tuyển dụng theo trạng thái nếu có.
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getJobs(JobStatus status) {
        return getJobs(status, null);
    }

    //Lấy danh sách tin tuyển dụng theo trạng thái và từ khóa tìm kiếm.
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getJobs(JobStatus status, String keyword) {
        List<JobPosting> jobPostings = hasKeyword(keyword)
                ? jobPostingRepository.searchByKeywordAndStatus(keyword.trim(), status)
                : findJobPostingsByStatus(status);

        return mapJobPostings(jobPostings);
    }

    //Lấy chi tiết tin tuyển dụng theo id.
    @Transactional(readOnly = true)
    public JobPostingResponse getJobById(Long id) {
        JobPosting jobPosting = findJobPostingById(id);
        return jobPostingMapper.toResponse(jobPosting);
    }

    //Cập nhật đầy đủ thông tin tin tuyển dụng theo id.
    @Transactional
    public JobPostingResponse updateJob(Long id, AdminJobPostingRequest request) {
        JobPosting jobPosting = findJobPostingById(id);
        User employer = findActiveEmployerById(request.getEmployerId());

        jobPosting.setTitle(request.getTitle());
        jobPosting.setDescription(request.getDescription());
        jobPosting.setSalaryRange(request.getSalaryRange());
        jobPosting.setStatus(request.getStatus());
        jobPosting.setEmployer(employer);

        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Duyệt tin tuyển dụng nếu tin chưa đóng.
    @Transactional
    public JobPostingResponse approveJob(Long id) {
        JobPosting jobPosting = findJobPostingById(id);

        if (jobPosting.getStatus() == JobStatus.CLOSED) {
            throw new ConflictException("Closed job posting cannot be approved");
        }

        jobPosting.setStatus(JobStatus.APPROVED);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Từ chối tin tuyển dụng nếu tin chưa đóng.
    @Transactional
    public JobPostingResponse rejectJob(Long id) {
        JobPosting jobPosting = findJobPostingById(id);

        if (jobPosting.getStatus() == JobStatus.CLOSED) {
            throw new ConflictException("Closed job posting cannot be rejected");
        }

        jobPosting.setStatus(JobStatus.REJECTED);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return jobPostingMapper.toResponse(savedJobPosting);
    }

    //Xóa tin tuyển dụng nếu chưa có hồ sơ ứng tuyển.
    @Transactional
    public void deleteJob(Long id) {
        JobPosting jobPosting = findJobPostingById(id);

        if (applicationRepository.existsByJobPostingId(jobPosting.getId())) {
            throw new ConflictException("Job posting has applications and cannot be deleted");
        }

        jobPostingRepository.delete(jobPosting);
    }

    //Tìm người dùng hoặc báo lỗi không tồn tại.
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    //Lấy người dùng theo bộ lọc cũ khi không tìm kiếm.
    private List<User> findUsersByFilters(Role role, Boolean isActive) {
        if (role != null && isActive != null) {
            return userRepository.findByRoleAndIsActive(role, isActive);
        }

        if (role != null) {
            return userRepository.findByRole(role);
        }

        if (isActive != null) {
            return userRepository.findByIsActive(isActive);
        }

        return userRepository.findAll();
    }

    //Chuyển danh sách User sang DTO an toàn cho API.
    private List<UserResponse> mapUsers(List<User> users) {
        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    //Tìm nhà tuyển dụng còn hoạt động hoặc báo lỗi phù hợp.
    private User findActiveEmployerById(Long id) {
        User employer = findUserById(id);

        if (employer.getRole() != Role.EMPLOYER) {
            throw new BadRequestException("Employer id must belong to an EMPLOYER user");
        }

        if (!Boolean.TRUE.equals(employer.getIsActive())) {
            throw new ConflictException("Employer account is inactive");
        }

        return employer;
    }

    //Kiểm tra đổi vai trò không làm sai dữ liệu đã liên kết.
    private void validateRoleChange(User user, Role newRole) {
        if (user.getRole() == newRole) {
            return;
        }

        if (jobPostingRepository.existsByEmployerId(user.getId()) && newRole != Role.EMPLOYER) {
            throw new ConflictException("User owns job postings and must remain EMPLOYER");
        }

        if (applicationRepository.existsByCandidateId(user.getId()) && newRole != Role.CANDIDATE) {
            throw new ConflictException("User owns applications and must remain CANDIDATE");
        }
    }

    //Tìm tin tuyển dụng hoặc báo lỗi không tồn tại.
    private JobPosting findJobPostingById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
    }

    //Lấy tin tuyển dụng theo trạng thái cũ khi không tìm kiếm.
    private List<JobPosting> findJobPostingsByStatus(JobStatus status) {
        if (status != null) {
            return jobPostingRepository.findByStatus(status);
        }

        return jobPostingRepository.findAll();
    }

    //Chuyển danh sách JobPosting sang DTO trả về API.
    private List<JobPostingResponse> mapJobPostings(List<JobPosting> jobPostings) {
        return jobPostings.stream()
                .map(jobPostingMapper::toResponse)
                .toList();
    }

    //Kiểm tra từ khóa tìm kiếm có giá trị hay không.
    private boolean hasKeyword(String keyword) {
        return keyword != null && !keyword.isBlank();
    }
}
