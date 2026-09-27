package com.example.blog.exception;

import com.example.blog.utils.Constance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNKNOW_EXCEPTION(1000, "Unknow error", HttpStatus.BAD_GATEWAY),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTED(1001,"Email existed", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002,"User not existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1006, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    EMAIL_INVALID(1008, "Email invalid", HttpStatus.BAD_REQUEST),
    NEED_LOGIN(1009, "Need to login", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(1010, "User already existed", HttpStatus.CONFLICT),
    POST_EXISTED(1011, "Post already existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1012, "Category not found", HttpStatus.BAD_REQUEST),
    SLUG_EXISTED(1013, "Slug already existed", HttpStatus.CONFLICT),
    CATEGORY_NAME_INVALID(1014, Constance.CATEGORY_NAME_INVALID, HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1015, "Post not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1016, "User not found", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(1017, "Comment not found", HttpStatus.BAD_REQUEST),
    FORBIDDEN(1018, "You do not have sufficient permissions", HttpStatus.FORBIDDEN),
    LOGGED_OUT(1019, "User has logged out.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1020, "Token already expired", HttpStatus.UNAUTHORIZED),
    POST_NOT_EXISTED(1021, "Post not existed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    int code;
    String message;
    HttpStatus httpStatus;
}
