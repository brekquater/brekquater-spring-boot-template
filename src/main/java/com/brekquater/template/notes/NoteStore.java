// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * Keeps notes in memory, so the template runs without a database. Replace this class with a Spring Data repository
 * when your service needs to keep its data.
 */
@Component
class NoteStore {

    private final NavigableMap<Long, Note> notesById = new ConcurrentSkipListMap<>();
    private final AtomicLong lastId = new AtomicLong();

    NoteStore() {
        add("Welcome", "Open Swagger UI and click API maturity to see how this API scores.");
    }

    Note add(String title, String content) {
        Note note = new Note(lastId.incrementAndGet(), title, content);
        notesById.put(note.id(), note);
        return note;
    }

    Note get(long id) {
        Note note = notesById.get(id);
        if (note == null) {
            throw new NoteNotFoundException(id);
        }
        return note;
    }

    void remove(long id) {
        if (notesById.remove(id) == null) {
            throw new NoteNotFoundException(id);
        }
    }

    /** Returns the requested page of notes, oldest first. */
    List<Note> page(int pageNumber, int pageSize) {
        return notesById.values().stream().skip((long) pageNumber * pageSize).limit(pageSize).toList();
    }
}
