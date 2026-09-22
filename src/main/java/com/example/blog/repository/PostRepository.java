package com.example.blog.repository;

import com.example.blog.dto.response.PostResponse;
import com.example.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    @Query("""
    SELECT new com.example.blog.dto.response.PostResponse(
        p.id, u.id, c.id, c.name, u.username, p.title, p.slug, p.content, p.status, p.createdAt, p.updatedAt
        )
        FROM Post p
        JOIN Category c on p.categoryId = c.id
        JOIN User u on p.authorId = u.id
        WHERE :isAdmin = true or p.authorId = :userId or (p.status = PUBLISHED and :userId = -1)
    """)
    Page<PostResponse> posts(Pageable pageable, @Param("isAdmin") boolean isAdmin, @Param("userId") long userId);

    Optional<Post> findByIdAndAuthorId(long id, long userId);
}
