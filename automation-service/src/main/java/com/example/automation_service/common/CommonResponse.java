package com.example.automation_service.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String status;
    private String message;
    private T data;
}
