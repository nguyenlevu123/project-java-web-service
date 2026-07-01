//Truy cập dữ liệu tin tuyển dụng.
package com.example.worksphere.repository;

import com.example.worksphere.entity.JobPosting;
import com.example.worksphere.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Cung cấp các truy vấn dữ liệu cho tin tuyển dụng.
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //Tìm tin tuyển dụng theo nhà tuyển dụng.
    List<JobPosting> findByEmployerId(Long employerId);

    //Kiểm tra nhà tuyển dụng còn tin tuyển dụng nào không.
    boolean existsByEmployerId(Long employerId);

    //Tìm tin tuyển dụng theo id và đúng nhà tuyển dụng sở hữu.
    Optional<JobPosting> findByIdAndEmployerId(Long id, Long employerId);

    //Tìm tin tuyển dụng theo trạng thái.
    List<JobPosting> findByStatus(JobStatus status);

    //Tìm tin tuyển dụng theo id và trạng thái.
    Optional<JobPosting> findByIdAndStatus(Long id, JobStatus status);

    //Tìm tin tuyển dụng theo nhà tuyển dụng và trạng thái.
    List<JobPosting> findByEmployerIdAndStatus(Long employerId, JobStatus status);
}
