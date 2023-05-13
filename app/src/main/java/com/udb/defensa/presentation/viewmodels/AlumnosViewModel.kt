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
                        Log.i("Ruben en alumnos", response.body()!!.alumnos.toString())
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
                    }
                }
            }
        }
    }

    fun deleteAlumn(id: String){
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
}