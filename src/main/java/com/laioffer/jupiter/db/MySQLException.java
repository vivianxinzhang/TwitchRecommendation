package com.laioffer.jupiter.db;

// 这里就是一个普通的 RuntimeException
public class MySQLException extends RuntimeException {
    public MySQLException(String errorMessage) {
        // call parent constructor
        super(errorMessage);
    }
}
