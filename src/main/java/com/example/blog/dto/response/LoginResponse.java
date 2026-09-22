package com.example.blog.dto.response;

import com.example.blog.enums.Roles;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    long id;
    String username;
    String email;
    String token;
    Set<Roles> role;
}
