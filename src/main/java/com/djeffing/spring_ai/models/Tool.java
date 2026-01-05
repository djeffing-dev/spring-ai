package com.djeffing.spring_ai.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tools")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    @Column(nullable = false, length = 2048)
    private String assessToken;

    @Column(length = 2048)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

    public static Tool build(String name, long userId, String accessToken){
        User user = new  User();
        user.setId(userId);

        Tool tool = new  Tool();
        tool.setName(name);
        tool.setAssessToken(accessToken);
        tool.setUserId(user);
        return  tool;
    }


}

