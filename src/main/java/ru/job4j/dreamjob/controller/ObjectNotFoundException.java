package ru.job4j.dreamjob.controller;

public class ObjectNotFoundException extends RuntimeException {

    ObjectNotFoundException(int id) {
        super("Could not find object with id " + id);
    }
}