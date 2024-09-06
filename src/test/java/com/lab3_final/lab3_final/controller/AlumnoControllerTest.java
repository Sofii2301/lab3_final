package com.lab3_final.lab3_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab3_final.lab3_final.business.AlumnoService;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.model.Asignatura;
import com.lab3_final.lab3_final.model.EstadoAsignatura;
import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.NotaInvalidaException;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.dto.AsignaturaDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class AlumnoControllerTest {

        @InjectMocks
        AlumnoController alumnoController;

        @Mock
        AlumnoService alumnoService;

        MockMvc mockMvc;

        private static ObjectMapper mapper = new ObjectMapper();

        @BeforeEach
        void setUp() {
                this.mockMvc = MockMvcBuilders.standaloneSetup(alumnoController).build();
        }

        @Test
        void testObtenerTodosLosAlumnos() throws Exception {
                Alumno alumno = new Alumno();
                alumno.setNombre("Juan");
                alumno.setApellido("Perez");
                alumno.setDni(12345678);

                Mockito.when(alumnoService.obtenerTodosLosAlumnos()).thenReturn(Collections.singletonList(alumno));

                mockMvc.perform(MockMvcRequestBuilders.get("/alumno")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testObtenerAlumnoPorId() throws Exception {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(1);
                alumno.setNombre("Juan");
                alumno.setApellido("Pérez");
                Mockito.when(alumnoService.obtenerAlumnoPorId(1)).thenReturn(alumno);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                Alumno response = mapper.readValue(result.getResponse().getContentAsString(), Alumno.class);
                assertEquals(alumno.getIdAlumno(), response.getIdAlumno());
        }

        @Test
        void testObtenerAlumnoPorIdNotFound() throws Exception {
                Mockito.when(alumnoService.obtenerAlumnoPorId(1))
                                .thenThrow(new AlumnoNotFoundException("No se encontró un alumno con ese id"));

                mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCrearAlumno() throws Exception {
                AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Perez", 12345678);
                Alumno alumno = new Alumno(1, "Juan", "Perez", 12345678);

                Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(alumno);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(alumnoDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andReturn();

                assertEquals(mapper.writeValueAsString(alumno), result.getResponse().getContentAsString());
        }

        @Test
        void testCrearAlumnoConflict() throws Exception {
                AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Perez", 12345678);

                Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class)))
                                .thenThrow(new AlumnoAlreadyExistsException(
                                                "El alumno con ese id ya existe en la base de datos"));

                mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(alumnoDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isConflict());
        }

        @Test
        void testActualizarAlumno() throws Exception {
                AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Pérez", 12345678);
                Alumno alumnoActualizado = new Alumno(1, "Juan", "Perez", 12345678);

                Mockito.when(alumnoService.actualizarAlumno(any(AlumnoDto.class), eq(1))).thenReturn(alumnoActualizado);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(alumnoDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                Alumno response = mapper.readValue(result.getResponse().getContentAsString(), Alumno.class);
                assertEquals(alumnoActualizado.getIdAlumno(), response.getIdAlumno());
        }

        @Test
        void testActualizarAlumnoNotFound() throws Exception {
                AlumnoDto alumnoDto = new AlumnoDto(1, "Juan", "Perez", 12345678);

                Mockito.when(alumnoService.actualizarAlumno(any(AlumnoDto.class), any(Integer.class)))
                                .thenThrow(new AlumnoNotFoundException("No se encontró un alumno con ese id"));

                mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(alumnoDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testEliminarAlumnoNotFound() throws Exception {
                Mockito.doThrow(new AlumnoNotFoundException("Alumno no encontrado")).when(alumnoService)
                                .eliminarAlumno(1);

                mockMvc.perform(MockMvcRequestBuilders.delete("/alumno/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testEliminarAlumno() throws Exception {
                Mockito.doNothing().when(alumnoService).eliminarAlumno(1);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/alumno/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent())
                                .andReturn();

                assertEquals("Alumno eliminado correctamente", result.getResponse().getContentAsString());
        }

        @Test
        void testAgregarAsignatura() throws Exception {
                Asignatura asignatura = new Asignatura();
                AsignaturaDto asignaturaDto = new AsignaturaDto();

                Mockito.when(alumnoService.agregarAsignatura(any(Integer.class), any(AsignaturaDto.class)))
                                .thenReturn(asignatura);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/alumno/1/asignatura")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(asignaturaDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andReturn();

                assertEquals(mapper.writeValueAsString(asignatura), result.getResponse().getContentAsString());
        }

        @Test
        void testAgregarAsignaturaAlumnoNotFound() throws Exception {
                AsignaturaDto asignaturaDto = new AsignaturaDto();
                asignaturaDto.setMateriaId(1);

                Mockito.when(alumnoService.agregarAsignatura(eq(1), any(AsignaturaDto.class)))
                                .thenThrow(new AlumnoNotFoundException("Alumno no encontrado"));

                mockMvc.perform(MockMvcRequestBuilders.post("/alumno/1/asignatura")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(asignaturaDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testAgregarAsignaturaConflict() throws Exception {
                AsignaturaDto asignaturaDto = new AsignaturaDto();

                Mockito.when(alumnoService.agregarAsignatura(any(Integer.class), any(AsignaturaDto.class)))
                                .thenThrow(new AsignaturaAlreadyExistsException(
                                                "La asignatura para esta materia ya existe: "));

                mockMvc.perform(MockMvcRequestBuilders.post("/alumno/1/asignatura")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(asignaturaDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isConflict());
        }

        @Test
        void testModificarEstadoAsignatura() throws Exception, EstadoIncorrectoException {
                EstadoAsignatura nuevoEstado = EstadoAsignatura.APROBADA;
                int nota = 7;

                Asignatura asignatura = new Asignatura();
                asignatura.setAsignaturaId(1);
                asignatura.setEstado(nuevoEstado);
                asignatura.setNota(nota);

                Mockito.when(alumnoService.modificarEstadoAsignatura(eq(1), eq(1), eq(nuevoEstado), eq(nota)))
                                .thenReturn(asignatura);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignatura/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"estado\": \"APROBADA\", \"nota\": 7 }")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                Asignatura response = mapper.readValue(result.getResponse().getContentAsString(), Asignatura.class);
                assertEquals(asignatura.getAsignaturaId(), response.getAsignaturaId());
                assertEquals(asignatura.getEstado(), response.getEstado());
                assertEquals(asignatura.getNota(), response.getNota());
        }

        @Test
        void testModificarEstadoAsignaturaNotFound() throws Exception, EstadoIncorrectoException {
                AsignaturaDto asignaturaDto = new AsignaturaDto();
                asignaturaDto.setEstado(EstadoAsignatura.APROBADA);
                asignaturaDto.setNota(8);

                Mockito.when(alumnoService.modificarEstadoAsignatura(any(Integer.class), any(Integer.class),
                                eq(EstadoAsignatura.APROBADA), eq(8)))
                                .thenThrow(new AsignaturaNotFoundException("No se encontró la asignatura con ese id"));

                mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignatura/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(asignaturaDto))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testModificarEstadoAsignaturaAlumnoNotFound() throws Exception, EstadoIncorrectoException {
                EstadoAsignatura nuevoEstado = EstadoAsignatura.APROBADA;
                int nota = 7;

                Mockito.when(alumnoService.modificarEstadoAsignatura(eq(1), eq(1), eq(nuevoEstado), eq(nota)))
                                .thenThrow(new AlumnoNotFoundException("Alumno no encontrado"));

                mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignatura/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"estado\": \"APROBADA\", \"nota\": 7 }")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testModificarEstadoAsignaturaEstadoIncorrecto() throws Exception, EstadoIncorrectoException {
                EstadoAsignatura nuevoEstado = EstadoAsignatura.APROBADA;
                int nota = 7;

                Mockito.when(alumnoService.modificarEstadoAsignatura(eq(1), eq(1), eq(nuevoEstado), eq(nota)))
                                .thenThrow(new EstadoIncorrectoException("Estado incorrecto"));

                mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignatura/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"estado\": \"APROBADA\", \"nota\": 7 }")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testModificarEstadoAsignaturaNotaInvalida() throws Exception, EstadoIncorrectoException {
                EstadoAsignatura nuevoEstado = EstadoAsignatura.APROBADA;
                int nota = 3;

                Mockito.when(alumnoService.modificarEstadoAsignatura(eq(1), eq(1), eq(nuevoEstado), eq(nota)))
                                .thenThrow(new NotaInvalidaException("Nota inválida"));

                mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignatura/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"estado\": \"APROBADA\", \"nota\": 3 }")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }
}
