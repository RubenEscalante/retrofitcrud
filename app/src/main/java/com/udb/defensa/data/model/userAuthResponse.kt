package com.udb.defensa.data.model

data class UserAuthResponse(
    val msg: String,
    val token: String,
    val usuario: UserModel,
    val errores: ErrorModel
)