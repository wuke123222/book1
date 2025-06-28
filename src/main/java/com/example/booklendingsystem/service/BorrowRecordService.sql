
ALTER TABLE book_ratings
    ADD CONSTRAINT unique_user_book UNIQUE (user_id, book_id);
