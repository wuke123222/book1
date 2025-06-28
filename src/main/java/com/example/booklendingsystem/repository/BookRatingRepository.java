package com.example.booklendingsystem.repository;

import com.example.booklendingsystem.model.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {
    @Modifying
    @Query(value = "INSERT INTO book_ratings (user_id, book_id, rating_type) VALUES (?1, ?2, ?3) " +
            "ON DUPLICATE KEY UPDATE rating_type = ?3", nativeQuery = true)
    void saveOrUpdate(Long userId, Long bookId, String ratingType);
    // 根据用户和书籍查找评分记录
    Optional<BookRating> findByUserIdAndBookId(Long userId, Long bookId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}