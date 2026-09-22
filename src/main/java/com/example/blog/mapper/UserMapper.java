package com.example.blog.mapper;

import com.example.blog.dto.request.RegisterRequest;
import com.example.blog.dto.response.RegisterResponse;
import com.example.blog.entity.User;
import com.example.blog.enums.Roles;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Set;
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);

    RegisterResponse toUserResponse(User user);

    default Set<Roles> map(String value) {
        if (value == null) {
            return null;
        }
        Roles role = Roles.valueOf(value);
        return Collections.singleton(role);
    }
}