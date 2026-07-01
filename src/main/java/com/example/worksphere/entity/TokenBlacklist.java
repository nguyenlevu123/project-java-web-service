//Lưu token đã bị thu hồi để chặn sử dụng lại.
package com.example.worksphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Đại diện token không còn hợp lệ trong hệ thống.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {

    //Khóa chính của token bị thu hồi.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Chuỗi token đã bị thu hồi.
    @Column(name = "token_string", nullable = false, unique = true, length = 512)
    private String tokenString;

    //Thời gian token bị thu hồi.
    @Column(name = "revoked_at", nullable = false, updatable = false)
    private LocalDateTime revokedAt;

    //Thiết lập thời gian thu hồi khi tạo bản ghi.
    @PrePersist
    public void prePersist() {
        if (revokedAt == null) {
            revokedAt = LocalDateTime.now();
        }
    }
}
