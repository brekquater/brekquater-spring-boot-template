// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * Keeps notes in memory, so the template runs without a database. Replace this class with a Spring Data repository
 * when your service needs to keep its data.
 */
@Component
class NoteStore {

    private final Map<String, Note> notes = new ConcurrentHashMap<>();
    private final AtomicLong lastNumber = new AtomicLong();

    NoteStore() {
        add("Welcome", "Open Swagger UI and click API maturity to see how this API scores.");
    }

    Note add(String title, String content) {
        Note note = new Note(String.valueOf(lastNumber.incrementAndGet()), title, content);
        notes.put(note.id(), note);
        return note;
    }

    Note get(String id) {
        Note note = notes.get(id);
        if (note == null) {
            throw new NoteNotFoundException(id);
        }
        return note;
    }

    void remove(String id) {
        if (notes.remove(id) == null) {
            throw new NoteNotFoundException(id);
        }
    }

    List<Note> page(int page, int size) {
        return notes.values().stream()
                .sorted(Comparator.comparingLong(note -> Long.parseLong(note.id())))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }
}
