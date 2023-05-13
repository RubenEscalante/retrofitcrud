package com.udb.defensa.data.model

data class ErrorModel(
    val value: String,
    val msg: String,
    val param: String,
    val location: String
)