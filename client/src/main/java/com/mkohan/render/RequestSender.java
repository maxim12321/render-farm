package com.mkohan.render;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public <T> Optional<T> sendGetRequest(String endpoint, Object requestBody, String token, TypeReference<T> type) {
        return sendRequest(endpoint, false, requestBody, token, type);
    }

    public Optional<String> sendPostRequestPlain(String endpoint, Object requestBody) {
        return sendPostRequestPlain(endpoint, requestBody, null);
    }

    public Optional<String> sendPostRequestPlain(String endpoint, Object requestBody, String token) {
        return sendRequestPlain(endpoint, true, requestBody, token);
    }

    private Optional<String> sendRequestPlain(String endpoint, boolean isPost, Object requestBody, String token) {
        try {
            final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(HOST_URI.resolve(endpoint))
                    .header("Content-Type", "application/json");

            if (isPost) {
                String requestBodyJson = requestBody == null ? "" : objectMapper.writeValueAsString(requestBody);
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
            }

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

    private <T> Optional<T> sendRequest(String endpoint, boolean isPost, Object requestBody, String token, TypeReference<T> type) {
        final Optional<String> response = sendRequestPlain(endpoint, isPost, requestBody, token);
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
