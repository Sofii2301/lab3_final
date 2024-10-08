package com.lab3_final.lab3_final.persistence.implementation;

import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.persistence.AlumnoDao;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

@Repository
public class AlumnoDaoImpl implements AlumnoDao {
    private static final Map<Integer, Alumno> repositorioAlumnos = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public List<Alumno> findAllAlumnos() {
        return new ArrayList<>(repositorioAlumnos.values());
    }

    @Override
    public Alumno findAlumnoById(Integer idAlumno) throws AlumnoNotFoundException {
        Alumno alumno = repositorioAlumnos.get(idAlumno);
        if (alumno == null) {
            throw new AlumnoNotFoundException("El alumno con ID " + idAlumno + " no existe.");
        }
        return alumno;
    }

    @Override
    public Alumno saveAlumno(Alumno alumno) throws AlumnoAlreadyExistsException {
        if (alumno.getDni() == null) {
            throw new IllegalArgumentException("El DNI del alumno no puede ser nulo.");
        }
        if (repositorioAlumnos.values().stream().anyMatch(a -> a.getDni().equals(alumno.getDni()))) {
            throw new AlumnoAlreadyExistsException("El alumno con DNI " + alumno.getDni() + " ya existe.");
        }
        int newId = idGenerator.getAndIncrement();
        alumno.setIdAlumno(newId);
        repositorioAlumnos.put(newId, alumno);
        return alumno;
    }

    @Override
    public Alumno updateAlumno(Alumno alumno) throws AlumnoNotFoundException {
        if (!repositorioAlumnos.containsKey(alumno.getIdAlumno())) {
            throw new AlumnoNotFoundException("El alumno con ID " + alumno.getIdAlumno() + " no existe.");
        }
        repositorioAlumnos.put(alumno.getIdAlumno(), alumno);
        return alumno;
    }

    @Override
    public void deleteAlumnoById(Integer idAlumno) throws AlumnoNotFoundException {
        if (!repositorioAlumnos.containsKey(idAlumno)) {
            throw new AlumnoNotFoundException("El alumno con ID " + idAlumno + " no existe.");
        }
        repositorioAlumnos.remove(idAlumno);
    }

    public void clearRepository() {
        repositorioAlumnos.clear();
        idGenerator.set(1);
    }
}
