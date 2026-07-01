//Xử lý lỗi khi người dùng chưa xác thực hoặc token không hợp lệ.
package com.example.worksphere.exception;

//Đại diện lỗi chưa được xác thực.
public class UnauthorizedException extends RuntimeException {

    //Tạo lỗi xác thực với thông báo cụ thể.
    public UnauthorizedException(String message) {
        super(message);
    }
}
