package com.lab3_final.lab3_final.persistence;

import java.util.List;

import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;

public interface AlumnoDao {
    List<Alumno> findAllAlumnos();

    Alumno findAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;

    Alumno saveAlumno(Alumno alumno) throws AlumnoAlreadyExistsException;

    Alumno updateAlumno(Alumno alumno) throws AlumnoNotFoundException;

    void deleteAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;
}
