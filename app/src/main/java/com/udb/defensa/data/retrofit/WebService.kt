package com.udb.defensa.data.retrofit

import com.udb.defensa.data.model.AddAlumnoModel
import com.udb.defensa.data.model.AlumnResponse
import com.udb.defensa.data.model.AlumnoModel
import com.udb.defensa.data.model.LoginModel
import com.udb.defensa.data.model.SingUpModel
import com.udb.defensa.data.model.UserAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {
    //Autenticacion
    @GET("/api/auth")
    suspend fun getAuthUser(@Header("x-auth-token") authToken: String): Response<UserAuthResponse>

    @POST("/api/auth")
    suspend fun loginAuth(
        @Body loginModel: LoginModel
    ): Response<UserAuthResponse>

    @POST("/api/usuarios")
    suspend fun singUp(
        @Body singUpModel: SingUpModel
    ): Response<UserAuthResponse>

    //Alumnos
    @GET("/api/alumnos")
    suspend fun getAlumnos(@Header("x-auth-token") authToken: String): Response<AlumnResponse>

    @POST("/api/alumnos")
    suspend fun addAlumnos(
        @Header("x-auth-token") authToken: String,
        @Body addAlumnoModel: AddAlumnoModel
    ): Response<AlumnResponse>

    @DELETE("/api/alumnos/{id}")
    suspend fun deleteAlumnos(
        @Header("x-auth-token") authToken: String,
        @Path("id") id: String
    ): Response<AlumnResponse>

    //Profesor
}