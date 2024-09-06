package com.lab3_final.lab3_final.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.lab3_final.lab3_final.business.implementation.AlumnoServiceImpl;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.dto.AsignaturaDto;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.model.Asignatura;
import com.lab3_final.lab3_final.model.EstadoAsignatura;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;
import com.lab3_final.lab3_final.persistence.AlumnoDao;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.NotaInvalidaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceImplTest {

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private MateriaDao materiaDao;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    private Alumno alumno;
    private Materia materia;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        alumno = new Alumno(1, "Juan", "Perez", 12345678);
        materia = new Materia(1, "Matemáticas", 1, 1, 101);
        asignatura = new Asignatura(1, materia, EstadoAsignatura.NO_CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignatura)));
    }

    @Test
    void testObtenerTodosLosAlumnos() {
        List<Alumno> alumnos = Arrays.asList(alumno);
        when(alumnoDao.findAllAlumnos()).thenReturn(alumnos);

        List<Alumno> resultado = alumnoService.obtenerTodosLosAlumnos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(alumno, resultado.get(0));
        verify(alumnoDao, times(1)).findAllAlumnos();
    }

    @Test
    void testObtenerAlumnoPorIdExistente() throws AlumnoNotFoundException {
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        Alumno resultado = alumnoService.obtenerAlumnoPorId(1);

        assertNotNull(resultado);
        assertEquals(alumno, resultado);
        verify(alumnoDao, times(1)).findAlumnoById(1);
    }

    @Test
    void testObtenerAlumnoPorIdNoExistente() throws AlumnoNotFoundException {
        when(alumnoDao.findAlumnoById(1)).thenReturn(null);

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.obtenerAlumnoPorId(1);
        });

        assertEquals("Alumno no encontrado con id: 1", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
    }

    @Test
    void testCrearAlumnoExitoso() throws AlumnoAlreadyExistsException {
        AlumnoDto alumnoDto = new AlumnoDto(2, "Maria", "Gomez", 87654321);
        Alumno nuevoAlumno = new Alumno(2, "Maria", "Gomez", 87654321);
        when(alumnoDao.saveAlumno(any(Alumno.class))).thenReturn(nuevoAlumno);

        Alumno resultado = alumnoService.crearAlumno(alumnoDto);

        assertNotNull(resultado);
        assertEquals(nuevoAlumno, resultado);
        verify(alumnoDao, times(1)).saveAlumno(any(Alumno.class));
    }

    @Test
    void testCrearAlumnoYaExiste() throws AlumnoAlreadyExistsException {
        AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Perez", 12345678);
        when(alumnoDao.saveAlumno(any(Alumno.class)))
                .thenThrow(new AlumnoAlreadyExistsException("El alumno con ese DNI ya existe"));

        AlumnoAlreadyExistsException exception = assertThrows(AlumnoAlreadyExistsException.class, () -> {
            alumnoService.crearAlumno(alumnoDto);
        });

        assertEquals("El alumno con ese DNI ya existe", exception.getMessage());
        verify(alumnoDao, times(1)).saveAlumno(any(Alumno.class));
    }

    @Test
    void testActualizarAlumnoExitoso() throws AlumnoNotFoundException {
        AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Lopez", 12345678);
        Alumno alumnoActualizado = new Alumno(1, "Juan", "Lopez", 12345678);
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);
        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumnoActualizado);

        Alumno resultado = alumnoService.actualizarAlumno(alumnoDto, 1);

        assertNotNull(resultado);
        assertEquals("Lopez", resultado.getApellido());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testActualizarAlumnoNoExiste() throws AlumnoNotFoundException {
        AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Lopez", 12345678);
        when(alumnoDao.findAlumnoById(1)).thenReturn(null);

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.actualizarAlumno(alumnoDto, 1);
        });

        assertEquals("Alumno no encontrado con id: 1", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testEliminarAlumnoExitoso() throws AlumnoNotFoundException {
        doNothing().when(alumnoDao).deleteAlumnoById(1);

        assertDoesNotThrow(() -> alumnoService.eliminarAlumno(1));

        verify(alumnoDao, times(1)).deleteAlumnoById(1);
    }

    @Test
    void testEliminarAlumnoNoExiste() throws AlumnoNotFoundException {
        doThrow(new AlumnoNotFoundException("Alumno no encontrado")).when(alumnoDao).deleteAlumnoById(1);

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.eliminarAlumno(1);
        });

        assertEquals("Alumno no encontrado", exception.getMessage());
        verify(alumnoDao, times(1)).deleteAlumnoById(1);
    }

    @Test
    void testAgregarAsignaturaExitoso()
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(2);
        asignaturaDto.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturaDto.setNota(null);

        Materia nuevaMateria = new Materia(2, "Física", 1, 1, 102);
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);
        when(materiaDao.findMateriaById(2)).thenReturn(nuevaMateria);
        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumno);

        Asignatura nuevaAsignatura = alumnoService.agregarAsignatura(1, asignaturaDto);

        assertNotNull(nuevaAsignatura);
        assertEquals(EstadoAsignatura.NO_CURSADA, nuevaAsignatura.getEstado());
        assertEquals(nuevaMateria, nuevaAsignatura.getMateria());
        assertNull(nuevaAsignatura.getNota());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(materiaDao, times(1)).findMateriaById(2);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testAgregarAsignaturaAlumnoNoExiste()
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(2);
        asignaturaDto.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturaDto.setNota(null);

        when(alumnoDao.findAlumnoById(1)).thenReturn(null);

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.agregarAsignatura(1, asignaturaDto);
        });

        assertEquals("Alumno no encontrado con id: 1", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(materiaDao, times(0)).findMateriaById(anyInt());
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testAgregarAsignaturaYaExiste()
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        // Agregar una asignatura para materiaId 1 ya existe en el alumno
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(1);
        asignaturaDto.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturaDto.setNota(null);

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        AsignaturaAlreadyExistsException exception = assertThrows(AsignaturaAlreadyExistsException.class, () -> {
            alumnoService.agregarAsignatura(1, asignaturaDto);
        });

        assertEquals("El alumno ya tiene una asignatura para esta materia.", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(materiaDao, times(0)).findMateriaById(anyInt());
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testAgregarAsignaturaMateriaNoExiste()
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(2);
        asignaturaDto.setEstado(EstadoAsignatura.NO_CURSADA);
        asignaturaDto.setNota(null);

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);
        when(materiaDao.findMateriaById(2)).thenReturn(null);

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            alumnoService.agregarAsignatura(1, asignaturaDto);
        });

        assertEquals("Materia no encontrada con id: 2", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(materiaDao, times(1)).findMateriaById(2);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaCursada() throws AlumnoNotFoundException, AsignaturaNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        Asignatura asignaturaCursada = new Asignatura(1, materia, EstadoAsignatura.NO_CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignaturaCursada)));

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        // Simula que el alumno se ha actualizado correctamente
        alumno.getAsignaturas().get(0).setEstado(EstadoAsignatura.CURSADA);
        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumno);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.CURSADA, null);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.CURSADA, resultado.getEstado());
        assertNull(resultado.getNota());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaCursadaConNota() throws AlumnoNotFoundException, AsignaturaNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.CURSADA, 7);
        });

        assertEquals("Si una asignatura esta CURSADA no debe tener nota.", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaAprobada() throws AlumnoNotFoundException, AsignaturaNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        Asignatura asignaturaCursada = new Asignatura(1, materia, EstadoAsignatura.CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignaturaCursada)));

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);
        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumno);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.APROBADA, 7);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.APROBADA, resultado.getEstado());
        assertEquals(7, resultado.getNota());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaAprobadaNotaInvalida() throws AlumnoNotFoundException,
            AsignaturaNotFoundException, EstadoIncorrectoException, NotaInvalidaException {
        // Cambiar el estado a CURSADA
        Asignatura asignaturaCursada = new Asignatura(1, materia, EstadoAsignatura.CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignaturaCursada)));

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        NotaInvalidaException exception = assertThrows(NotaInvalidaException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.APROBADA, 3);
        });

        assertEquals("Para aprobar una asignatura, la nota debe ser mayor que 4.", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaAprobadaNotaNula() throws AlumnoNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        // Cambiar el estado a CURSADA
        Asignatura asignaturaCursada = new Asignatura(1, materia, EstadoAsignatura.CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignaturaCursada)));

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.APROBADA, null);
        });

        assertEquals("Para aprobar una asignatura, la nota no puede ser nula.", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaNoCursada() throws AlumnoNotFoundException, AsignaturaNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        Asignatura asignaturaCursada = new Asignatura(1, materia, EstadoAsignatura.CURSADA, null);
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignaturaCursada)));

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        alumno.getAsignaturas().get(0).setEstado(EstadoAsignatura.APROBADA);

        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumno);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.NO_CURSADA, null);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.NO_CURSADA, resultado.getEstado());
        assertNull(resultado.getNota());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaNoExiste() throws AlumnoNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 2, EstadoAsignatura.APROBADA, 7);
        });

        assertEquals("Asignatura con ID 2 no encontrada para el alumno con ID 1", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaEstadoInvalido() throws AlumnoNotFoundException, AsignaturaNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 1, null, null);
        });

        assertEquals("Estado no válido para la asignatura.", exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaConCorrelatividades() throws AlumnoNotFoundException,
            AsignaturaNotFoundException, EstadoIncorrectoException, NotaInvalidaException {
        // Crear una materia con correlatividades
        Materia correlativa = new Materia(2, "Química", 1, 1, 103);
        materia.setCorrelatividades(Arrays.asList(2));

        // Agregar una asignatura correlativa aprobada
        Asignatura asignaturaCorrelativa = new Asignatura(2, correlativa, EstadoAsignatura.APROBADA, 6);
        alumno.getAsignaturas().add(asignaturaCorrelativa);

        // Cambiar el estado de la asignatura a CURSADA
        Asignatura asignaturaCursada = alumno.getAsignaturas().get(0);
        asignaturaCursada.setEstado(EstadoAsignatura.CURSADA);
        asignaturaCursada.setNota(null);

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);
        when(alumnoDao.updateAlumno(any(Alumno.class))).thenReturn(alumno);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.APROBADA, 7);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.APROBADA, resultado.getEstado());
        assertEquals(7, resultado.getNota());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(1)).updateAlumno(any(Alumno.class));
    }

    @Test
    void testModificarEstadoAsignaturaSinCorrelatividades() throws AlumnoNotFoundException,
            EstadoIncorrectoException, NotaInvalidaException {
        // Crear una materia con correlatividades
        new Materia(2, "Química", 1, 1, 103);
        materia.setCorrelatividades(Arrays.asList(2));

        // Asegurarse de que no hay asignaturas correlativas aprobadas
        alumno.setAsignaturas(new ArrayList<>(Arrays.asList(asignatura)));

        // Cambiar el estado de la asignatura a CURSADA
        Asignatura asignaturaCursada = alumno.getAsignaturas().get(0);
        asignaturaCursada.setEstado(EstadoAsignatura.CURSADA);
        asignaturaCursada.setNota(null);

        when(alumnoDao.findAlumnoById(1)).thenReturn(alumno);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> {
            alumnoService.modificarEstadoAsignatura(1, 1, EstadoAsignatura.APROBADA, 7);
        });

        assertEquals("Debe aprobar todas las materias correlativas antes de aprobar esta asignatura.",
                exception.getMessage());
        verify(alumnoDao, times(1)).findAlumnoById(1);
        verify(alumnoDao, times(0)).updateAlumno(any(Alumno.class));
    }
}
