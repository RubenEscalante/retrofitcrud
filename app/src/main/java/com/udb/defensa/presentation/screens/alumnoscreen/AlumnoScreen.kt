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
    alumnosViewModel.getAlums()
    val alumnos: List<AlumnoModel> by alumnosViewModel.alumnos.observeAsState(initial = emptyList())
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
                AlumnoCard(alumno, alumnosViewModel)
            }
        }
    }
}

@Composable
fun AlumnoCard(alumno: AlumnoModel, alumnosViewModel: AlumnosViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    //TODO Aca tengo un tapgestures, borrar o hacer algo
                    //TODO Cambiar iconos de la navigationbar
                    //TODO Arreglar diseno de medicinas
                    //Todo Agregar Update al crud
                    //Todo Mejorar Codigo
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
                        text = alumno.nombre, style = MaterialTheme.typography.h6,
                        color = Color(0xff312b63), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = alumno.apellido, style = MaterialTheme.typography.h6,
                        color = Color(0xffcac9d7)
                    )
                }
                Text(
                    text = "edad: ${alumno.edad}", style = MaterialTheme.typography.h6,
                    color = Color(0xffcac9d7)
                )

            }
            Icon(
                Icons.Default.Delete, contentDescription = "Delete Icon",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        alumnosViewModel.deleteAlumn(alumno._id)
                    })
                })
        }
    }
}

@Composable
fun alumnoDialog(state: Boolean, onDismiss: () -> Unit, alumnosViewModel: AlumnosViewModel) {
    var enableButton by remember { mutableStateOf(false) }
    var alumno by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    if (state) {
        Dialog(onDismissRequest = { onDismiss() }) {
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
                        text = "Agregar Medicamento",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = alumno,
                        onValueChange = { alumno = it }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Nombre") }
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Apellido") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = edad,
                        onValueChange = { edad = it }, modifier = Modifier.padding(8.dp),
                        label = { androidx.compose.material.Text("Edad") }
                    )


                    Row {
                        OutlinedButton(
                            onClick = { onDismiss() },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            androidx.compose.material.Text(text = "Cancelar")
                        }

                        if (apellido != "") enableButton = true
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
                    }
                }
            }
        }
    }
}