// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class NotesApiTest {

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NO_CONTENT = 204;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;

    @LocalServerPort
    private int port;

    @Test
    void listsTheWelcomeNote() {
        HttpResponse<String> response = new TemplateHttpClient(port).get("/notes");

        assertThat(response.statusCode()).isEqualTo(OK);
        assertThat(response.body()).contains("Welcome");
    }

    @Test
    void addsFindsAndDeletesANote() {
        TemplateHttpClient http = new TemplateHttpClient(port);

        HttpResponse<String> created = http.post("/notes", "{\"title\":\"Groceries\",\"content\":\"Milk\"}");
        assertThat(created.statusCode()).isEqualTo(CREATED);
        String id = created.body().replaceAll(".*\"id\":\"(\\d+)\".*", "$1");

        assertThat(http.get("/notes/" + id).body()).contains("Groceries");
        assertThat(http.delete("/notes/" + id).statusCode()).isEqualTo(NO_CONTENT);
        assertThat(http.get("/notes/" + id).statusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void reportsAMissingNoteAsAProblemDetail() {
        HttpResponse<String> response = new TemplateHttpClient(port).get("/notes/999999");

        assertThat(response.statusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.headers().firstValue("Content-Type")).hasValueSatisfying(
                type -> assertThat(type).startsWith("application/problem+json"));
    }

    @Test
    void rejectsAPageSizeThatIsTooLarge() {
        HttpResponse<String> response = new TemplateHttpClient(port).get("/notes?size=1000");

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void servesSwaggerUi() {
        assertThat(new TemplateHttpClient(port).get("/swagger-ui/index.html").statusCode()).isEqualTo(OK);
    }
}
