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
import com.mitocode.model.Examen;
import com.mitocode.service.IExamenService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/examenes")
public class ExamenController {

	@Autowired
	private IExamenService service;
	
	@GetMapping
	public ResponseEntity<List<Examen>> listarController() {
		List<Examen> lista = service.listarService();
		return new ResponseEntity<List<Examen>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Examen> listarPorIdController(@PathVariable("id") Integer id) {
		Examen obj = service.leerPorIdService(id);
		if (obj.getIdExamen() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		return new ResponseEntity<Examen>(obj, HttpStatus.OK);
	}
	
	//Nivel 3
	/*@GetMapping("/{id}")
	public Resource<Examen> listarPorIdController(@PathVariable("id") Integer id) {
		Examen obj = service.leerPorIdService(id);
		if (obj.getIdExamen() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		
		//Hay que armar la ruta
		//localhost:8080/examens/{id}
		Resource<Examen> recurso = new Resource<Examen>(obj);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorIdController(id));
		recurso.add(linkTo.withRel("examen-resource"));
		
		return recurso;
	}*/
	
	//Nivel 1
	//@Valid para Spring Boot 1.5 para que respete los constraints	
	/*@PostMapping
	public ResponseEntity<Examen> registrarController(@Valid @RequestBody Examen objReq) {
		Examen obj = service.registrarService(objReq);
		return new ResponseEntity<Examen>(obj, HttpStatus.CREATED);
	}*/
	
	//Nivel 2
	@PostMapping
	public ResponseEntity<Object> registrarController(@Valid @RequestBody Examen objReq) {
		Examen obj = service.registrarService(objReq);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExamen()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Examen> modificarController(@RequestBody Examen objReq) {
		Examen obj = service.modificarService(objReq);
		return new ResponseEntity<Examen>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminarController(@PathVariable("id") Integer id) {
		Examen obj = service.leerPorIdService(id);
		if (obj.getIdExamen() == null) {
			throw new ModeloNotFoundException("ID no encontrado " + id);
		}
		service.eliminarService(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
