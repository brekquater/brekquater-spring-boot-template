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
    private static final String PROBLEM_JSON = "application/problem+json";

    @LocalServerPort
    private int port;

    @Test
    void listsTheWelcomeNote() {
        HttpResponse<String> response = http().get("/notes");

        assertThat(response.statusCode()).isEqualTo(OK);
        assertThat(response.body()).contains("Welcome");
    }

    @Test
    void addsFindsAndDeletesANote() {
        TemplateHttpClient http = http();

        HttpResponse<String> created = http.post("/notes", "{\"title\":\"Groceries\",\"content\":\"Milk\"}");
        assertThat(created.statusCode()).isEqualTo(CREATED);
        long id = TemplateHttpClient.json(created).get("id").asLong();

        assertThat(http.get("/notes/" + id).body()).contains("Groceries");
        assertThat(http.delete("/notes/" + id).statusCode()).isEqualTo(NO_CONTENT);
        assertThat(http.get("/notes/" + id).statusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void reportsAMissingNoteAsAProblemDetail() {
        HttpResponse<String> response = http().get("/notes/999999");

        assertThat(response.statusCode()).isEqualTo(NOT_FOUND);
        assertThat(contentTypeOf(response)).startsWith(PROBLEM_JSON);
        assertThat(TemplateHttpClient.json(response).get("detail").asString()).isEqualTo("Note 999999 does not exist.");
    }

    @Test
    void rejectsANoteWithABlankTitle() {
        HttpResponse<String> response = http().post("/notes", "{\"title\":\" \",\"content\":\"Milk\"}");

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST);
        assertThat(contentTypeOf(response)).startsWith(PROBLEM_JSON);
    }

    @Test
    void rejectsAPageSizeThatIsTooLarge() {
        HttpResponse<String> response = http().get("/notes?size=1000");

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST);
        assertThat(contentTypeOf(response)).startsWith(PROBLEM_JSON);
    }

    @Test
    void rejectsANoteNumberThatIsNotANumber() {
        HttpResponse<String> response = http().get("/notes/abc");

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST);
        assertThat(contentTypeOf(response)).startsWith(PROBLEM_JSON);
    }

    @Test
    void servesSwaggerUi() {
        assertThat(http().get("/swagger-ui/index.html").statusCode()).isEqualTo(OK);
    }

    private TemplateHttpClient http() {
        return new TemplateHttpClient(port);
    }

    private static String contentTypeOf(HttpResponse<String> response) {
        return response.headers().firstValue("Content-Type").orElse("");
    }
}
