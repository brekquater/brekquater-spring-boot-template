// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.api;

import com.brekquater.template.notes.NoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Turns every failure into an RFC 9457 problem detail ({@code application/problem+json}). The base class does it for
 * Spring's own errors, such as an invalid request body or a malformed number in a path; the handler below does it for
 * the template's own exception.
 */
@RestControllerAdvice
class ApiErrors extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    ProblemDetail noteNotFound(NoteNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
