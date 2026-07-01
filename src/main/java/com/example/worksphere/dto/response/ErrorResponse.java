//Chuẩn hóa dữ liệu phản hồi lỗi của API.
package com.example.worksphere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Định dạng phản hồi chung khi API gặp lỗi.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    //Thời điểm xảy ra lỗi.
    private LocalDateTime timestamp;

    //Mã trạng thái HTTP.
    private int status;

    //Tên lỗi HTTP.
    private String error;

    //Thông báo lỗi an toàn cho client.
    private String message;

    //Đường dẫn API gây lỗi.
    private String path;
}
