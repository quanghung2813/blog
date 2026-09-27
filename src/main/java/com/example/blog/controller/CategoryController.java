package com.example.blog.controller;

import com.example.blog.dto.request.CategoryRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.dto.response.CategoryResponse;
import com.example.blog.entity.Category;
import com.example.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody @Valid CategoryRequest request
    ) {
        var response = ApiResponse.<CategoryResponse>builder()
                .result(categoryService.categories(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/categories")
    public ApiResponse<Page<CategoryResponse>> getCategory(@PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        var result = categoryService.listCategory(pageable);

        return ApiResponse.<Page<CategoryResponse>>builder()
                .result(result.getResult())
                .build();
    }
}
