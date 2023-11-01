package com.esufam.megami.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "posts")
@EntityListeners( AuditingEntityListener.class )
public class Post {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String title;
    private String description;
    private @Column(unique = true, nullable = false) String filename;
    private @CreatedBy @Column(nullable = false) Integer userId;
    private @CreationTimestamp Timestamp createdAt;
    private @UpdateTimestamp Timestamp updatedAt;
}
