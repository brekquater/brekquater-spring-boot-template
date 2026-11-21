// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * A small notes API that follows every rule the Brekquater API starter checks. Copy its shape for your own APIs.
 *
 * <p>The error responses are documented here, next to the operations they belong to; {@code ApiErrors} produces them.
 */
@RestController
@RequestMapping("/notes")
@ApiResponse(responseCode = "400", description = "The request is not valid", content = @Content(
        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
@ApiResponse(responseCode = "404", description = "The note does not exist", content = @Content(
        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
class NoteController {

    private static final int MAXIMUM_PAGE_SIZE = 100;
    private static final int MAXIMUM_TITLE_LENGTH = 100;
    private static final int MAXIMUM_CONTENT_LENGTH = 2000;

    private final NoteStore notes;

    NoteController(NoteStore notes) {
        this.notes = notes;
    }

    @Operation(summary = "List notes, one page at a time")
    @GetMapping
    List<Note> listNotes(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAXIMUM_PAGE_SIZE) int size) {
        return notes.page(page, size);
    }

    @Operation(summary = "Find one note by its number")
    @GetMapping("/{id}")
    Note findNote(@PathVariable long id) {
        return notes.get(id);
    }

    @Operation(summary = "Add a note")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Note addNote(@Valid @RequestBody NewNote newNote) {
        return notes.add(newNote.title(), newNote.content());
    }

    @Operation(summary = "Delete a note")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteNote(@PathVariable long id) {
        notes.remove(id);
    }

    /**
     * What a client sends to add a note.
     *
     * @param title a short heading
     * @param content the text of the note
     */
    record NewNote(
            @NotBlank @Size(max = MAXIMUM_TITLE_LENGTH) String title,
            @NotBlank @Size(max = MAXIMUM_CONTENT_LENGTH) String content) {}
}
