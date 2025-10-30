package com.example.level_up_gamer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.level_up_gamer.ui.screen.CatalogoScreen
import com.example.level_up_gamer.ui.screen.LoginScreen
import com.example.level_up_gamer.ui.screen.MainScreen
import com.example.level_up_gamer.ui.screen.PerfilScreen
import com.example.level_up_gamer.ui.screen.RegistroScreen
import com.example.level_up_gamer.viewmodel.RegistroViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    object Registro : Screen("registro", "Registro", Icons.Default.AppRegistration)
    object Login : Screen("login", "Login", Icons.Default.Login)
    object Catalogo : Screen("catalogo", "Catálogo", Icons.Default.ShoppingBasket)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
}

val items = listOf(
    Screen.Inicio,
    Screen.Registro,
    Screen.Login,
    Screen.Catalogo,
    Screen.Perfil
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val usuario by usuarioViewModel.usuarioLogueado.collectAsState()

                items.forEach { screen ->
                    if (usuario == null || (screen.route != Screen.Login.route && screen.route != Screen.Registro.route)) {
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Inicio.route, Modifier.padding(innerPadding)) {
            composable(Screen.Inicio.route) { MainScreen() }
            
            composable(Screen.Registro.route) { 
                val usuario by usuarioViewModel.usuarioLogueado.collectAsState()
                if (usuario != null) {
                    LaunchedEffect(Unit) { navController.navigate(Screen.Perfil.route) }
                } else {
                    RegistroScreen(registroViewModel = registroViewModel, usuarioViewModel = usuarioViewModel)
                }
            }

            composable(Screen.Login.route) {
                val usuario by usuarioViewModel.usuarioLogueado.collectAsState()
                if (usuario != null) {
                    LaunchedEffect(Unit) { navController.navigate(Screen.Perfil.route) }
                } else {
                    LoginScreen(
                        usuarioViewModel = usuarioViewModel,
                        onLoginSuccess = {
                            navController.navigate(Screen.Perfil.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
            }

            composable(Screen.Catalogo.route) { CatalogoScreen() }
            composable(Screen.Perfil.route) { PerfilScreen(usuarioViewModel = usuarioViewModel) }
        }
    }
}
