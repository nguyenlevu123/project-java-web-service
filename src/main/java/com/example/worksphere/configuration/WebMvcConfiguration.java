//Cấu hình truy cập file tĩnh được tải lên cục bộ.
package com.example.worksphere.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

//Mở đường dẫn /uploads/** tới thư mục uploads.
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    //Ánh xạ URL uploads tới thư mục uploads trên máy chủ.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath.toUri().toString());
    }
}
