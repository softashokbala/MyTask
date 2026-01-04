package com.abc.todo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abc.todo.presentation.screen.TodoScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.TodoHome.route) {
        composable(Route.TodoHome.route) {
            TodoScreen(
                modifier=modifier
            )
        }
    }

}

sealed class Route (val route: String){
    object TodoHome: Route("home")
    object TodoEdit: Route("edit")
    object TodoDelete: Route("delete")
}