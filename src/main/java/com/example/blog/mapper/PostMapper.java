package com.example.blog.mapper;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest request);
}
