package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.model.Alumno;

import java.util.List;

import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;

public interface AlumnoService {
    List<Alumno> obtenerTodosLosAlumnos();

    Alumno obtenerAlumnoPorId(Integer idAlumno) throws AlumnoNotFoundException;

    Alumno crearAlumno(AlumnoDto alumnoDto) throws AlumnoAlreadyExistsException;

    Alumno actualizarAlumno(AlumnoDto alumnoDto, Integer idAlumno) throws AlumnoNotFoundException;

    void eliminarAlumno(Integer idAlumno) throws AlumnoNotFoundException;
}
