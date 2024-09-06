package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.business.implementation.ProfesorServiceImpl;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceImplTest {

    @Mock
    private ProfesorDao profesorDao;

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    private Profesor profesor;
    private ProfesorDto profesorDto;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        profesor.setIdProfesor(1);
        profesor.setNombre("Juan");
        profesor.setApellido("Perez");
        profesor.setTitulo("Licenciado en Matemáticas");

        profesorDto = new ProfesorDto();
        profesorDto.setNombre("Juan");
        profesorDto.setApellido("Perez");
        profesorDto.setTitulo("Licenciado en Matemáticas");
    }

    @Test
    void testObtenerTodosLosProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        profesores.add(profesor);

        when(profesorDao.findAllProfesores()).thenReturn(profesores);

        List<Profesor> result = profesorService.obtenerTodosLosProfesores();
        assertEquals(1, result.size());
        verify(profesorDao, times(1)).findAllProfesores();
    }

    @Test
    void testObtenerProfesorPorId_ProfesorExistente() throws ProfesorNotFoundException {
        when(profesorDao.findProfesorById(1)).thenReturn(profesor);

        Profesor result = profesorService.obtenerProfesorPorId(1);
        assertNotNull(result);
        assertEquals(profesor.getIdProfesor(), result.getIdProfesor());
        verify(profesorDao, times(1)).findProfesorById(1);
    }

    @Test
    void testObtenerProfesorPorId_ProfesorNoEncontrado() throws ProfesorNotFoundException {
        when(profesorDao.findProfesorById(1)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.obtenerProfesorPorId(1));
        verify(profesorDao, times(1)).findProfesorById(1);
    }

    @Test
    void testCrearProfesor() {
        when(profesorDao.saveProfesor(any(Profesor.class))).thenReturn(profesor);

        Profesor result = profesorService.crearProfesor(profesorDto);
        assertNotNull(result);
        assertEquals(profesor.getNombre(), result.getNombre());
        verify(profesorDao, times(1)).saveProfesor(any(Profesor.class));
    }

    @Test
    void testModificarProfesor_ProfesorExistente() throws ProfesorNotFoundException {
        when(profesorDao.findProfesorById(1)).thenReturn(profesor);
        when(profesorDao.updateProfesor(any(Profesor.class))).thenReturn(profesor);

        Profesor result = profesorService.modificarProfesor(1, profesorDto);
        assertNotNull(result);
        assertEquals(profesor.getNombre(), result.getNombre());
        verify(profesorDao, times(1)).findProfesorById(1);
        verify(profesorDao, times(1)).updateProfesor(any(Profesor.class));
    }

    @Test
    void testModificarProfesor_ProfesorNoEncontrado() throws ProfesorNotFoundException {
        when(profesorDao.findProfesorById(1)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.modificarProfesor(1, profesorDto));
        verify(profesorDao, times(1)).findProfesorById(1);
    }

    @Test
    void testEliminarProfesor_ProfesorExistente() throws ProfesorNotFoundException {
        when(profesorDao.existsById(1)).thenReturn(true);
        doNothing().when(profesorDao).deleteProfesorById(1);

        profesorService.eliminarProfesor(1);
        verify(profesorDao, times(1)).existsById(1);
        verify(profesorDao, times(1)).deleteProfesorById(1);
    }

    @Test
    void testEliminarProfesor_ProfesorNoEncontrado() {
        when(profesorDao.existsById(1)).thenReturn(false);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.eliminarProfesor(1));
        verify(profesorDao, times(1)).existsById(1);
    }

    @Test
    void testObtenerMateriasPorProfesor_ProfesorExistente() throws ProfesorNotFoundException {
        List<Materia> materias = new ArrayList<>();
        materias.add(new Materia());

        profesor.setMateriasDictadas(materias);

        when(profesorDao.findProfesorById(1)).thenReturn(profesor);

        List<Materia> result = profesorService.obtenerMateriasPorProfesor(1);
        assertEquals(1, result.size());
        verify(profesorDao, times(1)).findProfesorById(1);
    }

    @Test
    void testObtenerMateriasPorProfesor_ProfesorNoEncontrado() throws ProfesorNotFoundException {
        when(profesorDao.findProfesorById(1)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.obtenerMateriasPorProfesor(1));
        verify(profesorDao, times(1)).findProfesorById(1);
    }
}
