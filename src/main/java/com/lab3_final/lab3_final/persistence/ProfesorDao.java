package com.lab3_final.lab3_final.persistence;

import java.util.List;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.exception.ProfesorAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

public interface ProfesorDao {
    List<Profesor> findAllProfesores();

    Profesor findProfesorById(int idProfesor) throws ProfesorNotFoundException;

    Profesor saveProfesor(Profesor profesor) throws ProfesorAlreadyExistsException;

    Profesor updateProfesor(Profesor profesor) throws ProfesorNotFoundException;

    void deleteProfesorById(int idProfesor) throws ProfesorNotFoundException;

    boolean existsByNombreAndApellido(String nombre, String apellido);

    boolean existsById(int idProfesor);
}
