package com.lab3_final.lab3_final.business.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lab3_final.lab3_final.business.AlumnoService;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.persistence.AlumnoDao;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoDao alumnoDao;

    public AlumnoServiceImpl(AlumnoDao alumnoDao) {
        this.alumnoDao = alumnoDao;
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoDao.findAllAlumnos();
    }

    @Override
    public Alumno obtenerAlumnoPorId(Integer idAlumno) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoById(idAlumno);
    }

    @Override
    public Alumno crearAlumno(AlumnoDto alumnoDto) throws AlumnoAlreadyExistsException {
        Alumno alumno = convertirAlumnoDtoAAlumno(alumnoDto);
        return alumnoDao.saveAlumno(alumno);
    }

    @Override
    public Alumno actualizarAlumno(AlumnoDto alumnoDto, Integer idAlumno) throws AlumnoNotFoundException {
        Alumno alumnoExistente = alumnoDao.findAlumnoById(idAlumno);
        alumnoExistente.setNombre(alumnoDto.getNombre());
        alumnoExistente.setApellido(alumnoDto.getApellido());
        alumnoExistente.setDni(alumnoDto.getDni());
        alumnoExistente.setAsignaturas(alumnoDto.getAsignaturas());
        return alumnoDao.updateAlumno(alumnoExistente);
    }

    @Override
    public void eliminarAlumno(Integer idAlumno) throws AlumnoNotFoundException {
        alumnoDao.deleteAlumnoById(idAlumno);
    }

    private Alumno convertirAlumnoDtoAAlumno(AlumnoDto alumnoDto) {
        return new Alumno(
                alumnoDto.getIdAlumno(),
                alumnoDto.getNombre(),
                alumnoDto.getApellido(),
                alumnoDto.getDni());
    }
}
