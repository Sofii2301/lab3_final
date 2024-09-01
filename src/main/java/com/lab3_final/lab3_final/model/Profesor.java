package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Profesor {
    private int idProfesor;
    private String nombre;
    private String apellido;
    private String titulo;

    private List<Materia> materiasDictadas;

    public Profesor() {
        this.materiasDictadas = new ArrayList<>();
    }

    public Profesor(String nombre, String apellido, String titulo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.titulo = titulo;
        this.materiasDictadas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "idProfesor=" + idProfesor +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}