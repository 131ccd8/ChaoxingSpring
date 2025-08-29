package com.iaai.chaoxing.chaoxingsignweb.service;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private long timestamp;

    // 构造方法、getter和setter
    public static <String> ApiResponse<String> success(String message) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message.toString());
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static ApiResponse<String> error(int code, String message) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
