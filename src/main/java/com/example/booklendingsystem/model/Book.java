package com.example.booklendingsystem.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    private String title;
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "author")
    private String author;
    public String getAuthor() {
        return this.author;
    }

    @Setter
    @Column(name = "isbn", unique = true)
    private String isbn;
    public String getIsbn() {
        return this.isbn;
    }

    @Setter
    @Column(name = "published_date")
    private LocalDate publishedDate;
    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }

    @Column(name = "available")
    private boolean available;
    public boolean isAvailable() {
        return this.available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    @Column(name = "num", nullable = false)
    private Long num;
    
    // 新增分类字段
    @Column(name = "category")
    private String category;

    public Long getNum() {
        return this.num;
    }
    
    public void setNum(Long num) {
        this.num = num;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "like_count")
    private Long likeCount;

    public Long getLikeCount() {
        return this.likeCount;
    }
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
    @Column(name = "dislike_count")
    private Long dislikeCount;
    public Long getDislikeCount() {
        return this.dislikeCount;
    }
    public void setDislikeCount(Long dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
