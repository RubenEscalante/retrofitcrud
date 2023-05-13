package com.udb.defensa.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.udb.defensa.data.model.LoginModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.udb.defensa.Defensa
import com.udb.defensa.data.SettingsDataStore
import com.udb.defensa.data.model.SingUpModel
import com.udb.defensa.data.model.UserModel
import com.udb.defensa.data.retrofit.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//  @SuppressLint("StaticFieldLeak") private val context: Context,
//    application: Application

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val context: Context,
   private val settingsDataStore: SettingsDataStore
) : ViewModel() {
   // private val settingsDataStore = (application as Defensa).settingsDataStore
    var actualUser: UserModel by mutableStateOf(UserModel("", "", ""))
    var msg: String by mutableStateOf("")

    private val _logout = MutableLiveData<Unit>()
    val logout: LiveData<Unit> = _logout

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        getAuthUser()
        viewModelScope.launch {
            settingsDataStore.tokeny.collect { tokenl ->
                if (tokenl == "" || tokenl == null) {
                    _logout.postValue(Unit)
                } else {
                    _token.value = tokenl
                }
            }
        }
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    fun onLoginChanged(name: String, email: String, password: String) {
        _name.value = name
        _email.value = email
        _password.value = password
        _isLoginEnable.value = enableLogin(email, password)
    }

    fun clearLogin() {
        onLoginChanged("", "", "")
    }

    fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 2

    fun getAuthUser() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStore.getToken().collect {
                val response =
                    RetrofitClient.webService.getAuthUser(it)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        actualUser = response.body()!!.usuario
                    }
                }
            }

        }
    }

    fun onLogin(email: String, password: String, navigate: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val login = LoginModel(email, password)
            val response = RetrofitClient.webService.loginAuth(login)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    settingsDataStore.setToken(response.body()!!.token)
                    getAuthUser()
                    clearLogin()
                    navigate()
                } else {
                    val duration =
                        Toast.LENGTH_SHORT // o Toast.LENGTH_LONG para mostrar el mensaje más tiempo
                    val toast =
                        Toast.makeText(context, "Usuario o Contraseña incorrectos", duration)
                    toast.show()
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            settingsDataStore.clearToken()
        }
    }

    fun singUp(name: String, email: String, password: String, navigate: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val singUp = SingUpModel(name, email, password)
            val response = RetrofitClient.webService.singUp(singUp)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val duration =
                        Toast.LENGTH_SHORT // o Toast.LENGTH_LONG para mostrar el mensaje más tiempo
                    val toast =
                        Toast.makeText(context, "Usuario creado con exito", duration)
                    toast.show()
                    clearLogin()
                    navigate()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errorResponse, JsonObject::class.java)
                        val erroresArray = jsonObject.getAsJsonArray("errores")
                        val errorMsg = if (erroresArray != null && erroresArray.size() > 0) {
                            erroresArray[0].asJsonObject.get("msg").asString
                        } else {
                            jsonObject.get("msg")?.asString
                        }
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, errorMsg, duration)
                        toast.show()
                    } catch (e: Exception) {
                        Log.e("Error", e.message.toString())
                    }
                }
            }
        }
    }
}
