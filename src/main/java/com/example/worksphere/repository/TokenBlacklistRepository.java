//Truy cập dữ liệu token đã bị thu hồi.
package com.example.worksphere.repository;

import com.example.worksphere.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Cung cấp các truy vấn dữ liệu cho danh sách token bị chặn.
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    //Kiểm tra token đã bị thu hồi chưa.
    boolean existsByTokenString(String tokenString);

    //Tìm token bị thu hồi theo chuỗi token.
    Optional<TokenBlacklist> findByTokenString(String tokenString);
}
