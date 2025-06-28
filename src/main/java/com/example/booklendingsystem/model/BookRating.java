package com.example.booklendingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_ratings")
@Data
public class BookRating {

    public BookRating() {
        // JPA required no-arg constructor
    }
    
    public BookRating(Long userId, Long bookId, RatingType ratingType) {
        this.userId = userId;
        this.bookId = bookId;
        this.ratingType = ratingType;
        this.createdAt = LocalDateTime.now();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating_type", nullable = false)
    private RatingType ratingType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum RatingType {
        LIKE, DISLIKE
    }
}