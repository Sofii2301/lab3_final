package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.ProfesorService;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping
    public ResponseEntity<List<Profesor>> obtenerTodosLosProfesores() {
        List<Profesor> profesores = profesorService.obtenerTodosLosProfesores();
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    @GetMapping("/{idProfesor}")
    public ResponseEntity<?> obtenerProfesorPorId(@PathVariable Integer idProfesor) throws ProfesorNotFoundException {
        try {
            Profesor profesor = profesorService.obtenerProfesorPorId(idProfesor);
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("No se encontró un profesor con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesorDto) {
        Profesor nuevoProfesor = profesorService.crearProfesor(profesorDto);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    @PutMapping("/{idProfesor}")
    public ResponseEntity<?> modificarProfesor(@PathVariable int idProfesor,
            @RequestBody ProfesorDto profesorDto)
            throws ProfesorNotFoundException {
        try {
            Profesor profesorModificado = profesorService.modificarProfesor(idProfesor, profesorDto);
            return new ResponseEntity<>(profesorModificado, HttpStatus.OK);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idProfesor}")
    public ResponseEntity<?> eliminarProfesor(@PathVariable int idProfesor) throws ProfesorNotFoundException {
        try {
            profesorService.eliminarProfesor(idProfesor);
            return new ResponseEntity<>("Profesor eliminado correctamente", HttpStatus.NO_CONTENT);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/materias/{idProfesor}")
    public ResponseEntity<?> obtenerMateriasPorProfesor(@PathVariable int idProfesor)
            throws ProfesorNotFoundException {
        try {
            List<Materia> materias = profesorService.obtenerMateriasPorProfesor(idProfesor);
            return new ResponseEntity<>(materias, HttpStatus.OK);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("No se encontró un alumno con ese id", HttpStatus.NOT_FOUND);
        }
    }
}
