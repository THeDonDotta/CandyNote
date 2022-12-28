package com.example.candynote

import android.app.Application
import androidx.room.Room
import com.example.candynote.persistence.NotesDao
import com.example.candynote.persistence.NotesDatabase

class CandyNotesApp: Application() {
    private var db: NotesDatabase? = null

    init {
        instance = this
    }

    private fun getDb(): NotesDatabase {
        if(db != null){
            return db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                NotesDatabase::class.java, Constants.DATABASE_NAME
            ).build()

            return db!!
        }
    }
    companion object{
        private var instance: CandyNotesApp? = null

        fun getDao() : NotesDao {
            return instance!!.getDb().NotesDao()
        }


    }
}