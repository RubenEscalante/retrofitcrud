package com.udb.defensa.presentation.screens.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udb.defensa.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    val logo = painterResource(id = R.drawable.logo_black)
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onNavigateToLogin()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "API REST",
                fontSize = 50.sp
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                modifier = Modifier.size(350.dp),
                painter = logo,
                contentDescription = "Logo de la aplicación"
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Ruben Oswaldo Escalante Amaya",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}