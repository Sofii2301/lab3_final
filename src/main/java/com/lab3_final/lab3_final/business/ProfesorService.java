package com.lab3_final.lab3_final.business;

import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.exception.ProfesorAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Materia;

import java.util.List;

public interface ProfesorService {
    List<Profesor> obtenerTodosLosProfesores();

    Profesor obtenerProfesorPorId(Integer idProfesor) throws ProfesorNotFoundException;

    Profesor crearProfesor(ProfesorDto profesorDto) throws ProfesorAlreadyExistsException;

    Profesor modificarProfesor(int idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException;

    void eliminarProfesor(int idProfesor) throws ProfesorNotFoundException;

    List<Materia> obtenerMateriasPorProfesor(int idProfesor) throws ProfesorNotFoundException;
}
