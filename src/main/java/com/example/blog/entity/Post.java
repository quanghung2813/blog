package com.example.blog.entity;

import com.example.blog.enums.Status;
import com.example.blog.utils.Constance;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = Constance.UK_POST_SLUG, columnNames = "slug")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, unique = true, name = "slug")
    String slug;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Status status;

    @Column(nullable = false)
    long authorId;

    long categoryId;

    @CreationTimestamp
    Date createdAt;

    @UpdateTimestamp
    Date updatedAt;
}
