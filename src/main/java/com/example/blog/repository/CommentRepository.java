package com.example.blog.repository;

import com.example.blog.dto.response.CommentResponse;
import com.example.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query("""
    SELECT new com.example.blog.dto.response.CommentResponse(
    c.id, c.content, p.id, u.id, c.createdAt
    )
    FROM Comment c
    JOIN Post p on c.postId = p.id
    JOIN User u on u.id = c.userId
    WHERE c.postId = :postId
    """)
    Page<CommentResponse> comments(Pageable pageable, @Param("postId") long postId);

    Optional<Comment> findByIdAndUserId(long id, long userId);
}
