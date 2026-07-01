//Lưu thông tin tin tuyển dụng của nhà tuyển dụng.
package com.example.worksphere.entity;

import com.example.worksphere.enums.JobStatus;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Đại diện một tin tuyển dụng trong hệ thống.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_postings")
public class JobPosting {

    //Khóa chính của tin tuyển dụng.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nội dung chính của tin tuyển dụng.
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "salary_range")
    private String salaryRange;

    //Trạng thái duyệt và hiển thị của tin tuyển dụng.
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.DRAFT;

    //Thời gian tạo và cập nhật tin tuyển dụng.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Nhà tuyển dụng sở hữu tin tuyển dụng.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    //Danh sách hồ sơ ứng tuyển vào tin này.
    @Builder.Default
    @OneToMany(mappedBy = "jobPosting")
    private List<Application> applications = new ArrayList<>();

    //Thiết lập dữ liệu mặc định khi tạo tin tuyển dụng.
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = JobStatus.DRAFT;
        }
        createdAt = LocalDateTime.now();
    }

    //Cập nhật thời gian chỉnh sửa tin tuyển dụng.
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
