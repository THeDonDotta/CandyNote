package com.example.candynote.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.candynote.Model.Note

@Database(version = 1, entities = [Note::class])
abstract class NotesDatabase: RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}