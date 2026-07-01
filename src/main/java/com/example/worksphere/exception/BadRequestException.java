//Xử lý lỗi khi client gửi dữ liệu không hợp lệ.
package com.example.worksphere.exception;

//Đại diện lỗi yêu cầu không hợp lệ từ client.
public class BadRequestException extends RuntimeException {

    //Tạo lỗi yêu cầu không hợp lệ với thông báo cụ thể.
    public BadRequestException(String message) {
        super(message);
    }
}
