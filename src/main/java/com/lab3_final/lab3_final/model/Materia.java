package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Materia {

    private int materiaId;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private Profesor profesor;

    private List<Materia> correlatividades;

    public Materia() {
        correlatividades = new ArrayList<>();
    }

    public Materia(String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.profesor = profesor;

        correlatividades = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Materia{" +
                "materiaId=" + materiaId +
                ", nombre='" + nombre + '\'' +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", profesor=" + (profesor != null ? profesor.getIdProfesor() : "null") +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(materiaId, nombre, anio, cuatrimestre, profesor, correlatividades);
    }
}
