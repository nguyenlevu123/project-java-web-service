//Tải thông tin người dùng theo email cho Spring Security.
package com.example.worksphere.security;

import com.example.worksphere.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Cung cấp UserDetails từ dữ liệu người dùng trong database.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //Nhận repository để tìm người dùng theo email.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Tải người dùng theo email khi xác thực.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
