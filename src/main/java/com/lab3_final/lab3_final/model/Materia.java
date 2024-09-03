package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Materia {

    private int materiaId;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private int profesorId;

    private List<Integer> correlatividades;

    public Materia() {
        correlatividades = new ArrayList<>();
    }

    public Materia(Integer materiaId, String nombre, int anio, int cuatrimestre, int profesorId) {
        this.materiaId = materiaId;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.profesorId = profesorId;

        correlatividades = new ArrayList<>();
    }
}
