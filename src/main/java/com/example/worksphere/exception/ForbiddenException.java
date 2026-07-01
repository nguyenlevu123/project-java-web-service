//Xử lý lỗi khi người dùng không có đủ quyền truy cập.
package com.example.worksphere.exception;

//Đại diện lỗi bị từ chối quyền truy cập.
public class ForbiddenException extends RuntimeException {

    //Tạo lỗi phân quyền với thông báo cụ thể.
    public ForbiddenException(String message) {
        super(message);
    }
}
