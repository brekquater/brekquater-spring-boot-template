// SPDX-License-Identifier: Apache-2.0
// Copyright Brekquater

package com.brekquater.template.notes;

/**
 * A note, as the API returns it.
 *
 * @param id the note's number
 * @param title a short heading
 * @param content the text of the note
 */
public record Note(long id, String title, String content) {}
