package com.example.blog.service;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.request.StatusRequest;
import com.example.blog.dto.response.PostResponse;
import com.example.blog.entity.Post;
import com.example.blog.enums.Roles;
import com.example.blog.enums.Status;
import com.example.blog.exception.AppException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.mapper.PostMapper;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public PostResponse createPost(PostRequest request, long authorId) {
        if (categoryRepository.findById(request.getCategoryId()).isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!postRepository.findBySlug(request.getSlug()).isEmpty()) {
            throw new AppException(ErrorCode.POST_EXISTED);
        }

        Post post = postMapper.toPost(request);
        post.setAuthorId(authorId);
        post.setStatus(Status.DRAFT);

        post = postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public Page<PostResponse> posts(Pageable pageable, long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            Page<PostResponse> posts = postRepository.posts(pageable, false, userId);
            return posts;
        }
        if (user.get().getRole().contains(Roles.ADMIN)) {
            return postRepository.posts(pageable, true, userId);
        } else {
            return postRepository.posts(pageable, false, userId);
        }
    }

    public PostResponse getPost(long id, Long userId) {

        var post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            if (post.getStatus().equals(Status.DRAFT)) {
                throw new AppException(ErrorCode.FORBIDDEN);
            }
            return PostResponse.builder()
                    .id(post.getId())
                    .authorId(post.getAuthorId())
                    .title(post.getTitle())
                    .slug(post.getSlug())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
        if (user.get().getRole().contains(Roles.ADMIN)) {
            return PostResponse.builder()
                    .id(post.getId())
                    .authorId(post.getAuthorId())
                    .title(post.getTitle())
                    .slug(post.getSlug())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        } else {
            if (!userId.equals(post.getAuthorId())) {
                if (post.getStatus().equals(Status.DRAFT)) {
                    throw new AppException(ErrorCode.FORBIDDEN);
                }
            }
            return PostResponse.builder()
                    .id(post.getId())
                    .authorId(post.getAuthorId())
                    .title(post.getTitle())
                    .slug(post.getSlug())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }

    public ResponseEntity<Void> deletePost(long id, long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var postCheck = postRepository.findByIdAndAuthorId(id, userId);
        if (user.getRole().contains(Roles.ADMIN)) {
            if (postRepository.findById(id).isEmpty()) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }
        } else if (postCheck.isEmpty()){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        postRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public PostResponse updatePost(long id, PostRequest request, long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var postCheck = postRepository.findByIdAndAuthorId(id, userId);
        if (user.getRole().contains(Roles.ADMIN)) {
            if (postRepository.findById(id).isEmpty()) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }
        }  else if (postCheck.isEmpty()){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        var postUpdate = postMapper.toPost(request);
        postUpdate = postRepository.save(postUpdate);

        return PostResponse.builder()
                .id(postUpdate.getId())
                .authorId(postUpdate.getAuthorId())
                .title(postUpdate.getTitle())
                .slug(postUpdate.getSlug())
                .content(postUpdate.getContent())
                .status(postUpdate.getStatus())
                .createdAt(postUpdate.getCreatedAt())
                .updatedAt(postUpdate.getUpdatedAt())
                .build();
    }

    public PostResponse updatePostStatus(long id, StatusRequest request, long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var postCheck = postRepository.findByIdAndAuthorId(id, userId);
        if (user.getRole().contains(Roles.ADMIN)) {
            if (postRepository.findById(id).isEmpty()) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }
        }  else if (postCheck.isEmpty()){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        post.setStatus(Status.valueOf(request.getStatus()));
        post = postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
