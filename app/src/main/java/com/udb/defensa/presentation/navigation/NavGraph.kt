package com.udb.defensa.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udb.defensa.presentation.screens.alumnoscreen.AlumnoScreen
import com.udb.defensa.presentation.screens.loginscreen.LoginScreen
import com.udb.defensa.presentation.screens.profesorscreen.ProfesorScreen
import com.udb.defensa.presentation.screens.singupscreen.SingUpScreen
import com.udb.defensa.presentation.screens.splashscreen.SplashScreen
import com.udb.defensa.presentation.viewmodels.AlumnosViewModel
import com.udb.defensa.presentation.viewmodels.AuthViewModel
import com.udb.defensa.presentation.viewmodels.ProfesorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    AlumnosViewModel: AlumnosViewModel,
    profesorViewModel: ProfesorViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val items = listOf(
        MenuScreen.Alumn,
        MenuScreen.Professor
    )
    authViewModel.logout.observe(LocalContext.current as LifecycleOwner, Observer {
        navController.navigate(Screen.Login.route)
    })
    authViewModel.token.observe(LocalContext.current as LifecycleOwner) { token ->
        if (token == null || token == "") {
            navController.navigate(Screen.Login.route)
        } else {
            navController.navigate(Screen.Alumn.route) {
                popUpTo(Screen.Login.route) {
                    inclusive = true
                }
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(
        navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen() { navController.navigate(Screen.Login.route) }
        }
        composable(Screen.Login.route) {
            LoginScreen(authViewModel,
                onNavigateToSingUp = { navController.navigate(Screen.SingUp.route) },
                onNavigateToAlumn = {
                    navController.navigate(Screen.Alumn.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.SingUp.route) {
            SingUpScreen(authViewModel,
                onNavigateToLogin = {navController.navigate(Screen.Login.route)
            })
        }
        composable(Screen.Alumn.route) {
            MyScaffold(navController = navController, scaffoldState, scope, items, authViewModel) {
                AlumnoScreen(AlumnosViewModel)
            }
        }
        composable(Screen.Professor.route) {
            MyScaffold(
                navController = navController,
                scaffoldState,
                scope,
                items,
                authViewModel
            ) {
                ProfesorScreen(profesorViewModel)
            }


        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyScaffold(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    items: List<MenuScreen>,
    authViewModel: AuthViewModel,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        drawerContent = {
            NavigationDrawer(items, authViewModel) {
                when (it) {
                    MenuScreen.Alumn -> {navController.navigate(Screen.Alumn.route)}
                    MenuScreen.Professor -> {navController.navigate(Screen.Professor.route)}
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }
    ) {
        content()
    }
}

@Composable
fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Retrofit CRUD") },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
            }
        }
    )
}

@Composable
fun NavigationDrawer(
    items: List<MenuScreen>,
    authViewModel: AuthViewModel,
    onItemClick: (MenuScreen) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = authViewModel.actualUser.nombre)
        Text(text = authViewModel.actualUser.email)
        Button(onClick = { authViewModel.logOut() }) {
            Text(text = "Cerrar Session")
        }
        Divider()
        items.forEach {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(it) }) {
                Icon(imageVector = it.icon, contentDescription = "Icono")
                Text(text = it.title)
            }
        }
    }
}