package com.heroku.java.Exceptions;

public class InvalidTokenException extends Throwable {
    public InvalidTokenException(String invalidOrExpiredToken) {
        System.out.println(invalidOrExpiredToken);
    }
}
