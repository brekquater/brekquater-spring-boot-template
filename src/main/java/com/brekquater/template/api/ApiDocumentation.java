// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Describes the API as a whole: its name, purpose and owner. Replace the placeholder text with your own. */
@Configuration(proxyBeanMethods = false)
class ApiDocumentation {

    @Bean
    OpenAPI notesApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notes API")
                        .version("1.0")
                        .description("Adds, finds and deletes short notes. A starting point for your own API.")
                        .contact(new Contact().name("Your team").email("team@example.com")));
    }
}
