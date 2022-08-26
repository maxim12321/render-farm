package com.mkohan.render;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mkohan.render.dtos.AuthenticationRequest;
import com.mkohan.render.dtos.TaskDto;

import java.util.List;
import java.util.Optional;

public class RenderFarmApi {

    private final RequestSender requestSender = new RequestSender();

    private String activeToken;

    public boolean signUp(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);

        return requestSender.sendPostRequestPlain("/sign_up", request).isPresent();
    }

    public boolean login(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);

        activeToken = requestSender.sendPostRequestPlain("/login", request)
                .orElse(null);

        return activeToken != null;
    }

    public void logout() {
        activeToken = null;
    }

    public List<TaskDto> getAllTasks() {
        return requestSender.sendGetRequest("/tasks", null, activeToken, new TypeReference<List<TaskDto>>() {
        }).orElse(List.of());
    }

    public Optional<TaskDto> getTaskById(long taskId) {
        return requestSender.sendGetRequest("/tasks/" + taskId, null, activeToken, new TypeReference<>() {
        });
    }

    public boolean submitTask() {
        return requestSender.sendPostRequestPlain("/tasks", null, activeToken)
                .isPresent();
    }
}
