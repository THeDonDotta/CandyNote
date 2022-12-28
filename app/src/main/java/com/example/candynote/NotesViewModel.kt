package com.example.candynote

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.candynote.Model.Note
import com.example.candynote.persistence.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(
    private val db: NotesDao
): ViewModel() {

    val  notes : LiveData<List<Note>> = db.getNotes()

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            db.updateNote(note)
        }
    }

    fun createNote(title: String, note: String) {
        val note = Note(title = title, note = note)
        viewModelScope.launch(Dispatchers.IO) {
            db.insertNote(note)
        }
    }

   suspend fun getNote(id: Int): Note?{
        return db.getNoteById(id)
    }

}

class NotesViewModelFactory(
    private val db: NotesDao
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(
            db = db
        ) as T
    }
}