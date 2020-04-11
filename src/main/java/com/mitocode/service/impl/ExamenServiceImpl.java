package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Examen;
import com.mitocode.repo.IExamenRepo;
import com.mitocode.service.IExamenService;

@Service
public class ExamenServiceImpl implements IExamenService {

	@Autowired
	private IExamenRepo repo;
	
	@Override
	public Examen registrarService(Examen obj) {
		return repo.save(obj);
	}

	@Override
	public Examen modificarService(Examen obj) {
		return repo.save(obj);
	}

	@Override
	public List<Examen> listarService() {
		return repo.findAll();
	}

	@Override
	public Examen leerPorIdService(Integer id) {
		Optional<Examen> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Examen();
	}

	@Override
	public boolean eliminarService(Integer id) {
		repo.deleteById(id);
		return true;
	}
	
}
