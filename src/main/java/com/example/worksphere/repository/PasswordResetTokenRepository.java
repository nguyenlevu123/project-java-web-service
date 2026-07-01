//Truy cập dữ liệu token đặt lại mật khẩu.
package com.example.worksphere.repository;

import com.example.worksphere.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Cung cấp truy vấn dữ liệu cho token đặt lại mật khẩu.
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    //Tìm token đặt lại mật khẩu theo chuỗi token.
    Optional<PasswordResetToken> findByToken(String token);

    //Tìm các token chưa dùng của một người dùng.
    List<PasswordResetToken> findByUserIdAndUsedFalse(Long userId);

    //Xóa các token đặt lại mật khẩu của một người dùng.
    void deleteByUserId(Long userId);
}
