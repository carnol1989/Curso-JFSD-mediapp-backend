package com.mitocode.service;

import java.util.List;

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;

public interface IConsultaService extends ICRUD<Consulta> {

	Consulta registrarTransaccionalService(ConsultaListaExamenDTO objDto);
	
	List<Consulta> buscarService(FiltroConsultaDTO filtro);
	
	List<Consulta> buscarFechaService(FiltroConsultaDTO filtro);
	
	List<ConsultaResumenDTO> listarResumenService();
	
	byte[] generarReporteService();
	
}
