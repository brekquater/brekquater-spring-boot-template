// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.api;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Marks every operation as public on purpose, which is what the template does today: it has no authentication. The
 * API starter's rule BQ-SEC-001 treats an unprotected operation as a blocker unless it is marked public.
 *
 * <p>When you add Spring Security, delete this class and declare a security scheme instead; see the rule's page at
 * https://starter.brekquater.com/api/rules/BQ-SEC-001.
 */
@Component
class PublicOperations implements OperationCustomizer {

    private static final String PUBLIC_EXTENSION = "x-brekquater-public";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        operation.addExtension(PUBLIC_EXTENSION, true);
        return operation;
    }
}
