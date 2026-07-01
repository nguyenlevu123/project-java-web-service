//Chuyển đổi dữ liệu người dùng sang DTO trả về.
package com.example.worksphere.mapper;

import com.example.worksphere.dto.response.UserResponse;
import com.example.worksphere.entity.User;
import org.springframework.stereotype.Component;

//Ánh xạ User sang UserResponse an toàn cho client.
@Component
public class UserMapper {

    //Chuyển User entity sang response, không trả về mật khẩu.
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
