package com.example.blog.dto.response;

import com.example.blog.enums.Roles;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    long id;
    String username;
    String email;
    Set<Roles> role;
}
