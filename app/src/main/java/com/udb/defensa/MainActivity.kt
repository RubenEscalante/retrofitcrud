package com.udb.defensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.udb.defensa.presentation.navigation.NavGraph
import com.udb.defensa.presentation.viewmodels.AlumnosViewModel
import com.udb.defensa.presentation.viewmodels.AuthViewModel
import com.udb.defensa.presentation.viewmodels.ProfesorViewModel
import com.udb.defensa.ui.theme.DefensaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val alumnosViewModel: AlumnosViewModel by viewModels()
    private val profesorViewModel: ProfesorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        //val authViewModel = ViewModelProvider(this,MyViewModelFactory(applicationContext,application))[AuthViewModel::class.java]
//        val AlumnosViewModel = ViewModelProvider(this)[AlumnosViewModel::class.java].apply {
//            this.authViewModel = authViewModel
//        }
        super.onCreate(savedInstanceState)
        setContent {
            DefensaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(authViewModel,alumnosViewModel,profesorViewModel)
                }
            }
        }
    }
}

//class MyViewModelFactory(private val context: Context, private val application: Application) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
//            return AuthViewModel(context,application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
