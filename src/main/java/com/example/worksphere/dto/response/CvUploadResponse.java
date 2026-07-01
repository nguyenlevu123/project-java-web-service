//Chuẩn hóa dữ liệu trả về sau khi tải CV lên.
package com.example.worksphere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Chứa thông tin file CV đã lưu.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvUploadResponse {

    //Tên file đã lưu trên hệ thống.
    private String fileName;

    //Đường dẫn truy cập file CV.
    private String fileUrl;

    //Kích thước file tính bằng byte.
    private Long fileSize;

    //Loại nội dung của file.
    private String contentType;
}
