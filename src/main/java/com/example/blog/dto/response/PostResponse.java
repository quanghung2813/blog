package com.example.blog.dto.response;

import com.example.blog.enums.Status;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    long id;
    long authorId;
    long categoryId;
    String categoryTitle;
    String userName;
    String title;
    String slug;
    String content;
    Status status;
    Date createdAt;
    Date updatedAt;
}
