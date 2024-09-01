package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto materiaDto) throws MateriaAlreadyExistsException, ProfesorNotFoundException;

    Materia obtenerMateriaPorId(int idMateria) throws MateriaNotFoundException;

    List<Materia> obtenerTodasLasMaterias();

    void eliminarMateria(int idMateria) throws MateriaNotFoundException;
}
