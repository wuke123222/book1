package com.example.booklendingsystem.controller;

import com.example.booklendingsystem.model.Book;
import com.example.booklendingsystem.model.BorrowRecord;
import com.example.booklendingsystem.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/borrow",method = {RequestMethod.GET, RequestMethod.POST})
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    // 恢复为PostMapping以匹配前端POST请求
    @PostMapping
    @ResponseBody
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordService.getAllBorrowRecords();
    }
    @GetMapping("/{id}")
    @ResponseBody
    public BorrowRecord getBorrowRecord(@PathVariable Long id) {
        return borrowRecordService.getBorrowRecordById(id);
    }

    @PostMapping("/borrowBook")
    @ResponseBody
    public BorrowRecord borrowBook(@RequestBody Map<String, Long> params) {
        return borrowRecordService.borrowBook(params.get("bookId"), params.get("userId"));
    }

    // 新增按分类查询接口
    @GetMapping("/books/category/{category}")
    public List<Book> getBooksByCategory(@PathVariable String category) {
        return borrowRecordService.getBooksByCategory(category);
    }

    @PostMapping("/returnBook")
    public void returnBook(@RequestParam Long id) {
        borrowRecordService.returnBook(id);
    }
}

