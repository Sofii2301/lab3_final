package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Alumno {
    private Integer idAlumno;
    private String nombre;
    private String apellido;
    private Integer dni;

    private List<Asignatura> asignaturas;

    public Alumno() {
    }

    public Alumno(Integer idAlumno, String nombre, String apellido, Integer dni) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        asignaturas = new ArrayList<>();
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
