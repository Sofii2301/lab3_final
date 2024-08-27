package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Profesor {
    private String nombre;
    private String apellido;
    private String titulo;

    private List<Materia> materiasDictadas;

    public Profesor() {
    }

    public Profesor(String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;
    }
}