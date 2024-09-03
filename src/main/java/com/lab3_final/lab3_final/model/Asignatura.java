package com.lab3_final.lab3_final.model;

import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Asignatura {
    private int asignaturaId;
    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;

    public Asignatura(int asignaturaId, Materia materia, EstadoAsignatura estado, Integer nota) {
        this.asignaturaId = asignaturaId;
        this.materia = materia;
        this.estado = estado;
        this.nota = nota;
    }

    public Asignatura() {
    }

    public void cursarAsignatura() {
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La materia debe estar cursada para aprobar");
        }
        if (nota >= 4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        }
    }
}
