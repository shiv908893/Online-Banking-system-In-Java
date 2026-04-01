CREATE DATABASE online_bank;
USE online_bank;
CREATE TABLE bank (
ac_no INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(50),
mob BIGINT,
city VARCHAR(50),
balance INT
);
