//Xử lý lỗi khi không tìm thấy dữ liệu được yêu cầu.
package com.example.worksphere.exception;

//Đại diện lỗi tài nguyên không tồn tại.
public class ResourceNotFoundException extends RuntimeException {

    //Tạo lỗi không tìm thấy dữ liệu với thông báo cụ thể.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
