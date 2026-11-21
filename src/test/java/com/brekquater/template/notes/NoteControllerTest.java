// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class NoteControllerTest {

    private final NoteController controller = new NoteController(new NoteStore());

    @Test
    void startsWithOneWelcomeNote() {
        assertThat(controller.listNotes(0, 20)).extracting(Note::title).containsExactly("Welcome");
    }

    @Test
    void numbersNotesInTheOrderTheyAreAdded() {
        Note second = controller.addNote(new NoteController.NewNote("Second", "Text"));

        assertThat(second.id()).isEqualTo("2");
        assertThat(controller.listNotes(0, 20)).extracting(Note::id).containsExactly("1", "2");
    }

    @Test
    void returnsTheRequestedPageOnly() {
        controller.addNote(new NoteController.NewNote("Second", "Text"));

        assertThat(controller.listNotes(1, 1)).extracting(Note::title).containsExactly("Second");
    }

    @Test
    void refusesAnEmptyOrOversizedPage() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> controller.listNotes(0, 0));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> controller.listNotes(-1, 10));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> controller.listNotes(0, 101));
    }

    @Test
    void findsAndDeletesANote() {
        controller.deleteNote("1");

        assertThatExceptionOfType(NoteNotFoundException.class)
                .isThrownBy(() -> controller.findNote("1"))
                .withMessage("Note 1 does not exist.");
    }

    @Test
    void refusesToDeleteANoteThatDoesNotExist() {
        assertThatExceptionOfType(NoteNotFoundException.class).isThrownBy(() -> controller.deleteNote("42"));
    }
}
