package com.example.blog.service;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.dto.response.CommentResponse;
import com.example.blog.entity.Comment;
import com.example.blog.enums.Roles;
import com.example.blog.exception.AppException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.repository.CommentRepository;
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

import javax.management.relation.Role;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    PostRepository postRepository;
    UserRepository userRepository;

    public CommentResponse createComment(CommentRequest request, long postId, long userId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Comment comment = commentMapper.toComment(request);
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment = commentRepository.save(comment);
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(postId)
                .userId(userId)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public Page<CommentResponse> comments(Pageable pageable, long postId) {
        Page<CommentResponse> comments = commentRepository.comments(pageable, postId);
        return comments;
    }

    public ResponseEntity<Void> deleteComment(long commentId, long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var commentCheck = commentRepository.findByIdAndUserId(commentId, userId);
        if (user.getRole().contains(Roles.ADMIN)) {
            if (commentRepository.findById(commentId).isEmpty()) {
                throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
            }
        } else if (commentCheck.isEmpty()) {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
