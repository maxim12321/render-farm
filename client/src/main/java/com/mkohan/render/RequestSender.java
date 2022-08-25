package com.mkohan.render;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkohan.render.dtos.AuthenticationRequest;
import com.mkohan.render.dtos.ErrorDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class RequestSender {

    private static final URI HOST_URI = URI.create("http://localhost:8080");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void signUp(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);
        sendPostRequestPlain("/sign_up", request);
    }

    public Optional<String> login(String username, String password) {
        final AuthenticationRequest request = new AuthenticationRequest(username, password);
        return sendPostRequestPlain("/login", request);
    }

    private Optional<String> sendPostRequestPlain(String endpoint, Object requestBody) {
        return sendPostRequestPlain(endpoint, requestBody, null);
    }

    private Optional<String> sendPostRequestPlain(String endpoint, Object requestBody, String token) {
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(HOST_URI.resolve(endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .header("Content-Type", "application/json");

            if (token != null) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            final HttpResponse<String> response = httpClient.send(
                    requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() > 299) {
                String error = objectMapper.readValue(response.body(), ErrorDto.class).getMessage();
                System.out.println(response.statusCode() + ": " + error);
                return Optional.empty();
            }

            return Optional.of(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private <T> Optional<T> sendPostRequest(String endpoint, Object requestBody, TypeReference<T> type) {
        return sendPostRequest(endpoint, requestBody, type, null);
    }

    private <T> Optional<T> sendPostRequest(String endpoint, Object requestBody, TypeReference<T> type, String token) {
        final Optional<String> response = sendPostRequestPlain(endpoint, requestBody, token);
        if (response.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(response.get(), type));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
