package com.example.candynote

import com.example.candynote.Model.Note


object Constants {
    const val NAVIGATION_NOTES_CREATE = "noteCreate"
    const val NAVIGATION_NOTES_LIST = "noteList"
    const val NAVIGATION_NOTES_DETAIL = "noteDetail/{noteId}"
    const val NAVIGATION_NOTES_EDIT = "noteEdit/{noteId}"
    const val NAVIGATION_NOTES_ID_ARGUMENT = "noteId"
    const val TABLE_NAME = "notes"
    const val DATABASE_NAME = "notesDatabase"

    val noteDetailPlaceholder = Note(
        note = "Cannot find note details",
        id = 0,
        title = "Cannot find note details"
    )


    fun noteDetailNavigation(noteId: Int) = "noteDetail/$noteId"
    fun noteEditNavigation(noteId: Int) = "noteEdit/$noteId"
}