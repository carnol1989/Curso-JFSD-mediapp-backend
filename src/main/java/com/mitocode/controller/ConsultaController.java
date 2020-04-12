package com.mitocode.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.dto.ConsultaDTO;
import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Archivo;
import com.mitocode.model.Consulta;
import com.mitocode.service.IArchivoService;
import com.mitocode.service.IConsultaService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private IConsultaService service;
	
	@Autowired
	private IArchivoService archivoService;
	
	@GetMapping
	public ResponseEntity<List<Consulta>> listarController() {
		List<Consulta> lista = service.listarService();
		return new ResponseEntity<List<Consulta>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Consulta> listarPorIdController(@PathVariable("id") Integer id) {
		Consulta obj = service.leerPorIdService(id);
		if (obj.getIdConsulta() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		return new ResponseEntity<Consulta>(obj, HttpStatus.OK);
	}
	
	@GetMapping(value = "/hateoas", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConsultaDTO> listarHateoas() {
		List<Consulta> consultas = new ArrayList<Consulta>();
		List<ConsultaDTO> consultasDTO = new ArrayList<ConsultaDTO>();
		consultas = service.listarService();
		
		for (Consulta c : consultas) {
			ConsultaDTO d = new ConsultaDTO();
			d.setIdConsulta(c.getIdConsulta());
			d.setMedico(c.getMedico());
			d.setPaciente(c.getPaciente());
			
			//localhost:8080/consultas/1
			ControllerLinkBuilder linkTo = linkTo(methodOn(ConsultaController.class).listarPorIdController(c.getIdConsulta()));
			d.add(linkTo.withSelfRel());
			consultasDTO.add(d);
			
			ControllerLinkBuilder linkTo2 = linkTo(methodOn(PacienteController.class).listarPorIdController(c.getPaciente().getIdPaciente()));
			d.add(linkTo2.withSelfRel());
			consultasDTO.add(d);
			
			ControllerLinkBuilder linkTo3 = linkTo(methodOn(MedicoController.class).listarPorIdController(c.getMedico().getIdMedico()));
			d.add(linkTo3.withSelfRel());			
			consultasDTO.add(d);
		}
		
		return consultasDTO;
	}
	
	//Nivel 3
	/*@GetMapping("/{id}")
	public Resource<Consulta> listarPorIdController(@PathVariable("id") Integer id) {
		Consulta obj = service.leerPorIdService(id);
		if (obj.getIdConsulta() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		
		//Hay que armar la ruta
		//localhost:8080/consultas/{id}
		Resource<Consulta> recurso = new Resource<Consulta>(obj);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorIdController(id));
		recurso.add(linkTo.withRel("consulta-resource"));
		
		return recurso;
	}*/
	
	//Nivel 1
	//@Valid para Spring Boot 1.5 para que respete los constraints	
	/*@PostMapping
	public ResponseEntity<Consulta> registrarController(@Valid @RequestBody Consulta objReq) {
		Consulta obj = service.registrarService(objReq);
		return new ResponseEntity<Consulta>(obj, HttpStatus.CREATED);
	}*/
	
	//Nivel 2
	@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody ConsultaListaExamenDTO objDtoReq) {
		Consulta obj = service.registrarTransaccionalService(objDtoReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	/*@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody Consulta objReq) {
		Consulta obj = service.registrarService(objReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}*/
	
	@PutMapping
	public ResponseEntity<Consulta> modificarController(@RequestBody Consulta objReq) {
		Consulta obj = service.modificarService(objReq);
		return new ResponseEntity<Consulta>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminarController(@PathVariable("id") Integer id) {
		Consulta obj = service.leerPorIdService(id);
		if (obj.getIdConsulta() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		service.eliminarService(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/buscar")
	public ResponseEntity<List<Consulta>> buscarController(@RequestBody FiltroConsultaDTO filtro) {
		List<Consulta> consultas = new ArrayList<>();
		if (filtro != null) {
			if (filtro.getFechaConsulta() != null) {
				consultas = service.buscarFechaService(filtro);
			} else {
				consultas = service.buscarService(filtro);
			}
		}
		return new ResponseEntity<List<Consulta>>(consultas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/listarResumen")
	public ResponseEntity<List<ConsultaResumenDTO>> listarResumen() {
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		consultas = service.listarResumenService();
		return new ResponseEntity<List<ConsultaResumenDTO>>(consultas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/generarReporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporteController() {
		byte[] data = null;
		data = service.generarReporteService();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	@PostMapping(value = "/guardarArchivo", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Integer> guardarArchivoController(@RequestParam("adjunto") MultipartFile file) throws IOException {
		int rpta = 0;
		
		Archivo ar = new Archivo();
		ar.setFiletype(file.getContentType());
		ar.setFilename(file.getName());
		ar.setValue(file.getBytes());
		
		rpta = archivoService.guardarArchivoService(ar);
		
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	@GetMapping(value = "/leerArchivo/{idArchivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> leerArchivo(@PathVariable("idArchivo") Integer idArchivo) {
		byte[] arr = archivoService.leerArchivoService(idArchivo);
		return new ResponseEntity<byte[]>(arr, HttpStatus.OK);
	}
	
}
