package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mitocode.model.Signo;
import com.mitocode.repo.ISignoRepo;
import com.mitocode.service.ISignoService;

@Service
public class SignoServiceImpl implements ISignoService {

	@Autowired
	private ISignoRepo repo;
	
	@Override
	public Signo registrarService(Signo obj) {
		return repo.save(obj);
	}

	@Override
	public Signo modificarService(Signo obj) {
		return repo.save(obj);
	}

	@Override
	public List<Signo> listarService() {
		return repo.findAll();
	}
	
	@Override
	public Page<Signo> listarPageableService(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Signo leerPorIdService(Integer id) {
		Optional<Signo> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Signo();
	}

	@Override
	public boolean eliminarService(Integer id) {
		repo.deleteById(id);
		return true;
	}

}
