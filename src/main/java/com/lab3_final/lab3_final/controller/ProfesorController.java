package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.ProfesorService;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorAlreadyExistsException;
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
    public ResponseEntity<?> obtenerProfesorPorId(@PathVariable Integer idProfesor) {
        try {
            Profesor profesor = profesorService.obtenerProfesorPorId(idProfesor);
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("No se encontró un profesor con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesorDto)
            throws ProfesorAlreadyExistsException {
        Profesor nuevoProfesor = profesorService.crearProfesor(profesorDto);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    @PutMapping("/{idProfesor}")
    public ResponseEntity<Profesor> modificarProfesor(@PathVariable int idProfesor,
            @RequestBody ProfesorDto profesorDto)
            throws ProfesorNotFoundException {
        Profesor profesorModificado = profesorService.modificarProfesor(idProfesor, profesorDto);
        return new ResponseEntity<>(profesorModificado, HttpStatus.OK);
    }

    @DeleteMapping("/{idProfesor}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable int idProfesor) throws ProfesorNotFoundException {
        profesorService.eliminarProfesor(idProfesor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/materias/{idProfesor}")
    public ResponseEntity<List<Materia>> obtenerMateriasPorProfesor(@PathVariable int idProfesor)
            throws ProfesorNotFoundException {
        List<Materia> materias = profesorService.obtenerMateriasPorProfesor(idProfesor);
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }
}
