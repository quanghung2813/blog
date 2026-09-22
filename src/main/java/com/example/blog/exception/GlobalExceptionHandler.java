package com.example.blog.exception;

import com.example.blog.dto.response.ApiResponse;
import com.example.blog.utils.Constance;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException e) {
        e.printStackTrace();
        String defaultMessage = e.getFieldError().getDefaultMessage();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(defaultMessage)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Throwable.class)
    ResponseEntity<ApiResponse> handleException(Throwable e) {
        e.printStackTrace();
        String defaultMessage = "Validation error";
        if (e.getMessage() != null) {
            defaultMessage = e.getMessage();
        }

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(400)
                .message(defaultMessage)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handleDuplicateKey(DataIntegrityViolationException e) {
        e.printStackTrace();
        var error = ErrorCode.UNKNOW_EXCEPTION;
        String defaultMessage = e.getMessage().toLowerCase();
        if (defaultMessage.contains(Constance.UK_USER_MAIL)) {
            error = ErrorCode.EMAIL_EXISTED;
        } else if (defaultMessage.contains(Constance.UK_USER_NAME)) {
            error = ErrorCode.USER_EXISTED;
        } else if (defaultMessage.contains(Constance.UK_CATEGORY_SLUG)) {
            error = ErrorCode.SLUG_EXISTED;
        } else if (defaultMessage.contains(Constance.UK_POST_SLUG)) {
            error = ErrorCode.SLUG_EXISTED;
        }
        var data = ApiResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(data);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handleException(HttpMessageNotReadableException e) {
        e.printStackTrace();
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);

        String errorMessage = "Dữ liệu JSON gửi lên không hợp lệ.";

        // Kiểm tra xem lỗi có phải do lệch kiểu dữ liệu hoặc sai định dạng từ Jackson hay không
        Throwable cause = e.getCause();
        if (cause instanceof MismatchedInputException mismatchedEx) {
            if (!mismatchedEx.getPath().isEmpty()) {
                // Lấy tên trường đang bị lỗi (ví dụ: id, price, age,...)
                String fieldName = mismatchedEx.getPath().get(0).getPropertyName();

                // Lấy kiểu dữ liệu đích mà Java đang mong đợi (ví dụ: Long, Integer, Double,...)
                String targetType = mismatchedEx.getTargetType().getSimpleName();

                // Tự động sinh ra câu thông báo chi tiết cho BẤT KỲ trường nào bị sai
                errorMessage = String.format("Trường '%s' phải có kiểu dữ liệu là %s, vui lòng kiểm tra lại giá trị đã truyền.", fieldName, targetType);
            }
        }

        response.setMessage(errorMessage);
        return ResponseEntity.badRequest().body(response);
    }
}
