package com.example.blog.dto.request;

import com.example.blog.utils.Constance;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @Email(message = Constance.EMAIL_INVALID)
    String email;

    @Size(min = 6, message = Constance.PASSWORD_INVALID)
    String passwordHash;
}
