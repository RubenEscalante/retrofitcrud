package com.udb.defensa.presentation.screens.alumnoscreen

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
import com.udb.defensa.presentation.viewmodels.AlumnosViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.udb.defensa.data.model.AlumnoModel

@Composable
fun AlumnoScreen(alumnosViewModel: AlumnosViewModel) {
    var dialogState by remember { mutableStateOf(false) }
    val alumnos by alumnosViewModel.alumnos.observeAsState(initial = emptyList())
    alumnosViewModel.getAlums()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Alumno CRUD")
            Icon(
                Icons.Default.Add, contentDescription = "Add Icon",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        dialogState = true
                        alumnosViewModel.changeToUpdate(false)
                    })
                })
            alumnoDialog(
                state = dialogState,
                onDismiss = { dialogState = !dialogState },
                alumnosViewModel = alumnosViewModel
            )
        }

        LazyColumn {
            items(alumnos) { alumno ->
                AlumnoCard(
                    alumno.nombre,alumno.apellido,alumno.edad, alumno._id, alumnosViewModel, state = { dialogState = true },
                )
            }
        }
    }
}

@Composable
fun AlumnoCard(
    alumno: String,
    apellido: String,
    edad: String,
    _id: String,
    alumnosViewModel: AlumnosViewModel,
    state: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    state()
                    alumnosViewModel.changeToUpdate(true)
                    alumnosViewModel.onAlumnChange(alumno,apellido,edad)
                    alumnosViewModel.onAlumnUpdateId(_id)
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
                        alumnosViewModel.deleteAlumn(_id)
                    })
                })
        }
    }
}

@Composable
fun alumnoDialog(state: Boolean, onDismiss: () -> Unit, alumnosViewModel: AlumnosViewModel) {
    var enableButton by remember { mutableStateOf(false) }
    val alumno: String by alumnosViewModel.nombre.observeAsState(initial = "")
    val apellido: String by alumnosViewModel.apellido.observeAsState(initial = "")
    val edad: String by alumnosViewModel.edad.observeAsState(initial = "")
    val alumnId: String by alumnosViewModel.alumnId.observeAsState(initial = "")
    val isUpdate: Boolean by alumnosViewModel.isUpdate.observeAsState(initial = false)

    if (state) {
        Dialog(onDismissRequest = {
            onDismiss()
            alumnosViewModel.clearAlumn()
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
                        text = "Alumno",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = alumno,
                        onValueChange = {
                            alumnosViewModel.onAlumnChange(
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
                            alumnosViewModel.onAlumnChange(
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
                            alumnosViewModel.onAlumnChange(
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
                                alumnosViewModel.clearAlumn()
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
                                    alumnosViewModel.addAlumn(
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
                                    alumnosViewModel.updateAlumn(
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