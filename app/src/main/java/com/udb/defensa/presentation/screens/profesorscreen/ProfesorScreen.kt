package com.udb.defensa.presentation.screens.profesorscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.udb.defensa.presentation.viewmodels.ProfesorViewModel

@Composable
fun ProfesorScreen(profesorViewModel: ProfesorViewModel) {
    var dialogState by remember { mutableStateOf(false) }
    val profesores by profesorViewModel.profesores.observeAsState(initial = emptyList())
    profesorViewModel.getProfesores()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Profesor CRUD")
            Icon(
                Icons.Default.Add, contentDescription = "Add Icon",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        dialogState = true
                        profesorViewModel.changeToUpdate(false)
                    })
                })
            profesorDialog(
                state = dialogState,
                onDismiss = { dialogState = !dialogState },
                profesorViewModel = profesorViewModel
            )
        }

        LazyColumn {
            items(profesores, key = {it._id}) { profesor ->
                profesorCard(
                    profesor.nombre,profesor.apellido,profesor.edad, profesor._id, profesorViewModel,
                ) { dialogState = true }
            }
        }
    }
}

@Composable
fun profesorCard(
    alumno: String,
    apellido: String,
    edad: String,
    _id: String,
    profesorViewModel: ProfesorViewModel,
    state: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    state()
                    profesorViewModel.changeToUpdate(true)
                    profesorViewModel.onProfesorChange(alumno,apellido,edad)
                    profesorViewModel.onprofesorUpdateId(_id)
                })
            }
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(Modifier.width(200.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    Text(
                        text = alumno, style = MaterialTheme.typography.h6,
                        color = Color(0xff312b63), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = apellido, style = MaterialTheme.typography.h6,
                        color = Color(0xffcac9d7)
                    )
                }
                Text(
                    text = "edad: ${edad}", style = MaterialTheme.typography.h6,
                    color = Color(0xffcac9d7)
                )

            }
            Icon(
                Icons.Default.Delete, contentDescription = "Delete Icon",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        profesorViewModel.deleteProfesor(_id)
                    })
                })
        }
    }
}

@Composable
fun profesorDialog(state: Boolean, onDismiss: () -> Unit, profesorViewModel: ProfesorViewModel) {
    var enableButton by remember { mutableStateOf(false) }
    val alumno: String by profesorViewModel.nombre.observeAsState(initial = "")
    val apellido: String by profesorViewModel.apellido.observeAsState(initial = "")
    val edad: String by profesorViewModel.edad.observeAsState(initial = "")
    val alumnId: String by profesorViewModel.profesorId.observeAsState(initial = "")
    val isUpdate: Boolean by profesorViewModel.isUpdate.observeAsState(initial = false)

    if (state) {
        Dialog(onDismissRequest = {
            onDismiss()
            profesorViewModel.clearProfesor()
        }) {
            Card(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier.padding(8.dp),
                elevation = 8.dp
            ) {
                Column(
                    Modifier
                        .background(Color.White)
                ) {

                    androidx.compose.material.Text(
                        text = "Profesor",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = alumno,
                        onValueChange = {
                            profesorViewModel.onProfesorChange(
                                name = it,
                                apellido = apellido,
                                edad = edad
                            )
                        }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Nombre") }
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            profesorViewModel.onProfesorChange(
                                name = alumno,
                                apellido = it,
                                edad = edad
                            )
                        }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Apellido") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = edad,
                        onValueChange = {
                            profesorViewModel.onProfesorChange(
                                name = alumno,
                                apellido = apellido,
                                edad = it
                            )
                        }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Edad") }
                    )


                    Row {
                        OutlinedButton(
                            onClick = {
                                onDismiss()
                                profesorViewModel.clearProfesor()
                            },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            androidx.compose.material.Text(text = "Cancelar")
                        }

                        if (apellido != "") enableButton = true
                        if (!isUpdate) {
                            Button(
                                enabled = enableButton,
                                onClick = {
                                    profesorViewModel.addProfesor(
                                        nombre = alumno,
                                        apellido = apellido,
                                        edad = edad
                                    )
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) {
                                androidx.compose.material.Text(text = "Agregar")
                            }
                        } else {
                            Button(
                                enabled = enableButton,
                                onClick = {
                                    profesorViewModel.updateProfesor(
                                        id = alumnId,
                                        nombre = alumno,
                                        apellido = apellido,
                                        edad = edad
                                    )
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .weight(1F)
                            ) {
                                androidx.compose.material.Text(text = "Actualizar")
                            }
                        }
                    }
                }
            }
        }
    }
}