package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.AlumnoService;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.dto.AsignaturaDto;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.model.Asignatura;
import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.NotaInvalidaException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    public ResponseEntity<List<Alumno>> obtenerTodosLosAlumnos() {
        List<Alumno> alumnos = alumnoService.obtenerTodosLosAlumnos();
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/{idAlumno}")
    public ResponseEntity<?> obtenerAlumnoPorId(@PathVariable Integer idAlumno) {
        try {
            Alumno alumno = alumnoService.obtenerAlumnoPorId(idAlumno);
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } catch (AlumnoNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        try {
            Alumno alumno = alumnoService.crearAlumno(alumnoDto);
            return new ResponseEntity<>(alumno, HttpStatus.CREATED);
        } catch (AlumnoAlreadyExistsException e) {
            return new ResponseEntity<>("El alumno con ese id ya existe en la base de datos", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{idAlumno}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable Integer idAlumno, @RequestBody AlumnoDto alumnoDto) {
        try {
            Alumno alumno = alumnoService.actualizarAlumno(alumnoDto, idAlumno);
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } catch (AlumnoNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idAlumno}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable Integer idAlumno) {
        try {
            alumnoService.eliminarAlumno(idAlumno);
            return new ResponseEntity<>("Alumno eliminado correctamente", HttpStatus.NO_CONTENT);
        } catch (AlumnoNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{idAlumno}/asignatura")
    public ResponseEntity<?> agregarAsignatura(
            @PathVariable int idAlumno,
            @RequestBody AsignaturaDto asignaturaDto)
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        try {
            Asignatura asignatura = alumnoService.agregarAsignatura(idAlumno, asignaturaDto);
            return new ResponseEntity<>(asignatura, HttpStatus.CREATED);
        } catch (AlumnoNotFoundException e) {
            return new ResponseEntity<>("Alumno no encontrado con id: " + idAlumno + ": " + e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (MateriaNotFoundException e) {
            return new ResponseEntity<>(
                    "Materia no encontrada con id " + asignaturaDto.getMateriaId() + ": " + e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (AsignaturaAlreadyExistsException e) {
            return new ResponseEntity<>("La asignatura para esta materia ya existe: " + e.getMessage(),
                    HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public ResponseEntity<?> modificarEstadoAsignatura(
            @PathVariable int idAlumno,
            @PathVariable int idAsignatura,
            @RequestBody AsignaturaDto asignaturaDto)
            throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException,
            NotaInvalidaException {
        try {
            Asignatura asignatura = alumnoService.modificarEstadoAsignatura(idAlumno, idAsignatura,
                    asignaturaDto.getEstado(), asignaturaDto.getNota());
            return new ResponseEntity<>(asignatura, HttpStatus.OK);
        } catch (AlumnoNotFoundException e) {
            return new ResponseEntity<>("No se encontró el alumno con ID: " + idAlumno, HttpStatus.NOT_FOUND);
        } catch (AsignaturaNotFoundException e) {
            return new ResponseEntity<>(
                    "No se encontró la asignatura con ID: " + idAsignatura + " para el alumno con ID: " + idAlumno,
                    HttpStatus.NOT_FOUND);
        } catch (EstadoIncorrectoException e) {
            return new ResponseEntity<>("El estado de la asignatura es incorrecto: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (NotaInvalidaException e) {
            return new ResponseEntity<>("La nota ingresada no es valida: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Manejo de errores inesperados
            return new ResponseEntity<>("Ocurrió un error inesperado al modificar la asignatura: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
