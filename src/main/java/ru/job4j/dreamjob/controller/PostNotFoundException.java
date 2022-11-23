package ru.job4j.dreamjob.controller;

public class PostNotFoundException extends RuntimeException {

    PostNotFoundException(int id) {
        super("Could not find post with id " + id);
    }
}