package com.lab3_final.lab3_final.dto;

import com.lab3_final.lab3_final.model.EstadoAsignatura;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignaturaDto {
    private EstadoAsignatura estado;
    private Integer nota;
    private int materiaId;

    public AsignaturaDto() {
    }

    public AsignaturaDto(EstadoAsignatura estado, Integer nota, int materiaId) {
        this.estado = estado;
        this.nota = nota;
        this.materiaId = materiaId;
    }
}
