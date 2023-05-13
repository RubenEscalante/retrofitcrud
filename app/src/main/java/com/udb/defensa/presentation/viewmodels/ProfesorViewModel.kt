package com.udb.defensa.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udb.defensa.data.model.AddAlumnoModel
import com.udb.defensa.data.model.AlumnoModel
import com.udb.defensa.data.retrofit.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfesorViewModel @Inject constructor(
    private val authViewModel: AuthViewModel
) : ViewModel() {
    private val _profesores = MutableLiveData<List<AlumnoModel>>()
    val profesores: LiveData<List<AlumnoModel>> = _profesores

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> = _apellido

    private val _edad = MutableLiveData<String>()
    val edad: LiveData<String> = _edad

    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    private val _profesorId = MutableLiveData<String>()
    val profesorId: LiveData<String> = _profesorId

    fun getProfesores() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.getProfesores(
                        authViewModel.token.value!!
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        _profesores.value = response.body()!!.profesores
                    }
                }
            }
        }
    }

    fun addProfesor(nombre: String, apellido: String, edad: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.addProfesor(
                        authViewModel.token.value!!,
                        AddAlumnoModel(nombre, apellido, edad)
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getProfesores()
                        clearProfesor()
                    }
                }
            }
        }
    }

    fun deleteProfesor(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.deleteProfesor(
                        authViewModel.token.value!!,
                        id
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getProfesores()
                    }
                }
            }
        }
    }

    fun updateProfesor(nombre: String, apellido: String, edad: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.updateProfesor(
                        authViewModel.token.value!!,
                        id,
                        AddAlumnoModel(nombre, apellido, edad)
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getProfesores()
                        clearProfesor()
                    }
                }
            }
        }
    }

    fun onProfesorChange(name: String, apellido: String, edad: String) {
        _nombre.value = name
        _apellido.value = apellido
        _edad.value = edad
    }

    fun clearProfesor() {
        onProfesorChange("", "", "")
    }

    fun changeToUpdate(state: Boolean) {
        _isUpdate.value = state
        clearProfesor()
    }

    fun onprofesorUpdateId(_id: String) {
        _profesorId.value = _id
    }
}