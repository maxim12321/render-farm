package com.mkohan.render.services;

import com.mkohan.render.entities.Task;
import com.mkohan.render.entities.User;

import java.util.List;

public interface TaskService {

    List<Task> getByUser(User user);

    void submit(User submitter);
}
