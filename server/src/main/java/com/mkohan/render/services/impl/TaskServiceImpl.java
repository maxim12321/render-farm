package com.mkohan.render.services.impl;

import com.mkohan.render.entities.Task;
import com.mkohan.render.entities.User;
import com.mkohan.render.repositories.TaskRepository;
import com.mkohan.render.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getByUser(User user) {
        return taskRepository.findBySubmitter(user);
    }

    @Override
    public void submit(User submitter) {
        final Task task = new Task(submitter);
        taskRepository.save(task);
    }
}
