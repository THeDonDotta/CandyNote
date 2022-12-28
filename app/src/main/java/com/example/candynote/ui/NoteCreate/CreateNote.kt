package com.example.candynote.ui.NoteCreate

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.candynote.Model.Note
import com.example.candynote.NotesViewModel
import com.example.candynote.R
import com.example.candynote.ui.GenericAppBar
import com.example.candynote.ui.theme.CandyNoteTheme


@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {

    val currentNote = remember {
        mutableStateOf("")
    }
    val currentTitle = remember {
        mutableStateOf("")
    }
    val saveButtonState = remember {
        mutableStateOf(false)
    }

    CandyNoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Create Note",
                        onIconClick = {
                            viewModel.createNote(
                                currentTitle.value,
                                currentNote.value
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = {
                            currentTitle.value = it
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        label = { Text(text = "Title") }
                    )

                    Spacer(modifier = Modifier.padding(12.dp))

                    TextField(
                        value = currentNote.value,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black
                        ),
                        onValueChange = {
                            currentNote.value = it
                            saveButtonState.value =
                                currentTitle.value != "" && currentNote.value != ""
                        },
                        label = { Text(text = "Body") }
                    )

                }

            }

        }
    }


}