package com.lab3_final.lab3_final.business.implementation;

import com.lab3_final.lab3_final.business.ProfesorService;
import com.lab3_final.lab3_final.dto.ProfesorDto;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.ProfesorAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorDao profesorDao;

    public ProfesorServiceImpl(ProfesorDao profesorDao) {
        this.profesorDao = profesorDao;
    }

    @Override
    public List<Profesor> obtenerTodosLosProfesores() {
        return profesorDao.findAllProfesores();
    }

    @Override
    public Profesor obtenerProfesorPorId(Integer idProfesor) throws ProfesorNotFoundException {
        Profesor profesor = profesorDao.findProfesorById(idProfesor);
        if (profesor == null) {
            throw new ProfesorNotFoundException("Profesor no encontrado.");
        }
        return profesor;
    }

    @Override
    public Profesor crearProfesor(ProfesorDto profesorDto) throws ProfesorAlreadyExistsException {
        if (profesorDao.existsByNombreAndApellido(profesorDto.getNombre(), profesorDto.getApellido())) {
            throw new ProfesorAlreadyExistsException("El profesor ya existe.");
        }
        Profesor profesor = convertirProfesorDtoAProfesor(profesorDto);
        return profesorDao.saveProfesor(profesor);
    }

    @Override
    public Profesor modificarProfesor(int idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException {
        Profesor profesorExistente = profesorDao.findProfesorById(idProfesor);
        if (profesorExistente == null) {
            throw new ProfesorNotFoundException("Profesor no encontrado.");
        }
        profesorExistente.setNombre(profesorDto.getNombre());
        profesorExistente.setApellido(profesorDto.getApellido());
        profesorExistente.setTitulo(profesorDto.getTitulo());
        return profesorDao.updateProfesor(profesorExistente);
    }

    @Override
    public void eliminarProfesor(int idProfesor) throws ProfesorNotFoundException {
        if (!profesorDao.existsById(idProfesor)) {
            throw new ProfesorNotFoundException("Profesor no encontrado.");
        }
        profesorDao.deleteProfesorById(idProfesor);
    }

    @Override
    public List<Materia> obtenerMateriasPorProfesor(int idProfesor) throws ProfesorNotFoundException {
        Profesor profesor = profesorDao.findProfesorById(idProfesor);
        if (profesor == null) {
            throw new ProfesorNotFoundException("Profesor no encontrado.");
        }
        return profesor.getMateriasDictadas().stream()
                .sorted((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()))
                .collect(Collectors.toList());
    }

    private Profesor convertirProfesorDtoAProfesor(ProfesorDto profesorDto) {
        return new Profesor(
                profesorDto.getNombre(),
                profesorDto.getApellido(),
                profesorDto.getTitulo());
    }
}
