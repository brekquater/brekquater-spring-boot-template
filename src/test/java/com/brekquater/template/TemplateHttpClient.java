// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/** Calls the running application over real HTTP, the way a browser or Swagger UI would. */
final class TemplateHttpClient {

    private static final JsonMapper JSON = JsonMapper.builder().build();

    private final HttpClient client = HttpClient.newHttpClient();
    private final int port;

    TemplateHttpClient(int port) {
        this.port = port;
    }

    HttpResponse<String> get(String path) {
        return send(HttpRequest.newBuilder(uri(path)).build());
    }

    HttpResponse<String> post(String path, String jsonBody) {
        return send(HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build());
    }

    HttpResponse<String> delete(String path) {
        return send(HttpRequest.newBuilder(uri(path)).DELETE().build());
    }

    static JsonNode json(HttpResponse<String> response) {
        return JSON.readTree(response.body());
    }

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while calling " + request.uri(), exception);
        }
    }
}
