const express = require("express");
const router = express.Router();
const alumnoController = require("../controllers/alumnoController");
const auth = require("../middleware/auth");
const { check } = require("express-validator");

// Crea proyectos
// api/proyectos
router.post("/",auth,[check("nombre", "El nombre del alumno es obligatorio").not().isEmpty(),
check("apellido", "El apellido del alumno es obligatorio").not().isEmpty(),
check("edad", "La edad del alumno es obligatorio").not().isEmpty()
], alumnoController.crearAlumno);
// Obtener todos los proyectos
router.get("/", auth, alumnoController.obtenerAlumnos);
// Actualizar Proyectos via ID
router.put("/:id",auth,[check("nombre", "El nombre del alumno es obligatorio").not().isEmpty(),
check("apellido", "El apellido del alumno es obligatorio").not().isEmpty(),
check("edad", "La edad del alumno es obligatorio").not().isEmpty()],alumnoController.actualizarAlumno);
// Eliminar un proyecto
router.delete("/:id", auth, alumnoController.eliminarAlumno);

module.exports = router;
