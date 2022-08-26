package com.mkohan.render;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mkohan.render.dtos.AuthenticationRequest;
import com.mkohan.render.dtos.TaskDto;

import java.util.List;

public class RenderFarmApi {

    private final RequestSender requestSender = new RequestSender();

    private String activeToken;

    public void signUp(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);

        requestSender.sendPostRequestPlain("/sign_up", request);
    }

    public void login(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);

        activeToken = requestSender.sendPostRequestPlain("/login", request)
                .orElse(null);
    }

    public void logout() {
        activeToken = null;
    }

    public List<TaskDto> getAllTasks() {
        return requestSender.sendGetRequest("/tasks", null, activeToken, new TypeReference<List<TaskDto>>() {
        }).orElse(List.of());
    }

    public void postTask() {
        requestSender.sendPostRequestPlain("/tasks", null, activeToken);
    }
}
