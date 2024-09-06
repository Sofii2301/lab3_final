package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.model.Asignatura;
import com.lab3_final.lab3_final.model.EstadoAsignatura;
import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;

import java.util.List;

import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.dto.AsignaturaDto;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;

public interface AlumnoService {
        List<Alumno> obtenerTodosLosAlumnos();

        Alumno obtenerAlumnoPorId(Integer idAlumno) throws AlumnoNotFoundException;

        Alumno crearAlumno(AlumnoDto alumnoDto) throws AlumnoAlreadyExistsException;

        Alumno actualizarAlumno(AlumnoDto alumnoDto, Integer idAlumno) throws AlumnoNotFoundException;

        void eliminarAlumno(Integer idAlumno) throws AlumnoNotFoundException;

        Asignatura modificarEstadoAsignatura(int idAlumno, int idAsignatura, EstadoAsignatura nuevoEstado, Integer nota)
                        throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException;

        Asignatura agregarAsignatura(int idAlumno, AsignaturaDto asignaturaDto)
                        throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException;
}
