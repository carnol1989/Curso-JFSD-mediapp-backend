package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Paciente;
import com.mitocode.repo.IPacienteRepo;
import com.mitocode.service.IPacienteService;

@Service
public class PacienteServiceImpl implements IPacienteService {

	@Autowired
	private IPacienteRepo repo;
	
	@Override
	public Paciente registrarService(Paciente obj) {
		return repo.save(obj);
	}

	@Override
	public Paciente modificarService(Paciente obj) {
		return repo.save(obj);
	}

	@Override
	public List<Paciente> listarService() {
		return repo.findAll();
	}

	@Override
	public Paciente leerPorIdService(Integer id) {
		Optional<Paciente> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Paciente();
	}

	@Override
	public boolean eliminarService(Integer id) {
		repo.deleteById(id);
		return true;
	}
	
}
