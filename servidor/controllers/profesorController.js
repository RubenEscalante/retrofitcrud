const Profesor = require("../models/Profesor");
const { validationResult } = require("express-validator");


exports.crearProfesor = async (req, res) => {
  // Revisar si hay errores
  const errores = validationResult(req);
  if (!errores.isEmpty()) {
    return res.status(400).json({ errores: errores.array() });
  }

  try {
    // Crear un nuevo profesor
    const profesor = new Profesor(req.body);

    // Guardar el creador via JWT
    profesor.creador = req.usuario.id;
    // Guardamos el proyecto
    profesor.save();
    res.json(profesor);
  } catch (error) {
    console.log(error);
    res.status(500).send("Hubo un error");
  }
};

// Obtene todos los alumnos del usuario actual
exports.obtenerProfesores = async (req, res) => {
  try {
    const profesores = await Profesor.find().sort({
      creado: 1,
    });
    res.json({ profesores });
  } catch (error) {
    console.log(error);
    res.status(500).send("Hubo un error");
  }
};

// Actualiza un proyecto
exports.actualizarProfesor = async (req, res) => {
  // Revisar si hay errores
  const errores = validationResult(req);
  if (!errores.isEmpty()) {
    return res.status(400).json({ errores: errores.array() });
  }

  // Extraer la informacion del profesor
  const { nombre,apellido,edad } = req.body;
  const nuevoProfesor = {};

  if (nombre && apellido && edad) {
    nuevoProfesor.nombre = nombre;
    nuevoProfesor.apellido = apellido;
    nuevoProfesor.edad = edad;
  }

  try {
    // Revisar el ID
    let profesor = await Profesor.findById(req.params.id);
    // Revisar si el proyecto existe o no
    if (!profesor) {
      return res.status(404).json({ msg: "Profesor no encontrado" });
    }
    // Actualizar
    profesor = await Profesor.findByIdAndUpdate(
      { _id: req.params.id },
      { $set: nuevoProfesor },
      { new: true }
    );
    res.json({ profesor });
  } catch (error) {
    console.log(error);
    res.status(500).send("Error en el servidor");
  }
};

// Eliminar un proyecto por su ID
exports.eliminarProfesor = async (req, res) => {
  try {
    // Revisar el ID
    let profesor = await Profesor.findById(req.params.id);
    // Revisar si el proyecto existe o no
    if (!profesor) {
      return res.status(404).json({ msg: "Profesor no encontrado" });
    }
    // Verificar el creador del proyecto
    if (profesor.creador.toString() !== req.usuario.id) {
      return res.status(401).json({ msg: "No autorizado" });
    }
    // Eliminar el proyecto
    await Profesor.findOneAndRemove({ _id: req.params.id });
    res.json({ msg: "Profesor eliminado" });
  } catch (error) {
    console.log(error);
    res.status(500).send("Error en el servidor");
  }
};