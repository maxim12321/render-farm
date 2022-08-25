package com.mkohan.render.controllers;

import com.mkohan.render.dtos.TaskDto;
import com.mkohan.render.entities.User;
import com.mkohan.render.services.TaskService;
import com.mkohan.render.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public List<TaskDto> getAllUserTasks(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());

        return taskService.getByUser(user).stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public void submit(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        taskService.submit(user);
    }
}
