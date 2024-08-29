package com.lab3_final.lab3_final.dto;

import java.util.ArrayList;

import com.lab3_final.lab3_final.model.Asignatura;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlumnoDto {
    private Integer idAlumno;
    private String nombre;
    private String apellido;
    private Integer dni;
    ArrayList<Asignatura> asignaturas = new ArrayList<>();

    public AlumnoDto() {
    }

    public AlumnoDto(Integer idAlumno, String nombre, String apellido, Integer dni) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }
}
