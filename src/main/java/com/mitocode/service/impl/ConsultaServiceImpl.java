package com.mitocode.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;
import com.mitocode.repo.IConsultaExamenRepo;
import com.mitocode.repo.IConsultaRepo;
import com.mitocode.service.IConsultaService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConsultaServiceImpl implements IConsultaService {

	@Autowired
	private IConsultaRepo repo;
	
	@Autowired
	private IConsultaExamenRepo ceRepo;
	
	@Override
	public Consulta registrarService(Consulta obj) {
		obj.getDetalleConsulta().forEach(det -> {
			det.setConsulta(obj);
		});
		return repo.save(obj);
	}
	
	@Transactional
	@Override
	public Consulta registrarTransaccionalService(ConsultaListaExamenDTO objDto) {
		objDto.getConsulta().getDetalleConsulta().forEach(det -> {
			det.setConsulta(objDto.getConsulta());
		});
		repo.save(objDto.getConsulta());		
		
		objDto.getLstExamen().forEach(ex -> ceRepo.registrarRepo(objDto.getConsulta().getIdConsulta(), ex.getIdExamen()));
		
		return objDto.getConsulta();
	}

	@Override
	public Consulta modificarService(Consulta obj) {
		return repo.save(obj);
	}

	@Override
	public List<Consulta> listarService() {
		return repo.findAll();
	}

	@Override
	public Consulta leerPorIdService(Integer id) {
		Optional<Consulta> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Consulta();
	}

	@Override
	public boolean eliminarService(Integer id) {
		repo.deleteById(id);
		return true;
	}

	@Override
	public List<Consulta> buscarService(FiltroConsultaDTO filtro) {
		return repo.buscarRepo(filtro.getDni(), filtro.getNombreCompleto());
	}

	@Override
	public List<Consulta> buscarFechaService(FiltroConsultaDTO filtro) {
		LocalDateTime fechaSgte = filtro.getFechaConsulta().plusDays(1);		
		return repo.buscarFechaRepo(filtro.getFechaConsulta(), fechaSgte);
	}

	@Override
	public List<ConsultaResumenDTO> listarResumenService() {
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		repo.listarResumenRepo().forEach(x -> {
			ConsultaResumenDTO cr = new ConsultaResumenDTO();
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consultas.add(cr);
		});
		return consultas;
	}

	@Override
	public byte[] generarReporteService() {
		byte[] data = null;
		try {
			//Los parámetros se envía mediante un HasMap
			//HasMap<String, String> params = new HasMap<String, String>();
			//params.put("txt_empresa", "MitoCode Network");
			
			File file = new ClassPathResource("/reports/consultas.jasper").getFile();
			//2do parámetro es variables que necesite el reporte
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), null, 
					new JRBeanCollectionDataSource(this.listarResumenService()));
			data = JasperExportManager.exportReportToPdf(print);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
}
