//Lưu thông tin hồ sơ ứng tuyển của ứng viên.
package com.example.worksphere.entity;

import com.example.worksphere.enums.ApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Đại diện hồ sơ ứng tuyển vào một tin tuyển dụng.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_candidate_job",
                        columnNames = {"candidate_id", "job_posting_id"}
                )
        }
)
public class Application {

    //Khóa chính của hồ sơ ứng tuyển.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nội dung ứng tuyển và đường dẫn CV.
    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "cv_url", nullable = false)
    private String cvUrl;

    //Trạng thái xử lý hồ sơ ứng tuyển.
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    //Thời gian nộp và cập nhật hồ sơ.
    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Ứng viên nộp hồ sơ.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    //Tin tuyển dụng được ứng tuyển.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    //Thiết lập dữ liệu mặc định khi tạo hồ sơ ứng tuyển.
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = ApplicationStatus.PENDING;
        }
        appliedAt = LocalDateTime.now();
    }

    //Cập nhật thời gian chỉnh sửa hồ sơ ứng tuyển.
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
