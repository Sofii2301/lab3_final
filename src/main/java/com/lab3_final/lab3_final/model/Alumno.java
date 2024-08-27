package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Alumno {
    private Integer legajo;
    private String nombre;
    private String apellido;
    private Integer dni;

    private List<Asignatura> asignaturas;

    public Alumno() {
    }

    public Alumno(Integer legajo, String nombre, String apellido, Integer dni) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }
}
