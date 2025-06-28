package com.example.booklendingsystem.service;

import com.example.booklendingsystem.model.Book;
import com.example.booklendingsystem.model.BorrowRecord;
import com.example.booklendingsystem.model.User;
import com.example.booklendingsystem.repository.BookRepository;
import com.example.booklendingsystem.repository.BorrowRecordRepository;
import com.example.booklendingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public BorrowRecord getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id).orElse(null);
    }

    // 新增按分类查询方法
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    @Transactional
    public BorrowRecord borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        System.out.println(book);
        if (book != null && user != null && book.isAvailable()) {
            book.setAvailable(false);
            book.setNum(book.getNum()-1);
            bookRepository.save(book);
            BorrowRecord record = borrowRecordRepository.findByBookId(bookId);
            if(record==null) {
                System.out.println("bookId"+bookId+"record is null");
                record = new BorrowRecord();
                record.setBookId(bookId);
                record.setUserId(userId);
                record.setBorrowDate(LocalDate.now());
                record.setReturnDate(null);
                record.setBookTitle(book.getTitle());
                return borrowRecordRepository.save(record);
            }
            else {
                System.out.println("bookId"+bookId+"record is not null");
                record.setBorrowDate(LocalDate.now());
                record.setReturnDate(null);
                return borrowRecordRepository.save(record);
            }
        }
        return null;
    }

    public void returnBook(Long id) {
        BorrowRecord record = borrowRecordRepository.findByBookId(id);
        if (record != null && record.getReturnDate() == null) {
            record.setReturnDate(LocalDate.now());
            Book book = bookRepository.findById(record.getBookId()).orElse(null);
            System.out.println(book.getAuthor());
            if (book != null) {
                book.setAvailable(true);
                book.setNum(book.getNum()+1);
                bookRepository.save(book);
            }
            borrowRecordRepository.save(record);
        }
    }
}
