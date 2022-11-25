package ru.job4j.dreamjob.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(int id) {
        super("Could not find object with id " + id);
    }
}