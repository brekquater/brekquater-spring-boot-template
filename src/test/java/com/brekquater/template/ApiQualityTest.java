// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;

/** Keeps the API's design clean: this test fails when a change adds a finding from the Brekquater API starter. */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApiQualityTest {

    @LocalServerPort
    private int port;

    @Test
    void theApiHasNoFindings() {
        JsonNode assessment = new TemplateHttpClient(port).assessment();

        assertThat(assessment.get("status").asString()).isEqualTo("ASSESSED");
        assertThat(assessment.get("findings")).isEmpty();
        assertThat(assessment.get("failedRules")).isEmpty();
    }

    @Test
    void strictModeLetsTheApplicationStart() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TemplateApplication.class)
                .properties("server.port=0", "brekquater.api.strict=true")
                .run()) {
            assertThat(context.isActive()).isTrue();
        }
    }
}
