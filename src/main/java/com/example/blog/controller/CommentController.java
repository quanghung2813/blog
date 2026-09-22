package com.example.blog.controller;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.dto.response.CommentResponse;
import com.example.blog.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
public class CommentController {
    CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> postComment(
            @PathVariable("postId") long postId,
            @RequestBody @Valid CommentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long userId = jwt.getClaim("userId");

        var response = ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request, postId, userId))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComment(
            @PageableDefault(page = 0, size = 20, sort = "createdAt") Pageable pageable,
            @PathVariable("postId") long postId) {
        var response = ApiResponse.<Page<CommentResponse>>builder()
                .result(commentService.comments(pageable, postId))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("id" ) long commentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long userId = jwt.getClaim("userId");
        return commentService.deleteComment(commentId, userId);
    }
}
