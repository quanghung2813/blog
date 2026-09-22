package com.example.blog.entity;

import com.example.blog.enums.Roles;
import com.example.blog.utils.Constance;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Target;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = Constance.UK_USER_MAIL, columnNames = "email"),
        @UniqueConstraint(name = Constance.UK_USER_NAME, columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true, name = "username")
    String username;

    // @Column(nullable = false, unique = true) đảm bảo email là duy nhất và không để trống
    @Column(nullable = false, unique = true, name = "email")
    String email;

    String passwordHash;
    Set<Roles> role;
}
