package com.example.booklendingsystem.repository;

import com.example.booklendingsystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 新增按分类查询方法
    List<Book> findByCategory(String category);

    Book findByIsbn(String isbn);
}
