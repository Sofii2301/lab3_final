package com.lab3_final.lab3_final.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Asignatura {

    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;

    public Asignatura(Materia materia, EstadoAsignatura estado, Integer nota) {
        this.materia = materia;
        this.estado = estado;
        this.nota = nota;
    }

    public Asignatura() {
    }
}
