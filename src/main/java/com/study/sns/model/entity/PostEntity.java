package com.study.sns.model.entity;

import com.study.sns.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"post\"")
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() where id = ?")
@FilterDef(name = "deletedFilter2")
@Filter(name = "deletedFilter2", condition = "deleted_at is null")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static PostEntity of(String title, String body, UserEntity userEntity) {
        PostEntity entity = new PostEntity();
        entity.setTitle(title);
        entity.setBody(body);
        entity.setUser(userEntity);
        return entity;
    }

}
