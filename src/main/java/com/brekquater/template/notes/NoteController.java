// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** A small notes API that follows every rule the Brekquater API starter checks. Copy its shape for your own APIs. */
@RestController
@RequestMapping("/notes")
class NoteController {

    private static final int MAXIMUM_PAGE_SIZE = 100;

    private final NoteStore notes;

    NoteController(NoteStore notes) {
        this.notes = notes;
    }

    @Operation(summary = "List notes, one page at a time")
    @GetMapping
    List<Note> listNotes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        if (page < 0 || size < 1 || size > MAXIMUM_PAGE_SIZE) {
            throw new IllegalArgumentException("page must be 0 or more, and size between 1 and " + MAXIMUM_PAGE_SIZE + ".");
        }
        return notes.page(page, size);
    }

    @Operation(summary = "Find one note by its number")
    @GetMapping("/{id}")
    Note findNote(@PathVariable String id) {
        return notes.get(id);
    }

    @Operation(summary = "Add a note")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Note addNote(@RequestBody NewNote newNote) {
        return notes.add(newNote.title(), newNote.content());
    }

    @Operation(summary = "Delete a note")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteNote(@PathVariable String id) {
        notes.remove(id);
    }

    /**
     * What a client sends to add a note.
     *
     * @param title a short heading
     * @param content the text of the note
     */
    record NewNote(String title, String content) {}
}
