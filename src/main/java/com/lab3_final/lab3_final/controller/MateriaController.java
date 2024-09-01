package com.lab3_final.lab3_final.controller;

import com.lab3_final.lab3_final.business.MateriaService;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
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
    public ResponseEntity<Materia> crearMateria(@RequestBody MateriaDto materiaDto)
            throws MateriaAlreadyExistsException, ProfesorNotFoundException {
        Materia nuevaMateria = materiaService.crearMateria(materiaDto);
        return new ResponseEntity<>(nuevaMateria, HttpStatus.CREATED);
    }

    @GetMapping("/{idMateria}")
    public ResponseEntity<Materia> obtenerMateriaPorId(@PathVariable int idMateria)
            throws MateriaNotFoundException {
        Materia materia = materiaService.obtenerMateriaPorId(idMateria);
        return new ResponseEntity<>(materia, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Materia>> obtenerTodasLasMaterias() {
        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @DeleteMapping("/{idMateria}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable int idMateria)
            throws MateriaNotFoundException {
        materiaService.eliminarMateria(idMateria);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
