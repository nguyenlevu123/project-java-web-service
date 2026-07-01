//Chuẩn hóa dữ liệu phản hồi thành công của API.
package com.example.worksphere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Định dạng phản hồi chung cho các API thành công.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    //Trạng thái thành công của phản hồi.
    private boolean success;

    //Thông báo trả về cho client.
    private String message;

    //Dữ liệu chính của phản hồi.
    private T data;

    //Tạo phản hồi thành công có dữ liệu.
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    //Tạo phản hồi thành công không có dữ liệu.
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    //Tạo phản hồi lỗi đơn giản.
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
