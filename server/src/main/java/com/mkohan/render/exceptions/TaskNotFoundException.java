package com.mkohan.render.exceptions;

public class TaskNotFoundException extends EntityNotFoundException {

    public TaskNotFoundException() {
        super("Task not found");
    }
}
