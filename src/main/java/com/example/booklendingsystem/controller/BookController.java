package com.example.booklendingsystem.controller;

import com.example.booklendingsystem.model.Book;
import com.example.booklendingsystem.model.User;
import com.example.booklendingsystem.repository.BookRepository;
import com.example.booklendingsystem.repository.UserRepository;
import com.example.booklendingsystem.service.BookService;
import com.example.booklendingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.stream.events.Comment;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    @ResponseBody
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    @PostMapping
    @ResponseBody
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
    @GetMapping("/search")
    @ResponseBody
    public ModelAndView searchBooks(@RequestParam(required = false) Long id,
                                 @RequestParam(required = false) String author,
                                 @RequestParam(required = false) String isbn) {
        String decodedIsbn = java.net.URLDecoder.decode(isbn, java.nio.charset.StandardCharsets.UTF_8);
        Book book = bookService.getBookByIsbn(decodedIsbn);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("book", book);
        modelAndView.setViewName("example");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/{isbn}")
    public ModelAndView getBookDetails(@PathVariable String isbn, 
                                       @RequestParam(defaultValue = "0") int page) {
        String decodedIsbn = java.net.URLDecoder.decode(isbn, java.nio.charset.StandardCharsets.UTF_8);
        Book book = bookService.getBookByIsbn(decodedIsbn);
        User user = userService.getUserById(1L);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("book", book);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("book-detail");
        return modelAndView;
    }

    @PostMapping("/{bookId}/rate")
    public ResponseEntity<Book> rateBook(
            @PathVariable Long bookId,
            @RequestParam Long userId,
            @RequestParam("isLike") String isLikeStr) { // 改用 String 避免类型转换错误
        if (userId <= 0) {
            throw new IllegalArgumentException("无效的用户ID");
        }
        boolean isLike = Boolean.parseBoolean(isLikeStr); // 手动解析布尔值
        Book updatedBook = bookService.rateBook(bookId, userId, isLike);
        return ResponseEntity.ok(updatedBook);
    }

}