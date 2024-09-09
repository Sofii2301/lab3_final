package com.lab3_final.lab3_final.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Carrera {

    private final String nombre;
    private int cantidadAnios;
    private List<Materia> materiasList = new ArrayList<>();

    public Carrera(String nombre, int cantidadAnios) {
        this.nombre = nombre;
        this.cantidadAnios = cantidadAnios;
        this.materiasList = new ArrayList<>();
    }

    public void agregarMateria(Materia materia) {
        materiasList.add(materia);
    }
}
