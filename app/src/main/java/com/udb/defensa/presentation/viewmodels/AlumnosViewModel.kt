package com.udb.defensa.presentation.viewmodels

import android.util.Log
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
class AlumnosViewModel @Inject constructor(
    private val authViewModel: AuthViewModel
) : ViewModel() {
    private val _alumnos = MutableLiveData<List<AlumnoModel>>()
    val alumnos: LiveData<List<AlumnoModel>> = _alumnos

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> = _apellido

    private val _edad = MutableLiveData<String>()
    val edad: LiveData<String> = _edad

    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    private val _alumnId = MutableLiveData<String>()
    val alumnId: LiveData<String> = _alumnId

    fun getAlums() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.getAlumnos(
                        authViewModel.token.value!!
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        _alumnos.value = response.body()!!.alumnos
                    }
                }
            }
        }
    }

    fun addAlumn(nombre: String, apellido: String, edad: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.addAlumnos(
                        authViewModel.token.value!!,
                        AddAlumnoModel(nombre, apellido, edad)
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getAlums()
                        clearAlumn()
                    }
                }
            }
        }
    }

    fun deleteAlumn(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.deleteAlumnos(
                        authViewModel.token.value!!,
                        id
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getAlums()
                    }
                }
            }
        }
    }

    fun updateAlumn(nombre: String, apellido: String, edad: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                authViewModel.token.value?.let {
                    RetrofitClient.webService.updateAlumnos(
                        authViewModel.token.value!!,
                        id,
                        AddAlumnoModel(nombre, apellido, edad)
                    )
                }
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.isSuccessful) {
                        getAlums()
                        clearAlumn()
                    }
                }
            }
        }
    }

    fun onAlumnChange(name: String, apellido: String, edad: String) {
        _nombre.value = name
        _apellido.value = apellido
        _edad.value = edad
    }

    fun clearAlumn() {
        onAlumnChange("", "", "")
    }

    fun changeToUpdate(state: Boolean) {
        _isUpdate.value = state
        clearAlumn()
    }

    fun onAlumnUpdateId(_id: String) {
        _alumnId.value = _id
    }
}