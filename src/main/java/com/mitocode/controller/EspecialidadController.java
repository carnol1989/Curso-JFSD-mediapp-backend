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
import com.mitocode.model.Especialidad;
import com.mitocode.service.IEspecialidadService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {

	@Autowired
	private IEspecialidadService service;
	
	@GetMapping
	public ResponseEntity<List<Especialidad>> listarController() {
		List<Especialidad> lista = service.listarService();
		return new ResponseEntity<List<Especialidad>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Especialidad> listarPorIdController(@PathVariable("id") Integer id) {
		Especialidad obj = service.leerPorIdService(id);
		if (obj.getIdEspecialidad() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		return new ResponseEntity<Especialidad>(obj, HttpStatus.OK);
	}
	
	//Nivel 3
	/*@GetMapping("/{id}")
	public Resource<Especialidad> listarPorIdController(@PathVariable("id") Integer id) {
		Especialidad obj = service.leerPorIdService(id);
		if (obj.getIdEspecialidad() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		
		//Hay que armar la ruta
		//localhost:8080/especialidads/{id}
		Resource<Especialidad> recurso = new Resource<Especialidad>(obj);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorIdController(id));
		recurso.add(linkTo.withRel("especialidad-resource"));
		
		return recurso;
	}*/
	
	//Nivel 1
	//@Valid para Spring Boot 1.5 para que respete los constraints	
	/*@PostMapping
	public ResponseEntity<Especialidad> registrarController(@Valid @RequestBody Especialidad objReq) {
		Especialidad obj = service.registrarService(objReq);
		return new ResponseEntity<Especialidad>(obj, HttpStatus.CREATED);
	}*/
	
	//Nivel 2
	@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody Especialidad objReq) {
		Especialidad obj = service.registrarService(objReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdEspecialidad()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Especialidad> modificarController(@RequestBody Especialidad objReq) {
		Especialidad obj = service.modificarService(objReq);
		return new ResponseEntity<Especialidad>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminarController(@PathVariable("id") Integer id) {
		Especialidad obj = service.leerPorIdService(id);
		if (obj.getIdEspecialidad() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		service.eliminarService(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
