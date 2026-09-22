package com.example.blog.controller;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.request.StatusRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.dto.response.PostResponse;
import com.example.blog.service.PostService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestBody @Valid PostRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long authorId = jwt.getClaim("userId");

        var response = ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request, authorId))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @PageableDefault(page = 0, size = 20, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long userId = -1;
        if (jwt != null) {
            userId = jwt.getClaim("userId");
        }
        var response = ApiResponse.<Page<PostResponse>>builder()
                .result(postService.posts(pageable, userId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable("id") long id, @AuthenticationPrincipal Jwt jwt) {
        long userId = jwt.getClaim("userId");
        var response = ApiResponse.<PostResponse>builder()
                .result(postService.getPost(id, userId))
                .build();
        return response;
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long userId = jwt.getClaim("userId");
        return postService.deletePost(id, userId);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable("id") long id,
            @RequestBody @Valid PostRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long userId = jwt.getClaim("userId");
        var response = ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(id, request, userId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/post/{id}/status")
    public ResponseEntity<ApiResponse<PostResponse>> updatePostStatus(
            @PathVariable("id") long id,
            @RequestBody @Valid StatusRequest request,
            @AuthenticationPrincipal Jwt jwt
    ){
        long userId = jwt.getClaim("userId");
        var response = ApiResponse.<PostResponse>builder()
                .result(postService.updatePostStatus(id, request, userId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
