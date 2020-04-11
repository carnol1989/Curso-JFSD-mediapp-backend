package com.mitocode.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mitocode.model.Medico;
import com.mitocode.service.IMedicoService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private IMedicoService service;
	
	@GetMapping
	public ResponseEntity<List<Medico>> listarController() {
		List<Medico> lista = service.listarService();
		return new ResponseEntity<List<Medico>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Medico> listarPorIdController(@PathVariable("id") Integer id) {
		Medico obj = service.leerPorIdService(id);
		if (obj.getIdMedico() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		return new ResponseEntity<Medico>(obj, HttpStatus.OK);
	}
	
	//Nivel 3
	/*@GetMapping("/{id}")
	public Resource<Medico> listarPorIdController(@PathVariable("id") Integer id) {
		Medico obj = service.leerPorIdService(id);
		if (obj.getIdMedico() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		
		//Hay que armar la ruta
		//localhost:8080/medicos/{id}
		Resource<Medico> recurso = new Resource<Medico>(obj);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorIdController(id));
		recurso.add(linkTo.withRel("medico-resource"));
		
		return recurso;
	}*/
	
	//Nivel 1
	//@Valid para Spring Boot 1.5 para que respete los constraints	
	/*@PostMapping
	public ResponseEntity<Medico> registrarController(@Valid @RequestBody Medico objReq) {
		Medico obj = service.registrarService(objReq);
		return new ResponseEntity<Medico>(obj, HttpStatus.CREATED);
	}*/
	
	//Nivel 2
	@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody Medico objReq) {
		Medico obj = service.registrarService(objReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMedico()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Medico> modificarController(@RequestBody Medico objReq) {
		Medico obj = service.modificarService(objReq);
		return new ResponseEntity<Medico>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminarController(@PathVariable("id") Integer id) {
		Medico obj = service.leerPorIdService(id);
		if (obj.getIdMedico() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		service.eliminarService(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
