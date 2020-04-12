package com.mitocode.service;

import com.mitocode.model.Archivo;

public interface IArchivoService {

	int guardarArchivoService(Archivo archivo);
	
	byte[] leerArchivoService(Integer idArchivo);
	
}
