package com.lab3_final.lab3_final.persistence.implementation;

import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MateriaDaoImpl implements MateriaDao {

    private final Map<Integer, Materia> materiaMap = new HashMap<>();
    private int nextId = 1;

    @Override
    public Materia saveMateria(Materia materia) throws MateriaAlreadyExistsException {
        if (existsByNombreAndAnioAndCuatrimestre(materia.getNombre(), materia.getAnio(), materia.getCuatrimestre())) {
            throw new MateriaAlreadyExistsException("Materia con nombre " + materia.getNombre() + ", año "
                    + materia.getAnio() + " y cuatrimestre " + materia.getCuatrimestre() + " ya existe.");
        }
        materia.setMateriaId(nextId++);
        materiaMap.put(materia.getMateriaId(), materia);
        return materia;
    }

    @Override
    public Materia findMateriaById(int idMateria) throws MateriaNotFoundException {
        Materia materia = materiaMap.get(idMateria);
        if (materia == null) {
            throw new MateriaNotFoundException("Materia con ID " + idMateria + " no encontrada.");
        }
        return materia;
    }

    @Override
    public List<Materia> findAllMaterias() {
        return new ArrayList<>(materiaMap.values());
    }

    @Override
    public void deleteMateriaById(int idMateria) throws MateriaNotFoundException {
        if (!materiaMap.containsKey(idMateria)) {
            throw new MateriaNotFoundException("Materia con ID " + idMateria + " no encontrada.");
        }
        materiaMap.remove(idMateria);
    }

    @Override
    public boolean existsByNombreAndAnioAndCuatrimestre(String nombre, int anio, int cuatrimestre) {
        return materiaMap.values().stream()
                .anyMatch(materia -> materia.getNombre().equals(nombre) &&
                        materia.getAnio() == anio &&
                        materia.getCuatrimestre() == cuatrimestre);
    }

    @Override
    public boolean existsById(int idMateria) {
        return materiaMap.containsKey(idMateria);
    }
}
