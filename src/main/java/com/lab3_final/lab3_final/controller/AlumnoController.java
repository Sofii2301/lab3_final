package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.AlumnoService;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;

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
}
