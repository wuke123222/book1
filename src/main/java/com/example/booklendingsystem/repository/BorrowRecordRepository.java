package com.example.booklendingsystem.repository;

import com.example.booklendingsystem.model.BorrowRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    BorrowRecord findByBookId(Long bookId);
}
