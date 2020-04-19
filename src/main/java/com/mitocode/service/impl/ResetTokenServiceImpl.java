package com.mitocode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.ResetToken;
import com.mitocode.repo.IResetTokenRepo;
import com.mitocode.service.IResetTokenService;

@Service
public class ResetTokenServiceImpl implements IResetTokenService {

	@Autowired
	private IResetTokenRepo repo;
	
	@Override
	public ResetToken findByTokenService(String token) {
		return repo.findByToken(token);
	}

	@Override
	public void guardarService(ResetToken token) {
		repo.save(token);
	}

	@Override
	public void eliminarService(ResetToken token) {
		repo.delete(token);
	}

}
