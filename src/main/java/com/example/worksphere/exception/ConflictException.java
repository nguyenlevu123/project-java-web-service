//Xử lý lỗi khi dữ liệu bị trùng hoặc xung đột.
package com.example.worksphere.exception;

//Đại diện lỗi xung đột dữ liệu trong hệ thống.
public class ConflictException extends RuntimeException {

    //Tạo lỗi xung đột dữ liệu với thông báo cụ thể.
    public ConflictException(String message) {
        super(message);
    }
}
