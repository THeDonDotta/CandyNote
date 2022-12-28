package com.example.candynote.ui.NoteList


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.candynote.Constants
import com.example.candynote.Model.Note
import com.example.candynote.Model.getDay
import com.example.candynote.Model.orPlaceHolderList
import com.example.candynote.NotesViewModel
import com.example.candynote.R
import com.example.candynote.ui.GenericAppBar
import com.example.candynote.ui.theme.CandyNoteTheme

@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {

    val deleteText = remember {
        mutableStateOf("")
    }
    val noteQuery = remember {
        mutableStateOf("")
    }
    val notesToDelete = remember {
        mutableStateOf(listOf<Note>())
    }
    val openDialog = remember {
        mutableStateOf(false)
    }

    val notes = viewModel.notes.observeAsState()
    val context = LocalContext.current

    CandyNoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Candy Notes",
                        onIconClick = {
                            if (notes.value?.isNotEmpty() == true) {
                                openDialog.value = true
                                deleteText.value = "Are you sure you want to delete these notes?"
                                notesToDelete.value = notes.value ?: emptyList()
                            } else {
                                Toast.makeText(context, "No notes found", Toast.LENGTH_SHORT).show()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                                contentDescription = stringResource(
                                    R.string.delete_note
                                ),
                                tint = Color.Black
                            )
                        },
                        iconState = remember {
                            mutableStateOf(true)
                        }
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = "Create Note",
                        action = { navController.navigate(Constants.NAVIGATION_NOTES_CREATE) },
                        icon = R.drawable.ic_add
                    )
                }

            ) {
                Column {
                    SearchBar(query = noteQuery)
                    NoteList(
                        notes = notes.value.orPlaceHolderList(),
                        openDialog = openDialog,
                        query = noteQuery,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete = notesToDelete
                    )
                }
                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    action = {
                             notesToDelete.value.forEach {
                                 viewModel.deleteNote(it)
                             }
                    },
                    notesToDelete = notesToDelete)

            }

        }
    }
}


@Composable
fun SearchBar(query: MutableState<String>) {
    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)) {
        TextField(
            value = query.value,
            placeholder = { Text(text = "Search...") },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                            contentDescription = "Clear Search"
                        )
                    }
                }
            }
        )

    }
}

@Composable
fun NoteList(
    notes: List<Note>,
    openDialog: MutableState<Boolean>,
    query: MutableState<String>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
) {
    var previousHeader = ""

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.background(MaterialTheme.colors.primary)
    ) {
        val queriedNotes = if (query.value.isEmpty()) {
            notes
        } else {
            notes.filter { it.note.contains(query.value) || it.title.contains(query.value) }
        }

        itemsIndexed(queriedNotes) { index, note ->
            if (note.getDay() != previousHeader) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = note.getDay().toString(), color = Color.Black)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                previousHeader = note.getDay().toString()
            }

            NoteListItem(
                note,
                openDialog,
                deleteText,
                navController,
                notesToDelete,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .combinedClickable(interactionSource = remember {
                MutableInteractionSource()
            },
                indication = rememberRipple(bounded = false),
                onClick = {
                    if (note.id != 0) {
                        navController.navigate(
                            Constants.noteDetailNavigation(
                                noteId = note.id ?: 0
                            )
                        )
                    }
                },
                onLongClick = {
                    if (note.id != 0) {
                        openDialog.value = true
                        deleteText.value = " Are you sure you want to delete this note"
                        notesToDelete.value = mutableListOf(note)
                    }
                }
            )) {
            Row {
                Column {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        maxLines = 3,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Text(
                        text = note.note,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Text(
                        text = note.dateUpdated,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun NotesFab(
    contentDescription: String,
    icon: Int,
    action: () -> Unit
) {
    return FloatingActionButton(
        onClick = { action.invoke() },
        backgroundColor = MaterialTheme.colors.primary
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = icon), contentDescription,
            tint = Color.Black
        )
    }
}

@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    notesToDelete: MutableState<List<Note>>
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = "delete Note")
            },
            text = {
                Column {
                    Text(text = text.value)
                }
            },
            buttons = {
                Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center) {
                    Column {
                        Button(modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                action.invoke()
                                openDialog.value = false
                                notesToDelete.value = mutableListOf()
                            }
                        ) {
                            Text(text = "Yes")

                        }
                        Spacer(modifier = Modifier.padding(12.dp))

                        Button(modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                openDialog.value = false
                                notesToDelete.value = mutableListOf()
                            }
                        ) {
                            Text(text = "No")

                        }
                    }
                }
            }

        )
    }

}

