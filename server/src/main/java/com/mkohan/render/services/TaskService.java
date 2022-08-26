package com.mkohan.render.services;

import com.mkohan.render.entities.Task;
import com.mkohan.render.entities.User;

import java.util.List;

public interface TaskService {

    List<Task> getAllByUser(User user);

    Task getByUserAndId(User user, long taskId);

    void submit(User submitter);
}
