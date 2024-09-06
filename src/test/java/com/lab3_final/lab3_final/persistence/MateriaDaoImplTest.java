package com.lab3_final.lab3_final.persistence;

import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.implementation.MateriaDaoImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MateriaDaoImplTest {

    private MateriaDaoImpl materiaDao;

    @BeforeEach
    void setUp() {
        materiaDao = new MateriaDaoImpl();
    }

    @Test
    void testGuardarMateria() throws MateriaAlreadyExistsException {
        Materia materia = new Materia();
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setAnio(1);
        materia.setProfesorId(1);

        Materia savedMateria = materiaDao.saveMateria(materia);

        assertNotNull(savedMateria.getMateriaId());
        assertEquals(materia.getNombre(), savedMateria.getNombre());
        assertEquals(1, materiaDao.findAllMaterias().size());
    }

    @Test
    void testGuardarMateriaYaExistente() throws MateriaAlreadyExistsException {
        Materia materia1 = new Materia();
        materia1.setNombre("Matemática");
        materia1.setAnio(2023);
        materia1.setAnio(1);
        materia1.setProfesorId(1);
        Materia materia2 = new Materia();
        materia2.setNombre("Matemática");
        materia2.setAnio(2023);
        materia2.setAnio(1);
        materia2.setProfesorId(1);

        materiaDao.saveMateria(materia1);

        assertThrows(MateriaAlreadyExistsException.class, () -> {
            materiaDao.saveMateria(materia2);
        });
    }

    @Test
    void testBuscarMateriaPorId() throws MateriaNotFoundException, MateriaAlreadyExistsException {
        Materia materia = new Materia();
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setAnio(1);
        materia.setProfesorId(1);
        materiaDao.saveMateria(materia);

        Materia foundMateria = materiaDao.findMateriaById(materia.getMateriaId());

        assertNotNull(foundMateria);
        assertEquals(materia.getNombre(), foundMateria.getNombre());
    }

    @Test
    void testBuscarMateriaNoExistente() {
        assertThrows(MateriaNotFoundException.class, () -> {
            materiaDao.findMateriaById(999);
        });
    }

    @Test
    void testFindAllMaterias() throws MateriaAlreadyExistsException {
        Materia materia1 = new Materia();
        materia1.setNombre("Matemática");
        materia1.setAnio(2023);
        materia1.setAnio(1);
        materia1.setProfesorId(1);
        Materia materia2 = new Materia();
        materia2.setNombre("Historia");
        materia2.setAnio(2023);
        materia2.setAnio(2);
        materia2.setProfesorId(2);

        materiaDao.saveMateria(materia1);
        materiaDao.saveMateria(materia2);

        List<Materia> allMaterias = materiaDao.findAllMaterias();

        assertEquals(2, allMaterias.size());
    }

    @Test
    void testEliminarMateriaPorId() throws MateriaNotFoundException, MateriaAlreadyExistsException {
        Materia materia = new Materia();
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setAnio(1);
        materia.setProfesorId(1);

        materiaDao.saveMateria(materia);

        materiaDao.deleteMateriaById(materia.getMateriaId());

        assertEquals(0, materiaDao.findAllMaterias().size());
    }

    @Test
    void testEliminarMateriaPorIdNoExistente() {
        assertThrows(MateriaNotFoundException.class, () -> {
            materiaDao.deleteMateriaById(999);
        });
    }

    @Test
    void testExistsByNombreAndAnioAndCuatrimestreAndProfesor_True() throws MateriaAlreadyExistsException {
        Materia materia = new Materia();
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setCuatrimestre(1);
        materia.setProfesorId(1);
        materiaDao.saveMateria(materia);

        boolean exists = materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor("Matemática", 2023, 1, 1);

        assertTrue(exists);
    }

    @Test
    void testExistsByNombreAndAnioAndCuatrimestreAndProfesor_False() {
        boolean exists = materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor("Matemática", 2023, 1, 1);

        assertFalse(exists);
    }

    @Test
    void testExistsById_True() throws MateriaAlreadyExistsException {
        Materia materia = new Materia();
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setAnio(1);
        materia.setProfesorId(1);
        materiaDao.saveMateria(materia);

        boolean exists = materiaDao.existsById(materia.getMateriaId());

        assertTrue(exists);
    }

    @Test
    void testExistsById_False() {
        boolean exists = materiaDao.existsById(999);

        assertFalse(exists);
    }
}
