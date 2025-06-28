-- 查看当前外键约束名称（可选）
SHOW CREATE TABLE borrow_records;

-- 删除外键（假设名字为 borrow_records_ibfk_1 和 borrow_records_ibfk_2）
ALTER TABLE borrow_records DROP FOREIGN KEY borrow_records_ibfk_1;
ALTER TABLE borrow_records DROP FOREIGN KEY borrow_records_ibfk_2;
