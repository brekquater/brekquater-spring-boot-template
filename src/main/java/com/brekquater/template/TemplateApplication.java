// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Starts the template API. Rename the package and this class when you turn the template into your own service. */
@SpringBootApplication
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
