package com.example.level_up_gamer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.level_up_gamer.di.AppViewModelProvider
import com.example.level_up_gamer.ui.screen.AdminProductosScreen
import com.example.level_up_gamer.ui.screen.CartScreen
import com.example.level_up_gamer.ui.screen.CatalogoScreen
import com.example.level_up_gamer.ui.screen.EditProfileScreen
import com.example.level_up_gamer.ui.screen.LoginScreen
import com.example.level_up_gamer.ui.screen.MainScreen
import com.example.level_up_gamer.ui.screen.PerfilScreen
import com.example.level_up_gamer.ui.screen.RegistroScreen
import com.example.level_up_gamer.viewmodel.CartViewModel
import com.example.level_up_gamer.viewmodel.CatalogoViewModel
import com.example.level_up_gamer.viewmodel.RegistroViewModel
import com.example.level_up_gamer.viewmodel.UsuarioViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    object Registro : Screen("registro", "Registro", Icons.Default.AppRegistration)
    object Login : Screen("login", "Login", Icons.Default.Login)
    object Catalogo : Screen("catalogo", "Catálogo", Icons.Default.ShoppingBasket)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
    object Carrito : Screen("carrito", "Carrito", Icons.Default.ShoppingCart)
    object EditarPerfil : Screen("editar_perfil", "Editar", Icons.Default.Edit)
    object AdminProductos : Screen("admin_productos", "Admin", Icons.Default.Edit)
}

val items = listOf(
    Screen.Inicio,
    Screen.Registro,
    Screen.Login,
    Screen.Catalogo,
    Screen.Carrito,
    Screen.Perfil
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // SOLUCIÓN: Usamos el Factory para crear los ViewModels con sus dependencias.
    val usuarioViewModel: UsuarioViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val registroViewModel: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val cartViewModel: CartViewModel = viewModel()
    val catalogoViewModel: CatalogoViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val usuario by usuarioViewModel.usuarioLogueado.collectAsState()

                items.forEach { screen ->
                    val mostrarItem = if (usuario != null) {
                        // Si hay usuario, mostramos todo MENOS Login y Registro
                        screen != Screen.Login && screen != Screen.Registro
                    } else {
                        // Si NO hay usuario, ocultamos Perfil y Carrito (opcional, o dejar Carrito visible)
                        // Aquí decido mostrar Carrito siempre, pero ocultar Perfil si no hay sesión
                        screen != Screen.Perfil
                    }

                    // Simplifiqué la lógica anterior para que sea más legible y agregué el carrito
                    if (mostrarItem) {
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

            composable(Screen.Catalogo.route) {
                CatalogoScreen(catalogoViewModel = catalogoViewModel, cartViewModel = cartViewModel)
            }

            composable(Screen.Perfil.route) {
                PerfilScreen(
                    usuarioViewModel = usuarioViewModel,
                    onEditClick = { navController.navigate(Screen.EditarPerfil.route) },
                    onAdminClick = { navController.navigate(Screen.AdminProductos.route) } // Nuevo evento
                )
            }
            composable(Screen.AdminProductos.route) {
                AdminProductosScreen(
                    catalogoViewModel = catalogoViewModel, // Pasamos el MISMO ViewModel
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Pantalla de Edición (La nueva)
            composable(Screen.EditarPerfil.route) {
                EditProfileScreen(
                    usuarioViewModel = usuarioViewModel,
                    onNavigateBack = { navController.popBackStack() } // Al terminar, volvemos atrás
                )
            }

            // 3. CONFIGURAMOS LA PANTALLA DEL CARRITO
            composable(Screen.Carrito.route) {
                // Pasamos la instancia compartida para mantener los datos
                CartScreen(cartViewModel = cartViewModel)
            }
        }
    }
}
