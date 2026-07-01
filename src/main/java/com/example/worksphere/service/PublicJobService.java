//Xử lý nghiệp vụ xem và tìm kiếm tin tuyển dụng công khai.
package com.example.worksphere.service;

import com.example.worksphere.dto.response.JobPostingResponse;
import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.enums.JobStatus;
import com.example.worksphere.exception.ResourceNotFoundException;
import com.example.worksphere.mapper.JobPostingMapper;
import com.example.worksphere.repository.JobPostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Cung cấp chức năng xem tin tuyển dụng đã duyệt cho người dùng công khai.
@Service
public class PublicJobService {

    private final JobPostingRepository jobPostingRepository;
    private final JobPostingMapper jobPostingMapper;

    //Nhận repository và mapper cần cho nghiệp vụ tin tuyển dụng công khai.
    public PublicJobService(JobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobPostingMapper = jobPostingMapper;
    }

    //Lấy danh sách tin tuyển dụng đã duyệt, có thể tìm theo từ khóa.
    @Transactional(readOnly = true)
    public List<JobPostingResponse> getApprovedJobs(String keyword) {
        List<JobPosting> jobPostings = hasKeyword(keyword)
                ? jobPostingRepository.searchByKeywordAndStatus(keyword.trim(), JobStatus.APPROVED)
                : jobPostingRepository.findByStatus(JobStatus.APPROVED);

        return jobPostings.stream()
                .map(jobPostingMapper::toResponse)
                .toList();
    }

    //Lấy chi tiết tin tuyển dụng đã được duyệt.
    @Transactional(readOnly = true)
    public JobPostingResponse getApprovedJobById(Long id) {
        JobPosting jobPosting = jobPostingRepository.findByIdAndStatus(id, JobStatus.APPROVED)
                .orElseThrow(() -> new ResourceNotFoundException("Approved job posting not found"));

        return jobPostingMapper.toResponse(jobPosting);
    }

    //Kiểm tra từ khóa tìm kiếm có giá trị hay không.
    private boolean hasKeyword(String keyword) {
        return keyword != null && !keyword.isBlank();
    }
}
