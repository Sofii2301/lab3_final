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
        return profesorDao.findProfesorById(idProfesor);
    }

    @Override
    public Profesor crearProfesor(ProfesorDto profesorDto) throws ProfesorAlreadyExistsException {
        Profesor profesor = convertirProfesorDtoAProfesor(profesorDto);
        return profesorDao.saveProfesor(profesor);
    }

    @Override
    public Profesor modificarProfesor(int idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException {
        Profesor profesorExistente = profesorDao.findProfesorById(idProfesor);
        profesorExistente.setNombre(profesorDto.getNombre());
        profesorExistente.setApellido(profesorDto.getApellido());
        profesorExistente.setTitulo(profesorDto.getTitulo());
        return profesorDao.updateProfesor(profesorExistente);
    }

    @Override
    public void eliminarProfesor(int idProfesor) throws ProfesorNotFoundException {
        profesorDao.deleteProfesorById(idProfesor);
    }

    @Override
    public List<Materia> obtenerMateriasPorProfesor(int idProfesor) throws ProfesorNotFoundException {
        Profesor profesor = profesorDao.findProfesorById(idProfesor);
        List<Materia> materias = profesor.getMateriasDictadas();
        return materias.stream()
                .sorted((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()))
                .collect(Collectors.toList());
    }

    private Profesor convertirProfesorDtoAProfesor(ProfesorDto profesorDto) {
        return new Profesor(
            profesorDto.getNombre(),
            profesorDto.getApellido(),
            profesorDto.getTitulo()
        );
    }
}
