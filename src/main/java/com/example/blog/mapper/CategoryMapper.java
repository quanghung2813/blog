package com.example.blog.mapper;

import com.example.blog.dto.request.CategoryRequest;
import com.example.blog.dto.response.CategoryResponse;
import com.example.blog.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
}
