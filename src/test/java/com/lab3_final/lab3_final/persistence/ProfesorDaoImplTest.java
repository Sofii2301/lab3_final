package com.lab3_final.lab3_final.persistence;

import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import com.lab3_final.lab3_final.persistence.implementation.ProfesorDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorDaoImplTest {

    private ProfesorDaoImpl profesorDao;

    @BeforeEach
    void setUp() {
        profesorDao = new ProfesorDaoImpl();
    }

    @Test
    void testGuardarProfesor() {
        Profesor profesor = new Profesor("Juan", "Pérez", "Ingeniero");

        Profesor profesorGuardado = profesorDao.saveProfesor(profesor);

        assertNotNull(profesorGuardado);
        assertEquals("Juan", profesorGuardado.getNombre());
        assertEquals("Pérez", profesorGuardado.getApellido());
        assertEquals(1, profesorGuardado.getIdProfesor());
    }

    @Test
    void testEncontrarProfesorPorId() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Carlos", "Fernández", "Licenciado");

        profesorDao.saveProfesor(profesor);
        Profesor profesorEncontrado = profesorDao.findProfesorById(1);

        assertNotNull(profesorEncontrado);
        assertEquals("Carlos", profesorEncontrado.getNombre());
        assertEquals("Fernández", profesorEncontrado.getApellido());
    }

    @Test
    void testProfesorNoEncontradoPorId() {
        assertThrows(ProfesorNotFoundException.class, () -> profesorDao.findProfesorById(999));
    }

    @Test
    void testActualizarProfesor() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Ana", "López", "Licenciada");

        profesorDao.saveProfesor(profesor);
        profesor.setApellido("Martínez");
        Profesor profesorActualizado = profesorDao.updateProfesor(profesor);

        assertNotNull(profesorActualizado);
        assertEquals("Martínez", profesorActualizado.getApellido());
    }

    @Test
    void testActualizarProfesorNoExistente() {
        Profesor profesor = new Profesor("Pedro", "Ramírez", "Doctor");
        profesor.setIdProfesor(999);

        assertThrows(ProfesorNotFoundException.class, () -> profesorDao.updateProfesor(profesor));
    }

    @Test
    void testEliminarProfesorPorId() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Laura", "García", "Arquitecta");

        profesorDao.saveProfesor(profesor);
        profesorDao.deleteProfesorById(1);

        assertThrows(ProfesorNotFoundException.class, () -> profesorDao.findProfesorById(1));
    }

    @Test
    void testEliminarProfesorNoExistente() {
        assertThrows(ProfesorNotFoundException.class, () -> profesorDao.deleteProfesorById(999));
    }

    @Test
    void testEncontrarTodosLosProfesores() {
        Profesor profesor1 = new Profesor("Carlos", "López", "Doctor");
        Profesor profesor2 = new Profesor("Sofía", "Martínez", "Ingeniera");

        profesorDao.saveProfesor(profesor1);
        profesorDao.saveProfesor(profesor2);

        List<Profesor> profesores = profesorDao.findAllProfesores();

        assertNotNull(profesores);
        assertEquals(2, profesores.size());
    }

    @Test
    void testExisteProfesorPorId() {
        Profesor profesor = new Profesor("Luis", "Rodríguez", "Economista");

        profesorDao.saveProfesor(profesor);

        assertTrue(profesorDao.existsById(1));
        assertFalse(profesorDao.existsById(999));
    }
}
