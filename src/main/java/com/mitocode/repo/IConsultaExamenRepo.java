package com.mitocode.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;

import com.mitocode.model.ConsultaExamen;

public interface IConsultaExamenRepo extends JpaRepository<ConsultaExamen, Integer> {

//	@Transactional //Puede generar data basura, es mejor llevarlo a un método superior.
	@Modifying
	@Query(value = "INSERT INTO consulta_examen(id_consulta, id_examen) VALUES (:idConsulta, :idExamen)", nativeQuery = true)
	Integer registrarRepo(@Param("idConsulta") Integer idConsulta, @Param("idExamen") Integer idExamen);
	
	@Query("FROM ConsultaExamen ce WHERE ce.consulta.idConsulta = :idConsulta")
	List<ConsultaExamen> lisarExamenesPorConsultaRepo(@Param("idConsulta") Integer idConsulta);
	
}
