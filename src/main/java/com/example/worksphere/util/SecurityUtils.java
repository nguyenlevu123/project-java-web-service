//Lấy thông tin người dùng đang xác thực từ SecurityContext.
package com.example.worksphere.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//Cung cấp tiện ích đọc username hiện tại một cách an toàn.
public final class SecurityUtils {

    private static final String ANONYMOUS_USER = "anonymousUser";

    //Không cho tạo đối tượng tiện ích.
    private SecurityUtils() {
    }

    //Lấy username hiện tại nếu request đã xác thực.
    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String username = authentication.getName();
        if (username == null || username.isBlank() || ANONYMOUS_USER.equals(username)) {
            return Optional.empty();
        }

        return Optional.of(username);
    }

    //Trả về username hiện tại hoặc anonymous nếu chưa xác thực.
    public static String getCurrentUsernameOrAnonymous() {
        return getCurrentUsername().orElse("anonymous");
    }
}
