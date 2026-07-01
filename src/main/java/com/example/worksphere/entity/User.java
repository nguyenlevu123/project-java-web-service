//Lưu thông tin người dùng và vai trò trong hệ thống.
package com.example.worksphere.entity;

import com.example.worksphere.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

//Đại diện tài khoản của admin, nhà tuyển dụng và ứng viên.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    //Khóa chính của người dùng.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Thông tin đăng nhập và hiển thị của người dùng.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    //Vai trò và trạng thái hoạt động của tài khoản.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    //Thời gian tạo và cập nhật tài khoản.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Danh sách tin tuyển dụng do nhà tuyển dụng tạo.
    @Builder.Default
    @OneToMany(mappedBy = "employer")
    private List<JobPosting> jobPostings = new ArrayList<>();

    //Danh sách hồ sơ ứng tuyển của ứng viên.
    @Builder.Default
    @OneToMany(mappedBy = "candidate")
    private List<Application> applications = new ArrayList<>();

    //Thiết lập dữ liệu mặc định khi tạo người dùng.
    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
        createdAt = LocalDateTime.now();
    }

    //Cập nhật thời gian chỉnh sửa người dùng.
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
