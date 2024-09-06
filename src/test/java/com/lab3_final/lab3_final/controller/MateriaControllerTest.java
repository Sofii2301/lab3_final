package com.lab3_final.lab3_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab3_final.lab3_final.business.MateriaService;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.CircularDependencyException;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class MateriaControllerTest {

    MockMvc mockMvc;

    @Mock
    MateriaService materiaService;

    @InjectMocks
    MateriaController materiaController;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(materiaController).build();
    }

    @Test
    void testCrearMateria_Success() throws Exception {
        MateriaDto materiaDto = new MateriaDto(1, "Matemática", 2023, 1, 1, Arrays.asList(2, 3));
        Materia materia = new Materia(1, "Matemática", 2023, 1, 1);

        when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(materia);

        mockMvc.perform(post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.materiaId").value(materia.getMateriaId()))
                .andExpect(jsonPath("$.nombre").value(materia.getNombre()));

        verify(materiaService, times(1)).crearMateria(any(MateriaDto.class));
    }

    @Test
    void testCrearMateria_MateriaAlreadyExistsException() throws Exception {
        MateriaDto materiaDto = new MateriaDto(1, "Matemática", 2023, 1, 1, Arrays.asList(2, 3));

        when(materiaService.crearMateria(any(MateriaDto.class)))
                .thenThrow(new MateriaAlreadyExistsException("La materia ya existe"));

        mockMvc.perform(post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("La materia ya existe"));

        verify(materiaService, times(1)).crearMateria(any(MateriaDto.class));
    }

    @Test
    void testCrearMateria_ProfesorNotFoundException() throws Exception {
        MateriaDto materiaDto = new MateriaDto(1, "Matemática", 2023, 1, 1, Arrays.asList(2, 3));

        when(materiaService.crearMateria(any(MateriaDto.class)))
                .thenThrow(new ProfesorNotFoundException("Profesor no encontrado"));

        mockMvc.perform(post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Profesor no encontrado"));

        verify(materiaService, times(1)).crearMateria(any(MateriaDto.class));
    }

    @Test
    void testObtenerMateriaPorId_Success() throws Exception {
        Materia materia = new Materia(1, "Matemática", 2023, 1, 1);

        when(materiaService.obtenerMateriaPorId(anyInt())).thenReturn(materia);

        mockMvc.perform(get("/materia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materiaId").value(materia.getMateriaId()))
                .andExpect(jsonPath("$.nombre").value(materia.getNombre()));

        verify(materiaService, times(1)).obtenerMateriaPorId(1);
    }

    @Test
    void testObtenerMateriaPorId_MateriaNotFoundException() throws Exception {
        when(materiaService.obtenerMateriaPorId(anyInt()))
                .thenThrow(new MateriaNotFoundException("No se encontró una materia con ese id"));

        mockMvc.perform(get("/materia/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró una materia con ese id"));

        verify(materiaService, times(1)).obtenerMateriaPorId(1);
    }

    @Test
    void testObtenerTodasLasMaterias_Success() throws Exception {
        Materia materia1 = new Materia(1, "Matemática", 2023, 1, 1);
        Materia materia2 = new Materia(2, "Física", 2023, 2, 1);
        List<Materia> materias = Arrays.asList(materia1, materia2);

        when(materiaService.obtenerTodasLasMaterias()).thenReturn(materias);

        mockMvc.perform(get("/materia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materiaId").value(materia1.getMateriaId()))
                .andExpect(jsonPath("$[1].materiaId").value(materia2.getMateriaId()));

        verify(materiaService, times(1)).obtenerTodasLasMaterias();
    }

    @Test
    void testEliminarMateria_Success() throws Exception {
        doNothing().when(materiaService).eliminarMateria(anyInt());

        mockMvc.perform(delete("/materia/1"))
                .andExpect(status().isNoContent());

        verify(materiaService, times(1)).eliminarMateria(1);
    }

    @Test
    void testEliminarMateria_MateriaNotFoundException() throws Exception {
        doThrow(new MateriaNotFoundException("No se encontró una materia con ese id")).when(materiaService)
                .eliminarMateria(anyInt());

        mockMvc.perform(delete("/materia/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró una materia con ese id"));

        verify(materiaService, times(1)).eliminarMateria(1);
    }
}
