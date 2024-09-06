package com.lab3_final.lab3_final.persistence.implementation;

import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProfesorDaoImpl implements ProfesorDao {
    private final Map<Integer, Profesor> repositorioProfesores = new HashMap<>();
    private int currentId = 1;

    @Override
    public List<Profesor> findAllProfesores() {
        return new ArrayList<>(repositorioProfesores.values());
    }

    @Override
    public Profesor findProfesorById(int idProfesor) throws ProfesorNotFoundException {
        Profesor profesor = repositorioProfesores.get(idProfesor);
        if (profesor == null) {
            throw new ProfesorNotFoundException("El profesor con ID " + idProfesor + " no existe.");
        }
        return profesor;
    }

    @Override
    public Profesor saveProfesor(Profesor profesor) {
        profesor.setIdProfesor(currentId++);
        repositorioProfesores.put(profesor.getIdProfesor(), profesor);
        return profesor;
    }

    @Override
    public Profesor updateProfesor(Profesor profesor) throws ProfesorNotFoundException {
        if (!repositorioProfesores.containsKey(profesor.getIdProfesor())) {
            throw new ProfesorNotFoundException("El profesor con ID " + profesor.getIdProfesor() + " no existe.");
        }
        repositorioProfesores.put(profesor.getIdProfesor(), profesor);
        return profesor;
    }

    @Override
    public void deleteProfesorById(int idProfesor) throws ProfesorNotFoundException {
        if (!repositorioProfesores.containsKey(idProfesor)) {
            throw new ProfesorNotFoundException("El profesor con ID " + idProfesor + " no existe.");
        }
        repositorioProfesores.remove(idProfesor);
    }

    @Override
    public boolean existsById(int idProfesor) {
        return repositorioProfesores.containsKey(idProfesor);
    }
}
