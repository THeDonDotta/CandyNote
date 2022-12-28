package com.example.candynote.ui.NoteEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.candynote.Constants
import com.example.candynote.Model.Note
import com.example.candynote.NotesViewModel
import com.example.candynote.R
import com.example.candynote.ui.GenericAppBar
import com.example.candynote.ui.theme.CandyNoteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NoteEditScreen(
    noteId: Int,
    navController: NavController,
    viewModel: NotesViewModel
) {
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(Constants.noteDetailPlaceholder) }

    val currentNote = remember {
        mutableStateOf(note.value.note)
    }
    val currentTitle = remember {
        mutableStateOf(note.value.title)
    }
    val saveButtonState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId ?: 0) ?: Constants.noteDetailPlaceholder
            currentNote.value = note.value.note
            currentTitle.value = note.value.title
        }
    }
    CandyNoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Edit Note",
                        onIconClick = {
                            viewModel.updateNote(
                                Note(
                                    id = note.value.id,
                                    note = currentNote.value,
                                    title = currentTitle.value
                                )
                            )
                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_save),
                                contentDescription = stringResource(
                                    R.string.save_note
                                ),
                                tint = Color.Black
                            )
                        },
                        iconState = saveButtonState
                    )
                },

                ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    TextField(
                        value = currentTitle.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = {
                            currentTitle.value = it
                            if (currentTitle.value != note.value.title) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note && currentTitle.value == note.value.title) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Title")}
                    )
                    Spacer(modifier = Modifier.padding(12.dp))

                    TextField(
                        value = currentNote.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = {
                            currentNote.value = it
                            if (currentNote.value != note.value.note) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note && currentTitle.value == note.value.title) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Body")}
                    )

                }

            }

        }
    }


}