package com.example.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
    @NotBlank(message = "Title is Empty")
    String title;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug chỉ được chứa chữ thường, số và dấu gạch ngang")
    String slug;

    @NotBlank(message = "Content is Empty")
    String content;

    @Pattern(regexp = "^(DRAFT|PUBLISHED)$", message = "Status must be DRAFT or PUBLISHED.")
    String status;

    @NotNull(message = "categoryId is empty")
    long categoryId;
}
