package com.mitocode.service;

import com.mitocode.model.ResetToken;

public interface IResetTokenService {

	ResetToken findByTokenService(String token);
	
	void guardarService(ResetToken token);
	
	void eliminarService(ResetToken token);
	
}
