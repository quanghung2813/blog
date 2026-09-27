package com.example.blog.mapper;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.response.PostResponse;
import com.example.blog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostRequest request);

    @Mapping(source = "category.id", target = "categoryId") // Ánh xạ ID của Category ra trường categoryId trong Response
    @Mapping(source = "category.name", target = "categoryName") // Nếu DTO của bạn có thêm tên danh mục
    @Mapping(source = "user.id", target = "userId") // Ánh xạ ID của tác giả bài viết
    PostResponse toPostResponse(Post post);
}
