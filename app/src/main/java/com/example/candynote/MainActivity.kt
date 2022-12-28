package com.example.candynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.candynote.ui.NoteCreate.CreateNoteScreen
import com.example.candynote.ui.NoteDetail.NoteDetail
import com.example.candynote.ui.NoteEdit.NoteEditScreen
import com.example.candynote.ui.NoteList.NoteListScreen
import com.example.candynote.ui.theme.CandyNoteTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = NotesViewModelFactory(CandyNotesApp.getDao()).create(NotesViewModel::class.java)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Constants.NAVIGATION_NOTES_LIST){
                //notes list
                composable(Constants.NAVIGATION_NOTES_LIST){ NoteListScreen(
                    navController = navController,
                    viewModel = viewModel
                )}

                //notes detail
                composable(Constants.NAVIGATION_NOTES_DETAIL,
                arguments = listOf(navArgument(Constants.NAVIGATION_NOTES_ID_ARGUMENT){
                    type = NavType.IntType
                })
                ){ navBackStackEntry ->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTES_ID_ARGUMENT)?.let {
                        NoteDetail(noteId = it, navController = navController, viewModel = viewModel)
                    }
                }

                //notes edit
                composable(Constants.NAVIGATION_NOTES_EDIT,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTES_ID_ARGUMENT){
                        type = NavType.IntType
                    })
                ){ navBackStackEntry ->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTES_ID_ARGUMENT)?.let {
                        NoteEditScreen(noteId = it, navController = navController, viewModel = viewModel)
                    }
                }

                //note create
                composable(Constants.NAVIGATION_NOTES_CREATE){
                    CreateNoteScreen(navController = navController, viewModel = viewModel)
                }

            }

        }
    }
}





