package com.lab3_final.lab3_final.business.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab3_final.lab3_final.business.AlumnoService;
import com.lab3_final.lab3_final.dto.AlumnoDto;
import com.lab3_final.lab3_final.dto.AsignaturaDto;
import com.lab3_final.lab3_final.model.Alumno;
import com.lab3_final.lab3_final.model.Asignatura;
import com.lab3_final.lab3_final.model.EstadoAsignatura;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.model.exception.EstadoIncorrectoException;
import com.lab3_final.lab3_final.persistence.AlumnoDao;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.exception.AlumnoAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AlumnoNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.AsignaturaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.NotaInvalidaException;

@Service
public class AlumnoServiceImpl implements AlumnoService {
    @Autowired
    private final AlumnoDao alumnoDao;

    @Autowired
    private final MateriaDao materiaDao;

    public AlumnoServiceImpl(AlumnoDao alumnoDao, MateriaDao materiaDao) {
        this.alumnoDao = alumnoDao;
        this.materiaDao = materiaDao;
    }

    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoDao.findAllAlumnos();
    }

    @Override
    public Alumno obtenerAlumnoPorId(Integer idAlumno) throws AlumnoNotFoundException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);

        if (alumno == null) {
            throw new AlumnoNotFoundException("Alumno no encontrado con id: " + idAlumno);
        }

        return alumno;
    }

    @Override
    public Alumno crearAlumno(AlumnoDto alumnoDto) throws AlumnoAlreadyExistsException {
        Alumno alumno = convertirAlumnoDtoAAlumno(alumnoDto);
        return alumnoDao.saveAlumno(alumno);
    }

    @Override
    public Alumno actualizarAlumno(AlumnoDto alumnoDto, Integer idAlumno) throws AlumnoNotFoundException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);

        if (alumno == null) {
            throw new AlumnoNotFoundException("Alumno no encontrado con id: " + idAlumno);
        }

        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());
        alumno.setAsignaturas(alumnoDto.getAsignaturas());
        return alumnoDao.updateAlumno(alumno);
    }

    @Override
    public void eliminarAlumno(Integer idAlumno) throws AlumnoNotFoundException {
        alumnoDao.deleteAlumnoById(idAlumno);
    }

    public Asignatura agregarAsignatura(int idAlumno, AsignaturaDto asignaturaDto)
            throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaAlreadyExistsException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        if (alumno == null) {
            throw new AlumnoNotFoundException("Alumno no encontrado con id: " + idAlumno);
        }

        // Verificar si la asignatura de la materia ya existe para el alumno
        boolean asignaturaExistente = alumno.getAsignaturas().stream()
                .anyMatch(asignatura -> asignatura.getMateria().getMateriaId() == asignaturaDto.getMateriaId());

        if (asignaturaExistente) {
            throw new AsignaturaAlreadyExistsException("El alumno ya tiene una asignatura para esta materia.");
        }

        Materia materia = materiaDao.findMateriaById(asignaturaDto.getMateriaId());
        if (materia == null) {
            throw new MateriaNotFoundException("Materia no encontrada con id: " + asignaturaDto.getMateriaId());
        }

        // Incrementar el asignaturaId
        int nuevoAsignaturaId = alumno.getAsignaturas().stream()
                .mapToInt(Asignatura::getAsignaturaId)
                .max()
                .orElse(0) + 1;

        Asignatura nuevaAsignatura = new Asignatura();
        nuevaAsignatura.setAsignaturaId(nuevoAsignaturaId);
        nuevaAsignatura.setMateria(materia);
        nuevaAsignatura.setEstado(EstadoAsignatura.NO_CURSADA); // Estado inicial

        alumno.getAsignaturas().add(nuevaAsignatura);
        alumnoDao.updateAlumno(alumno);

        return nuevaAsignatura;
    }

    @Override
    public Asignatura modificarEstadoAsignatura(int idAlumno, int idAsignatura, EstadoAsignatura nuevoEstado,
            Integer nota)
            throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException,
            NotaInvalidaException {

        // Buscar el alumno por su ID
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        if (alumno == null) {
            throw new AlumnoNotFoundException("Alumno no encontrado con id: " + idAlumno);
        }

        // Buscar la asignatura específica del alumno
        Asignatura asignatura = alumno.getAsignaturas().stream()
                .filter(a -> a.getAsignaturaId() == idAsignatura)
                .findFirst()
                .orElseThrow(() -> new AsignaturaNotFoundException(
                        "Asignatura con ID " + idAsignatura + " no encontrada para el alumno con ID " + idAlumno));

        if (nuevoEstado == null) {
            throw new EstadoIncorrectoException("Estado no válido para la asignatura.");
        }

        switch (nuevoEstado) {
            case CURSADA:
                if (nota != null) {
                    throw new EstadoIncorrectoException("Si una asignatura esta CURSADA no debe tener nota.");
                }
                asignatura.setEstado(nuevoEstado);
                asignatura.setNota(null);
                break;
            case APROBADA:
                if (nota == null) {
                    throw new EstadoIncorrectoException("Para aprobar una asignatura, la nota no puede ser nula.");
                }
                if (nota < 4) {
                    throw new NotaInvalidaException("Para aprobar una asignatura, la nota debe ser mayor que 4.");
                }
                if (asignatura.getEstado() != EstadoAsignatura.CURSADA) {
                    throw new EstadoIncorrectoException("La materia debe estar cursada para aprobar");
                }
                verificarCorrelatividades(alumno, asignatura.getMateria());
                asignatura.setEstado(nuevoEstado);
                asignatura.setNota(nota);
                break;
            case NO_CURSADA:
                if (nota != null) {
                    throw new EstadoIncorrectoException("Si una asignatura esta NO_CURSADA no debe tener nota.");
                }
                asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
                asignatura.setNota(null);
                break;
            default:
                throw new EstadoIncorrectoException("Estado no válido para la asignatura.");
        }

        alumnoDao.updateAlumno(alumno);
        return asignatura;
    }

    private Alumno convertirAlumnoDtoAAlumno(AlumnoDto alumnoDto) {
        return new Alumno(
                alumnoDto.getIdAlumno(),
                alumnoDto.getNombre(),
                alumnoDto.getApellido(),
                alumnoDto.getDni());
    }

    private void verificarCorrelatividades(Alumno alumno, Materia materia) throws EstadoIncorrectoException {
        for (Integer correlativaId : materia.getCorrelatividades()) {
            boolean correlativaAprobada = alumno.getAsignaturas().stream()
                    .anyMatch(asignatura -> asignatura.getMateria().getMateriaId() == correlativaId
                            && asignatura.getEstado() == EstadoAsignatura.APROBADA);
            if (!correlativaAprobada) {
                throw new EstadoIncorrectoException(
                        "Debe aprobar todas las materias correlativas antes de aprobar esta asignatura.");
            }
        }
    }
}
