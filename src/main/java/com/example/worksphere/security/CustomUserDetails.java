//Bọc thông tin người dùng để Spring Security xác thực.
package com.example.worksphere.security;

import com.example.worksphere.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Đại diện người dùng đã được nạp vào Spring Security.
public class CustomUserDetails implements UserDetails {

    //Người dùng gốc trong hệ thống.
    private final User user;

    //Tạo UserDetails từ entity User.
    public CustomUserDetails(User user) {
        this.user = user;
    }

    //Trả về entity người dùng gốc.
    public User getUser() {
        return user;
    }

    //Trả về quyền theo vai trò của người dùng.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    //Trả về mật khẩu đã mã hóa.
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    //Trả về email dùng làm tên đăng nhập.
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //Cho phép tài khoản không hết hạn.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Cho phép tài khoản không bị khóa.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Cho phép thông tin xác thực không hết hạn.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Kiểm tra tài khoản còn hoạt động không.
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getIsActive());
    }
}
