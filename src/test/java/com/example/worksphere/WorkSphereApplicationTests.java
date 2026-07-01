//Kiểm tra ngữ cảnh khởi động của ứng dụng WorkSphere.
package com.example.worksphere;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//Kiểm tra ứng dụng Spring Boot tải ngữ cảnh thành công.
@ActiveProfiles("test")
@SpringBootTest
class WorkSphereApplicationTests {

    //Xác nhận ngữ cảnh ứng dụng khởi động được.
    @Test
    void contextLoads() {
    }

}
