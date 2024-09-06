package com.lab3_final.lab3_final.business.implementation;

import com.lab3_final.lab3_final.business.MateriaService;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.CircularDependencyException;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaDao materiaDao;
    private final ProfesorDao profesorDao;

    public MateriaServiceImpl(MateriaDao materiaDao, ProfesorDao profesorDao) {
        this.materiaDao = materiaDao;
        this.profesorDao = profesorDao;
    }

    @Override
    public Materia crearMateria(MateriaDto materiaDto)
            throws ProfesorNotFoundException, MateriaAlreadyExistsException, MateriaNotFoundException,
            CircularDependencyException {
        // Verificar si ya existe una materia con el mismo nombre, año y cuatrimestre
        if (materiaDao.existsByNombreAndAnioAndCuatrimestreAndProfesor(materiaDto.getNombre(), materiaDto.getAnio(),
                materiaDto.getCuatrimestre(), materiaDto.getProfesorId())) {
            throw new MateriaAlreadyExistsException("La materia ya existe.");
        }

        // Buscar al profesor
        Profesor profesor = profesorDao.findProfesorById(materiaDto.getProfesorId());
        if (profesor == null) {
            throw new ProfesorNotFoundException("Profesor no encontrado.");
        }

        // Crear la materia
        Materia materia = convertirMateriaDtoAMateria(materiaDto);

        // Establecer las correlatividades
        List<Integer> correlatividades = new ArrayList<>();
        for (int correlativaId : materiaDto.getCorrelatividades()) {
            Materia correlativa = materiaDao.findMateriaById(correlativaId);
            if (correlativa == null) {
                throw new MateriaNotFoundException("Correlativa no encontrada.");
            }
            if (esCorrelativaCircular(materia, correlativa)) {
                throw new CircularDependencyException("La correlatividad entre " + materia.getNombre() + " y "
                        + correlativa.getNombre() + " es circular. No pueden ser correlativas una de otra.");
            }
            correlatividades.add(correlativaId);
        }
        materia.setCorrelatividades(correlatividades);

        // Guardar la materia
        materia = materiaDao.saveMateria(materia);

        // Asociar la materia al profesor
        profesor.getMateriasDictadas().add(materia);
        profesorDao.updateProfesor(profesor);

        return materia;
    }

    @Override
    public Materia obtenerMateriaPorId(int idMateria) throws MateriaNotFoundException {
        Materia materia = materiaDao.findMateriaById(idMateria);
        if (materia == null) {
            throw new MateriaNotFoundException("Materia no encontrada.");
        }
        return materia;
    }

    @Override
    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.findAllMaterias();
    }

    @Override
    public void eliminarMateria(int idMateria) throws MateriaNotFoundException {
        if (!materiaDao.existsById(idMateria)) {
            throw new MateriaNotFoundException("Materia no encontrada.");
        }
        materiaDao.deleteMateriaById(idMateria);
    }

    private Materia convertirMateriaDtoAMateria(MateriaDto materiaDto) {
        return new Materia(
                materiaDto.getMateriaId(),
                materiaDto.getNombre(),
                materiaDto.getAnio(),
                materiaDto.getCuatrimestre(),
                materiaDto.getProfesorId());
    }

    // Método auxiliar para verificar bucle de correlatividades
    private boolean esCorrelativaCircular(Materia materia, Materia correlativa) throws MateriaNotFoundException {
        if (correlativa.getCorrelatividades().contains(materia.getMateriaId())) {
            return true;
        }
        for (int subCorrelativaId : correlativa.getCorrelatividades()) {
            Materia subCorrelativa = materiaDao.findMateriaById(subCorrelativaId);
            if (esCorrelativaCircular(materia, subCorrelativa)) {
                return true;
            }
        }
        return false;
    }
}
