package com.esufam.megami.storage.exceptions;

public class FileNotFoundInDatabaseException extends RuntimeException {
    public FileNotFoundInDatabaseException(String filename) {
        super("Could not find file in database " + filename);
    }
}
