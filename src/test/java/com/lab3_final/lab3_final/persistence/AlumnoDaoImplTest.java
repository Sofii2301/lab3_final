package com.lab3_final.lab3_final.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.implementation.AlumnoDaoImpl;

import java.util.List;

class AlumnoDaoImplTest {

    private AlumnoDaoImpl alumnoDao;

    @BeforeEach
    void setUp() {
        alumnoDao = new AlumnoDaoImpl();
        alumnoDao.clearRepository();
    }

    @Test
    void testGuardarAlumno() throws AlumnoAlreadyExistsException {
        Alumno alumno = new Alumno();
        alumno.setDni(12345671);

        Alumno alumnoGuardado = alumnoDao.saveAlumno(alumno);

        assertNotNull(alumnoGuardado);
        assertEquals(12345671, alumnoGuardado.getDni());
    }

    @Test
    void testGuardarAlumnoYaExistente() throws AlumnoAlreadyExistsException {
        Alumno alumno = new Alumno();
        alumno.setDni(12345672);
        alumnoDao.saveAlumno(alumno);

        Alumno alumnoDuplicado = new Alumno();
        alumnoDuplicado.setDni(12345672);

        assertThrows(AlumnoAlreadyExistsException.class, () -> alumnoDao.saveAlumno(alumnoDuplicado));
    }

    @Test
    void testBuscarAlumnoPorId() throws AlumnoNotFoundException, AlumnoAlreadyExistsException {
        Alumno alumno = new Alumno();
        alumno.setDni(12345673);
        Alumno alumnoGuardado = alumnoDao.saveAlumno(alumno);

        int idAlumno = alumnoGuardado.getIdAlumno();

        Alumno alumnoEncontrado = alumnoDao.findAlumnoById(idAlumno);

        assertNotNull(alumnoEncontrado);
        assertEquals(12345673, alumnoGuardado.getDni());
    }

    @Test
    void testBuscarAlumnoNoExistente() {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findAlumnoById(999));
    }

    @Test
    void testActualizarAlumno() throws AlumnoNotFoundException, AlumnoAlreadyExistsException {
        Alumno alumno = new Alumno();
        alumno.setDni(12345674);
        Alumno alumnoGuardado = alumnoDao.saveAlumno(alumno);

        alumnoGuardado.setApellido("García");
        Alumno alumnoActualizado = alumnoDao.updateAlumno(alumnoGuardado);

        assertNotNull(alumnoActualizado);
        assertEquals("García", alumnoActualizado.getApellido());
        assertEquals(12345674, alumnoActualizado.getDni());
    }

    @Test
    void testActualizarAlumnoNoExistente() {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(999); // ID inexistente

        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.updateAlumno(alumno));
    }

    @Test
    void testEliminarAlumnoPorId() throws AlumnoNotFoundException, AlumnoAlreadyExistsException {
        Alumno alumno = new Alumno();
        alumno.setDni(12345674);
        Alumno alumnoGuardado = alumnoDao.saveAlumno(alumno);

        alumnoDao.deleteAlumnoById(alumnoGuardado.getIdAlumno());

        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findAlumnoById(alumnoGuardado.getIdAlumno()));
    }

    @Test
    void testEliminarAlumnoNoExistente() {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.deleteAlumnoById(999));
    }

    @Test
    void testEncontrarTodosLosAlumnos() throws AlumnoAlreadyExistsException {
        Alumno alumno1 = new Alumno();
        alumno1.setIdAlumno(5);
        alumno1.setDni(12345675);
        Alumno alumno2 = new Alumno();
        alumno2.setIdAlumno(6);
        alumno2.setDni(12345676);

        alumnoDao.saveAlumno(alumno1);
        alumnoDao.saveAlumno(alumno2);

        List<Alumno> alumnos = alumnoDao.findAllAlumnos();

        assertNotNull(alumnos);
        assertEquals(2, alumnos.size());
    }
}
