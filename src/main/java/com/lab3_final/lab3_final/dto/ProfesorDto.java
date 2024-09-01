package com.lab3_final.lab3_final.dto;

import java.util.ArrayList;

import com.lab3_final.lab3_final.model.Materia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfesorDto {
    private int idProfesor;
    private String nombre;
    private String apellido;
    private String titulo;

    ArrayList<Materia> materiasDictadas = new ArrayList<>();

    public ProfesorDto() {
    }

    public ProfesorDto(String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;
    }
}
