package com.lab3_final.lab3_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab3_final.lab3_final.business.ProfesorService;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ProfesorControllerTest {

    @InjectMocks
    ProfesorController profesorController;

    @Mock
    ProfesorService profesorService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(profesorController).build();
    }

    @Test
    void testObtenerTodosLosProfesores() throws Exception {
        Profesor profesor = new Profesor();
        profesor.setNombre("Carlos");
        profesor.setApellido("Gomez");
        profesor.setTitulo("Programador");

        Mockito.when(profesorService.obtenerTodosLosProfesores()).thenReturn(Collections.singletonList(profesor));

        mockMvc.perform(MockMvcRequestBuilders.get("/profesor")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testObtenerProfesorPorId() throws Exception {
        Profesor profesor = new Profesor();
        profesor.setIdProfesor(1);
        profesor.setNombre("Carlos");
        profesor.setApellido("Gomez");

        Mockito.when(profesorService.obtenerProfesorPorId(1)).thenReturn(profesor);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Profesor response = mapper.readValue(result.getResponse().getContentAsString(), Profesor.class);
        assertEquals(profesor.getIdProfesor(), response.getIdProfesor());
    }

    @Test
    void testObtenerProfesorPorIdNotFound() throws Exception {
        Mockito.when(profesorService.obtenerProfesorPorId(1))
                .thenThrow(new ProfesorNotFoundException("No se encontró un profesor con ese id"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profesor/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearProfesor() throws Exception {
        ProfesorDto profesorDto = new ProfesorDto("Carlos", "Gomez", "Programador", Collections.emptyList());
        Profesor profesor = new Profesor("Carlos", "Gomez", "Programador");

        Mockito.when(profesorService.crearProfesor(any(ProfesorDto.class))).thenReturn(profesor);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profesorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(mapper.writeValueAsString(profesor), result.getResponse().getContentAsString());
    }

    @Test
    void testModificarProfesor() throws Exception {
        ProfesorDto profesorDto = new ProfesorDto("Carlos", "Gomez", "Programador", Collections.emptyList());
        Profesor profesorActualizado = new Profesor("Carlos", "Gomez", "Programador");

        Mockito.when(profesorService.modificarProfesor(eq(1), any(ProfesorDto.class))).thenReturn(profesorActualizado);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/profesor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profesorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Profesor response = mapper.readValue(result.getResponse().getContentAsString(), Profesor.class);
        assertEquals(profesorActualizado.getIdProfesor(), response.getIdProfesor());
    }

    @Test
    void testModificarProfesorNotFound() throws Exception {
        ProfesorDto profesorDto = new ProfesorDto("Carlos", "Gomez", "Programador", Collections.emptyList());

        Mockito.when(profesorService.modificarProfesor(any(Integer.class), any(ProfesorDto.class)))
                .thenThrow(new ProfesorNotFoundException("No se encontró un profesor con ese id"));

        mockMvc.perform(MockMvcRequestBuilders.put("/profesor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profesorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProfesor() throws Exception {
        Mockito.doNothing().when(profesorService).eliminarProfesor(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProfesorNotFound() throws Exception {
        Mockito.doThrow(new ProfesorNotFoundException("Profesor no encontrado")).when(profesorService)
                .eliminarProfesor(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerMateriasPorProfesor() throws Exception {
        Materia materia = new Materia();
        materia.setNombre("Física");

        Mockito.when(profesorService.obtenerMateriasPorProfesor(1)).thenReturn(Collections.singletonList(materia));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/materias/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Materia> response = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(1, response.size());
    }
}
