const express = require("express");
const router = express.Router();
const profesorController = require("../controllers/profesorController");
const auth = require("../middleware/auth");
const { check } = require("express-validator");

// Crea profesores
// api/profesores
router.post(
  "/",
  auth,
  [
    check("nombre", "El nombre del alumno es obligatorio").not().isEmpty(),
    check("apellido", "El apellido del alumno es obligatorio").not().isEmpty(),
    check("edad", "La edad del alumno es obligatorio").not().isEmpty(),
  ],
  profesorController.crearProfesor
);

// Obtener todos los proyectos
router.get("/", auth, profesorController.obtenerProfesores);

// Actualizar Proyectos via ID
router.put(
  "/:id",
  auth,
  [
    check("nombre", "El nombre del alumno es obligatorio").not().isEmpty(),
    check("apellido", "El apellido del alumno es obligatorio").not().isEmpty(),
    check("edad", "La edad del alumno es obligatorio").not().isEmpty(),
  ],
  profesorController.actualizarProfesor
);

// Eliminar un proyecto
router.delete("/:id", auth, profesorController.eliminarProfesor);

module.exports = router;
