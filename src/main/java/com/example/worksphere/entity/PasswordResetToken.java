//Lưu token đặt lại mật khẩu cho luồng quên mật khẩu.
package com.example.worksphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Đại diện token dùng một lần để đặt lại mật khẩu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    //Khóa chính của token đặt lại mật khẩu.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Chuỗi token duy nhất gửi cho người dùng.
    @Column(nullable = false, unique = true)
    private String token;

    //Người dùng sở hữu token đặt lại mật khẩu.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Thời điểm token hết hạn.
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    //Đánh dấu token đã được sử dụng hay chưa.
    @Builder.Default
    @Column(nullable = false)
    private Boolean used = false;

    //Thời điểm tạo token.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //Thiết lập dữ liệu mặc định khi tạo token.
    @PrePersist
    public void prePersist() {
        if (used == null) {
            used = false;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
