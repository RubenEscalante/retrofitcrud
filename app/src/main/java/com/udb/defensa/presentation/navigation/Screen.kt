package com.udb.defensa.presentation.navigation

import com.udb.defensa.utils.ALUMN
import com.udb.defensa.utils.LOGIN
import com.udb.defensa.utils.PROFESSOR
import com.udb.defensa.utils.SINGUP
import com.udb.defensa.utils.SPLASH

sealed class Screen(val route: String) {
    object Splash : Screen(SPLASH)
    object Login : Screen(LOGIN)
    object SingUp : Screen(SINGUP)
    object Alumn : Screen(ALUMN)
    object Professor : Screen(PROFESSOR)
}