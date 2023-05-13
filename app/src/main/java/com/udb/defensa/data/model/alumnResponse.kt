package com.udb.defensa.data.model

data class AlumnResponse(
    val msg: String,
    val alumnos: List<AlumnoModel>,
    val token: String,
    val alumno: AlumnoModel,
    val errores: ErrorModel
)