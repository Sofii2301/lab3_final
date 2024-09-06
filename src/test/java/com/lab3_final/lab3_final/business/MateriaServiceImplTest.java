package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.business.implementation.MateriaServiceImpl;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.CircularDependencyException;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MateriaServiceImplTest {

    @Mock
    private MateriaDao materiaDao;

    @Mock
    private ProfesorDao profesorDao;

    @InjectMocks
    private MateriaServiceImpl materiaService;

    private MateriaDto materiaDto;
    private Materia materia;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        materiaDto = new MateriaDto();
        materiaDto.setMateriaId(1);
        materiaDto.setNombre("Matemática");
        materiaDto.setAnio(2023);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(1);

        List<Integer> correlativas = new ArrayList<>();
        correlativas.add(2);
        correlativas.add(3);
        materiaDto.setCorrelatividades(correlativas);

        materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("Matemática");
        materia.setAnio(2023);
        materia.setCuatrimestre(1);
        materia.setProfesorId(1);

        profesor = new Profesor();
        profesor.setIdProfesor(1);
        profesor.setNombre("Profesor 1");

        List<Materia> materiasDictadas = new ArrayList<>();
        materiasDictadas.add(materia);
        profesor.setMateriasDictadas(materiasDictadas);
    }

    @Test
    void testCrearMateria() throws MateriaAlreadyExistsException, ProfesorNotFoundException, MateriaNotFoundException,
            CircularDependencyException {
        // Simular que no existe materia con el mismo nombre, año y cuatrimestre
        when(materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor(
                anyString(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        // Simular que se encuentra al profesor
        when(profesorDao.findProfesorById(anyInt())).thenReturn(profesor);

        // Simular la búsqueda de correlativas
        when(materiaDao.findMateriaById(2)).thenReturn(new Materia(2, "Correlativa 1", 2023, 1, 1));
        when(materiaDao.findMateriaById(3)).thenReturn(new Materia(3, "Correlativa 2", 2023, 1, 1));

        // Simular el guardado de la materia
        when(materiaDao.saveMateria(any(Materia.class))).thenReturn(materia);

        // Ejecutar la creación de la materia
        Materia result = materiaService.crearMateria(materiaDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(materiaDto.getNombre(), result.getNombre());

        // Verificar interacciones
        verify(materiaDao, times(1)).saveMateria(any(Materia.class));
        verify(profesorDao, times(1)).updateProfesor(any(Profesor.class));
    }

    @Test
    void testCrearMateria_MateriaYaExiste() throws MateriaAlreadyExistsException {
        // Simular que ya existe una materia con el mismo nombre, año y cuatrimestre
        when(materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor(
                anyString(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        MateriaAlreadyExistsException exception = assertThrows(MateriaAlreadyExistsException.class, () -> {
            materiaService.crearMateria(materiaDto);
        });

        assertEquals("La materia ya existe.", exception.getMessage());

        verify(materiaDao, never()).saveMateria(any(Materia.class));
    }

    @Test
    void testCrearMateria_ProfesorNoEncontrado() throws ProfesorNotFoundException, MateriaAlreadyExistsException {
        // Simular que no se encuentra al profesor
        when(materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor(
                anyString(), anyInt(), anyInt(), anyInt())).thenReturn(false);
        when(profesorDao.findProfesorById(anyInt())).thenReturn(null);

        ProfesorNotFoundException exception = assertThrows(ProfesorNotFoundException.class, () -> {
            materiaService.crearMateria(materiaDto);
        });

        assertEquals("Profesor no encontrado.", exception.getMessage());

        verify(materiaDao, never()).saveMateria(any(Materia.class));
    }

    @Test
    void testObtenerMateriaPorId() throws MateriaNotFoundException {
        // Simular que se encuentra la materia
        when(materiaDao.findMateriaById(anyInt())).thenReturn(materia);

        Materia result = materiaService.obtenerMateriaPorId(1);

        assertNotNull(result);
        assertEquals(materia.getNombre(), result.getNombre());
    }

    @Test
    void testObtenerMateriaPorId_MateriaNoEncontrada() throws MateriaNotFoundException {
        // Simular que no se encuentra la materia
        when(materiaDao.findMateriaById(anyInt())).thenReturn(null);

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            materiaService.obtenerMateriaPorId(1);
        });

        assertEquals("Materia no encontrada.", exception.getMessage());
    }

    @Test
    void testEliminarMateria() throws MateriaNotFoundException {
        // Simular que existe la materia
        when(materiaDao.existsById(anyInt())).thenReturn(true);

        materiaService.eliminarMateria(1);

        verify(materiaDao, times(1)).deleteMateriaById(1);
    }

    @Test
    void testEliminarMateria_MateriaNoEncontrada() throws MateriaNotFoundException {
        // Simular que no existe la materia
        when(materiaDao.existsById(anyInt())).thenReturn(false);

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            materiaService.eliminarMateria(1);
        });

        assertEquals("Materia no encontrada.", exception.getMessage());

        verify(materiaDao, never()).deleteMateriaById(anyInt());
    }
}
