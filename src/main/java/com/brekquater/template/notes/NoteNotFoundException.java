// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

/** No note has the requested number. */
public class NoteNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoteNotFoundException(long id) {
        super("Note " + id + " does not exist.");
    }
}
