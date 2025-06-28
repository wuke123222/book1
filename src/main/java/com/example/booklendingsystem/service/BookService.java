package com.example.booklendingsystem.service;

import com.example.booklendingsystem.model.Book;
import com.example.booklendingsystem.model.BookRating;
import com.example.booklendingsystem.model.User;
import com.example.booklendingsystem.repository.BookRepository;
import com.example.booklendingsystem.repository.BookRatingRepository;
import com.example.booklendingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookRatingRepository bookRatingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * 处理书籍评分（点赞/点踩）
     * @param bookId 书籍ID
     * @param userId 用户ID
     * @param isLike true为点赞，false为点踩
     * @return 更新后的书籍信息
     */
    @Transactional
    public Book rateBook(Long bookId, Long userId, boolean isLike) {

        // 校验用户和书籍存在性
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("书籍不存在"));

        // 检查是否已存在评分
        Optional<BookRating> existingRating = bookRatingRepository.findByUserIdAndBookId(userId, bookId);

        // 保存旧评分类型用于后续计数更新
        BookRating.RatingType oldRatingType = existingRating.map(BookRating::getRatingType).orElse(null);

        // 如果已存在评分且类型未改变，直接返回
        if (existingRating.isPresent() && existingRating.get().getRatingType() == (isLike ? BookRating.RatingType.LIKE : BookRating.RatingType.DISLIKE)) {
            return book;
        }

        // 创建新评分记录
        BookRating newRating = new BookRating(userId, bookId, isLike ? BookRating.RatingType.LIKE : BookRating.RatingType.DISLIKE);

        existingRating.ifPresent(rating -> {
            bookRatingRepository.deleteById(rating.getId());
            // 更新计数器逻辑
            if (rating.getRatingType() == BookRating.RatingType.LIKE) {
                book.setLikeCount(Math.max(0, book.getLikeCount() - 1));
            } else {
                book.setDislikeCount(Math.max(0, book.getDislikeCount() - 1));
            }
        });

// 保存或更新评分
        bookRatingRepository.saveOrUpdate(userId, bookId, isLike ? BookRating.RatingType.LIKE.name() : BookRating.RatingType.DISLIKE.name());

// 更新新的评分计数
        if (isLike) {
            book.setLikeCount(book.getLikeCount() + 1);
        } else {
            book.setDislikeCount(book.getDislikeCount() + 1);
        }

        // 保存更新后的书籍信息
        return bookRepository.save(book);
    }
}