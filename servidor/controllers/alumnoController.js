const Alumno = require("../models/Alumno");
const { validationResult } = require("express-validator");

exports.crearAlumno = async (req, res) => {
  // Revisar si hay errores
  const errores = validationResult(req);
  if (!errores.isEmpty()) {
    return res.status(400).json({ errores: errores.array() });
  }

  try {
    // Crear un nuevo proyecto
    const alumno = new Alumno(req.body);

    // Guardar el creador via JWT
    alumno.creador = req.usuario.id;
    // Guardamos el proyecto
    alumno.save();
    res.json(alumno);
  } catch (error) {
    console.log(error);
    res.status(500).send("Hubo un error");
  }
};

// Obtene todos los alumnos del usuario actual
exports.obtenerAlumnos = async (req, res) => {
  try {
    const alumnos = await Alumno.find().sort({
      creado: 1,
    });
    res.json({ alumnos });
  } catch (error) {
    console.log(error);
    res.status(500).send("Hubo un error");
  }
};

// Actualiza un proyecto
exports.actualizarAlumno = async (req, res) => {
  // Revisar si hay errores
  const errores = validationResult(req);
  if (!errores.isEmpty()) {
    return res.status(400).json({ errores: errores.array() });
  }

  // Extraer la informacion del proyecto
  const { nombre,apellido,edad } = req.body;
  const nuevoAlumno = {};

  if (nombre && apellido && edad) {
    nuevoAlumno.nombre = nombre;
    nuevoAlumno.apellido = apellido;
    nuevoAlumno.edad = edad;
  }

  try {
    // Revisar el ID
    let alumno = await Alumno.findById(req.params.id);
    // Revisar si el proyecto existe o no
    if (!alumno) {
      return res.status(404).json({ msg: "Alumno no encontrado" });
    }
    // Actualizar
    alumno = await Alumno.findByIdAndUpdate(
      { _id: req.params.id },
      { $set: nuevoAlumno },
      { new: true }
    );
    res.json({ alumno });
  } catch (error) {
    console.log(error);
    res.status(500).send("Error en el servidor");
  }
};

// Eliminar un proyecto por su ID
exports.eliminarAlumno = async (req, res) => {
  try {
    // Revisar el ID
    let alumno = await Alumno.findById(req.params.id);
    // Revisar si el proyecto existe o no
    if (!alumno) {
      return res.status(404).json({ msg: "Proyecto no encontrado" });
    }
    // Verificar el creador del proyecto
    if (alumno.creador.toString() !== req.usuario.id) {
      return res.status(401).json({ msg: "No autorizado" });
    }
    // Eliminar el proyecto
    await Alumno.findOneAndRemove({ _id: req.params.id });
    res.json({ msg: "Alumno eliminado" });
  } catch (error) {
    console.log(error);
    res.status(500).send("Error en el servidor");
  }
};
