package com.lab3_final.lab3_final.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MateriaDto {
    private int materiaId;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private int profesorId;
    private List<Integer> correlatividades = new ArrayList<>();

    public MateriaDto() {
    }

    public MateriaDto(int materiaId, String nombre, int anio, int cuatrimestre, int profesorId,
            List<Integer> correlatividades) {
        this.materiaId = materiaId;
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.profesorId = profesorId;
        this.correlatividades = correlatividades;
    }
}
