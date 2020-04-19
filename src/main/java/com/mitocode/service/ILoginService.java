package com.mitocode.service;

import com.mitocode.model.Usuario;

public interface ILoginService {

	Usuario verificarNombreUsuarioService(String usuario) throws Exception;
	
	int cambiarClaveService(String clave, String nombre) throws Exception;
	
}
