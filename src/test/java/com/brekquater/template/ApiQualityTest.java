// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;

/**
 * Keeps the API's design clean. Strict mode makes the application refuse to start while a blocker finding exists, and
 * the test fails when a change adds any finding from the Brekquater API starter.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "brekquater.api.strict=true")
class ApiQualityTest {

    @LocalServerPort
    private int port;

    @Test
    void theApiHasNoFindings() {
        JsonNode assessment = TemplateHttpClient.json(new TemplateHttpClient(port).get("/brekquater/api/assessment"));

        assertThat(assessment.get("status").asString()).isEqualTo("ASSESSED");
        assertThat(assessment.get("findings")).isEmpty();
        assertThat(assessment.get("failedRules")).isEmpty();
    }
}
