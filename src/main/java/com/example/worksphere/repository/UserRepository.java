//Truy cập dữ liệu người dùng trong hệ thống.
package com.example.worksphere.repository;

import com.example.worksphere.entity.User;
import com.example.worksphere.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//Cung cấp các truy vấn dữ liệu cho người dùng.
public interface UserRepository extends JpaRepository<User, Long> {

    //Tìm người dùng theo email để phục vụ đăng nhập.
    Optional<User> findByEmail(String email);

    //Kiểm tra email đã tồn tại trong hệ thống chưa.
    boolean existsByEmail(String email);

    //Kiểm tra email đã thuộc tài khoản khác chưa.
    boolean existsByEmailAndIdNot(String email, Long id);

    //Tìm danh sách người dùng theo vai trò.
    List<User> findByRole(Role role);

    //Tìm danh sách người dùng theo vai trò và trạng thái.
    List<User> findByRoleAndIsActive(Role role, Boolean isActive);

    //Tìm danh sách người dùng theo trạng thái hoạt động.
    List<User> findByIsActive(Boolean isActive);

    //Tìm người dùng theo email hoặc tên, kết hợp bộ lọc vai trò và trạng thái.
    @Query("""
            SELECT u FROM User u
            WHERE (:role IS NULL OR u.role = :role)
              AND (:isActive IS NULL OR u.isActive = :isActive)
              AND (
                    LOWER(u.email) LIKE LOWER(CONCAT(CONCAT('%', :keyword), '%'))
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT(CONCAT('%', :keyword), '%'))
              )
            """)
    List<User> searchByKeywordAndFilters(
            @Param("keyword") String keyword,
            @Param("role") Role role,
            @Param("isActive") Boolean isActive
    );
}
