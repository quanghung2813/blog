package com.example.blog.mapper;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest request);
}
