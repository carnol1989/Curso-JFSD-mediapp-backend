package com.mitocode.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Paciente;
import com.mitocode.service.IPacienteService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private IPacienteService service;
	
	@GetMapping
	public ResponseEntity<List<Paciente>> listarController() {
		List<Paciente> lista = service.listarService();
		return new ResponseEntity<List<Paciente>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Paciente> listarPorIdController(@PathVariable("id") Integer id) {
		Paciente obj = service.leerPorIdService(id);
		if (obj.getIdPaciente() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		return new ResponseEntity<Paciente>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/pageable")
	public ResponseEntity<Page<Paciente>> listarPageableController(Pageable pageable){
		Page<Paciente> pacientes = service.listarPageableService(pageable);
		return new ResponseEntity<Page<Paciente>>(pacientes, HttpStatus.OK);
	}
	
	//Nivel 3
	/*@GetMapping("/{id}")
	public Resource<Paciente> listarPorIdController(@PathVariable("id") Integer id) {
		Paciente obj = service.leerPorIdService(id);
		if (obj.getIdPaciente() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		
		//Hay que armar la ruta
		//localhost:8080/pacientes/{id}
		Resource<Paciente> recurso = new Resource<Paciente>(obj);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorIdController(id));
		recurso.add(linkTo.withRel("paciente-resource"));
		
		return recurso;
	}*/
	
	//Nivel 1
	//@Valid para Spring Boot 1.5 para que respete los constraints	
	/*@PostMapping
	public ResponseEntity<Paciente> registrarController(@Valid @RequestBody Paciente objReq) {
		Paciente obj = service.registrarService(objReq);
		return new ResponseEntity<Paciente>(obj, HttpStatus.CREATED);
	}*/
	
	//Nivel 2
	@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody Paciente objReq) {
		Paciente obj = service.registrarService(objReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPaciente()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Paciente> modificarController(@RequestBody Paciente objReq) {
		Paciente obj = service.modificarService(objReq);
		return new ResponseEntity<Paciente>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminarController(@PathVariable("id") Integer id) {
		Paciente obj = service.leerPorIdService(id);
		if (obj.getIdPaciente() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		service.eliminarService(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
