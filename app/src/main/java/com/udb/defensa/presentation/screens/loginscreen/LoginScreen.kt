package com.udb.defensa.presentation.screens.loginscreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udb.defensa.presentation.viewmodels.AuthViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSingUp: () -> Unit,
    onNavigateToAlumn: () -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finish()
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Body(Modifier.align(Alignment.Center), authViewModel, onNavigateToAlumn)
        Footer(Modifier.align(Alignment.BottomCenter), onNavigateToSingUp,authViewModel)
    }
}

@Composable
fun Footer(modifier: Modifier, onNavigateToSingUp: () -> Unit, authViewModel: AuthViewModel) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))
        SignUp(onNavigateToSingUp,authViewModel)
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SignUp(onNavigateToSingUp: () -> Unit, authViewModel: AuthViewModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "¿No tienes una cuenta?", fontSize = 12.sp, color = Color(0xFFB5B5B5)
        )
        Text(
            text = "Crear cuenta.",
            Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    onNavigateToSingUp()
                    authViewModel.clearLogin()
                },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111727),
        )
    }
}

@Composable
fun Body(modifier: Modifier, authViewModel: AuthViewModel, onNavigateToAlumn: () -> Unit) {
    val email: String by authViewModel.email.observeAsState(initial = "")
    val password: String by authViewModel.password.observeAsState(initial = "")
    val isLoginEnable: Boolean by authViewModel.isLoginEnable.observeAsState(initial = false)
    Column(modifier = modifier) {
        TextLogo(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        Email(email) {
            authViewModel.onLoginChanged(name = "", email = it, password = password)
        }
        Spacer(modifier = Modifier.size(4.dp))
        Password(password) {
            authViewModel.onLoginChanged(name = "", email = email, password = it)
        }
        Spacer(modifier = Modifier.size(8.dp))

        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(authViewModel, email, password, isLoginEnable, onNavigateToAlumn)
        Spacer(modifier = Modifier.size(16.dp))

        Spacer(modifier = Modifier.size(32.dp))

    }

}


@Composable
fun LoginButton(
    authViewModel: AuthViewModel,
    email: String,
    password: String,
    loginEnable: Boolean,
    onNavigateToAlumn: () -> Unit
) {
    Button(
        onClick = { authViewModel.onLogin(email, password, onNavigateToAlumn) },
        enabled = loginEnable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF111727),
            disabledContainerColor = Color(0xffeff1f5),
            contentColor = Color.White,
            disabledContentColor = Color(0xffaaafb8)
        )
    ) {
        Text(text = "Entrar")
    }

}


@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Olvide mi contraseña",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111727),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Contraseña") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFFB2B2B2),
            focusedLabelColor = Color(0xff111727),
            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val imagen = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = imagen, contentDescription = "show password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFFB2B2B2),
            focusedLabelColor = Color(0xff111727),
            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun TextLogo(modifier: Modifier) {
    Text(
        text = "Iniciar Sesión",
        modifier.padding(horizontal = 8.dp),
        fontSize = 35.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        color = Color(0xff111727),
    )
}