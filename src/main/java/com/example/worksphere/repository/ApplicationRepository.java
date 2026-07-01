//Truy cập dữ liệu hồ sơ ứng tuyển.
package com.example.worksphere.repository;

import com.example.worksphere.entity.Application;
import com.example.worksphere.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Cung cấp các truy vấn dữ liệu cho hồ sơ ứng tuyển.
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    //Kiểm tra ứng viên đã nộp hồ sơ cho tin tuyển dụng này chưa.
    boolean existsByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);

    //Kiểm tra ứng viên còn hồ sơ ứng tuyển nào không.
    boolean existsByCandidateId(Long candidateId);

    //Kiểm tra tin tuyển dụng còn hồ sơ ứng tuyển nào không.
    boolean existsByJobPostingId(Long jobPostingId);

    //Tìm hồ sơ ứng tuyển theo ứng viên.
    List<Application> findByCandidateId(Long candidateId);

    //Tìm hồ sơ ứng tuyển theo tin tuyển dụng.
    List<Application> findByJobPostingId(Long jobPostingId);

    //Tìm hồ sơ theo id và nhà tuyển dụng sở hữu tin tuyển dụng.
    Optional<Application> findByIdAndJobPostingEmployerId(Long applicationId, Long employerId);

    //Tìm hồ sơ ứng tuyển theo trạng thái.
    List<Application> findByStatus(ApplicationStatus status);
}
