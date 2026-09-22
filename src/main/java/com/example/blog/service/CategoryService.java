package com.example.blog.service;

import com.example.blog.dto.request.CategoryRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.dto.response.CategoryResponse;
import com.example.blog.entity.Category;
import com.example.blog.exception.AppException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.mapper.CategoryMapper;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.utils.Constance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse categories(CategoryRequest request, String token) {
        if (!categoryRepository.findBySlug(request.getSlug()).isEmpty()) {
            throw new DataIntegrityViolationException(Constance.UK_CATEGORY_SLUG);
        }

        Category category = categoryMapper.toCategory(request);

        category = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

    public ApiResponse<List<Category>> listCategory() {
        var result = categoryRepository.findAll();

        return ApiResponse.<List<Category>>builder()
                .result(result)
                .build();
    }
}
