package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.MateriaService;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.CircularDependencyException;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<?> crearMateria(@RequestBody MateriaDto materiaDto) {
        try {
            Materia materia = materiaService.crearMateria(materiaDto);
            return new ResponseEntity<>(materia, HttpStatus.CREATED);
        } catch (MateriaAlreadyExistsException e) {
            return new ResponseEntity<>("La materia ya existe", HttpStatus.CONFLICT);
        } catch (ProfesorNotFoundException e) {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        } catch (MateriaNotFoundException e) {
            return new ResponseEntity<>("Correlativa no encontrada", HttpStatus.NOT_FOUND);
        } catch (CircularDependencyException e) {
            return new ResponseEntity<>("Dos materias no pueden ser correlativas una de otra.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{idMateria}")
    public ResponseEntity<?> obtenerMateriaPorId(@PathVariable int idMateria) {
        try {
            Materia materia = materiaService.obtenerMateriaPorId(idMateria);
            return new ResponseEntity<>(materia, HttpStatus.OK);
        } catch (MateriaNotFoundException e) {
            return new ResponseEntity<>("No se encontró una materia con ese id", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Materia>> obtenerTodasLasMaterias() {
        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @DeleteMapping("/{idMateria}")
    public ResponseEntity<?> eliminarMateria(@PathVariable int idMateria) {
        try {
            materiaService.eliminarMateria(idMateria);
            return new ResponseEntity<>("Materia eliminada correctamente", HttpStatus.NO_CONTENT);
        } catch (MateriaNotFoundException e) {
            return new ResponseEntity<>("No se encontró una materia con ese id", HttpStatus.NOT_FOUND);
        }
    }
}
