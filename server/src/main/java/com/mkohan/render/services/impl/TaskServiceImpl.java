package com.mkohan.render.services.impl;

import com.mkohan.render.entities.Task;
import com.mkohan.render.entities.User;
import com.mkohan.render.exceptions.TaskNotFoundException;
import com.mkohan.render.repositories.TaskRepository;
import com.mkohan.render.services.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ThreadPoolTaskScheduler scheduler;

    @Value("${tasks.duration.min:60}")
    private Integer minTaskDuration;

    @Value("${tasks.duration.max:300}")
    private Integer maxTaskDuration;

    private final Random random = new Random();

    public TaskServiceImpl(TaskRepository taskRepository, ThreadPoolTaskScheduler scheduler) {
        this.taskRepository = taskRepository;
        this.scheduler = scheduler;
    }

    @Override
    public List<Task> getAllByUser(User user) {
        return taskRepository.findBySubmitter(user);
    }

    @Override
    public Task getByUserAndId(User user, long taskId) {
        return taskRepository.findById(taskId)
                .filter(task -> task.getSubmitter().equals(user))
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public void submit(User submitter) {
        final Task task = new Task(submitter);
        taskRepository.save(task);

        scheduler.schedule(() -> complete(task), generateTaskCompleteTime());
    }

    private Instant generateTaskCompleteTime() {
        int duration = random.nextInt(maxTaskDuration - minTaskDuration + 1) + maxTaskDuration;
        return Instant.now().plusSeconds(duration);
    }

    private void complete(Task task) {
        task.setCompletedAt(new Date());
        taskRepository.save(task);
    }
}
