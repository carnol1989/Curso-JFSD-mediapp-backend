package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Especialidad;
import com.mitocode.repo.IEspecialidadRepo;
import com.mitocode.service.IEspecialidadService;

@Service
public class EspecialidadServiceImpl implements IEspecialidadService {

	@Autowired
	private IEspecialidadRepo repo;
	
	@Override
	public Especialidad registrarService(Especialidad obj) {
		return repo.save(obj);
	}

	@Override
	public Especialidad modificarService(Especialidad obj) {
		return repo.save(obj);
	}

	@Override
	public List<Especialidad> listarService() {
		return repo.findAll();
	}

	@Override
	public Especialidad leerPorIdService(Integer id) {
		Optional<Especialidad> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Especialidad();
	}

	@Override
	public boolean eliminarService(Integer id) {
		repo.deleteById(id);
		return true;
	}
	
}
