//Khởi động ứng dụng RESTful API WorkSphere.
package com.example.worksphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Cấu hình điểm bắt đầu của ứng dụng Spring Boot.
@SpringBootApplication
public class WorkSphereApplication {

    //Chạy ứng dụng WorkSphere.
    public static void main(String[] args) {
        SpringApplication.run(WorkSphereApplication.class, args);
    }

}
