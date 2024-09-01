package com.lab3_final.lab3_final.business.implementation;

import com.lab3_final.lab3_final.business.MateriaService;
import com.lab3_final.lab3_final.dto.MateriaDto;
import com.lab3_final.lab3_final.model.Materia;
import com.lab3_final.lab3_final.model.Profesor;
import com.lab3_final.lab3_final.persistence.MateriaDao;
import com.lab3_final.lab3_final.persistence.ProfesorDao;
import com.lab3_final.lab3_final.persistence.exception.MateriaAlreadyExistsException;
import com.lab3_final.lab3_final.persistence.exception.MateriaNotFoundException;
import com.lab3_final.lab3_final.persistence.exception.ProfesorNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaDao materiaDao;
    private final ProfesorDao profesorDao;

    public MateriaServiceImpl(MateriaDao materiaDao, ProfesorDao profesorDao) {
        this.materiaDao = materiaDao;
        this.profesorDao = profesorDao;
    }

    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws MateriaAlreadyExistsException, ProfesorNotFoundException {
        Profesor profesor = profesorDao.findProfesorById(materiaDto.getProfesorId());
        Materia materia = new Materia(materiaDto.getNombre(), materiaDto.getAnio(), materiaDto.getCuatrimestre(),
                profesor);
        profesor.getMateriasDictadas().add(materia);
        return materiaDao.saveMateria(materia);
    }

    @Override
    public Materia obtenerMateriaPorId(int idMateria) throws MateriaNotFoundException {
        return materiaDao.findMateriaById(idMateria);
    }

    @Override
    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.findAllMaterias();
    }

    @Override
    public void eliminarMateria(int idMateria) throws MateriaNotFoundException {
        materiaDao.deleteMateriaById(idMateria);
    }
}
