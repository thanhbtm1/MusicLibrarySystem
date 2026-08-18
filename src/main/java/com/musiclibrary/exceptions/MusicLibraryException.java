package com.musiclibrary.exceptions;

public class MusicLibraryException extends RuntimeException {
    public MusicLibraryException(String message) {
        super(message);
    }
    
    public MusicLibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
