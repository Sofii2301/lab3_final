package com.lab3_final.lab3_final.persistence;

import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaDao {

    Materia saveMateria(Materia materia) throws MateriaAlreadyExistsException;

    Materia findMateriaById(int idMateria) throws MateriaNotFoundException;

    List<Materia> findAllMaterias();

    void deleteMateriaById(int idMateria) throws MateriaNotFoundException;

    boolean existsByNombreAndAnioAndCuatrimestre(String nombre, int anio, int cuatrimestre);

    boolean existsById(int idMateria);
}
