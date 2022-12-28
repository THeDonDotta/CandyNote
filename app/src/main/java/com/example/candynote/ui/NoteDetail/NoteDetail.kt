package com.example.candynote.ui.NoteDetail


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
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
import com.example.candynote.Constants.noteDetailPlaceholder
import com.example.candynote.NotesViewModel
import com.example.candynote.R
import com.example.candynote.ui.GenericAppBar
import com.example.candynote.ui.theme.CandyNoteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun NoteDetail(noteId: Int, navController: NavController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()

    val note = remember {
        mutableStateOf(noteDetailPlaceholder)
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: noteDetailPlaceholder
        }
    }

    CandyNoteTheme {
        androidx.compose.material.Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = note.value.title,
                        onIconClick = {
                            navController.navigate(
                                Constants.noteEditNavigation(
                                    note.value.id ?: 0
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = com.example.candynote.R.drawable.ic_edit),
                                contentDescription = stringResource(
                                    R.string.edit_note
                                ),
                                tint = Color.Black
                            )
                        },
                        iconState = remember {
                            mutableStateOf(true)
                        }
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = note.value.title,
                        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = note.value.dateUpdated,
                        modifier = Modifier.padding(12.dp),
                        color = Color.Gray
                    )
                    Text(
                        text = note.value.note,
                        modifier = Modifier.padding(12.dp)
                    )

                }

            }

        }
    }

}