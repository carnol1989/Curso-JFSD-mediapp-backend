package com.mitocode.repo;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.mitocode.model.Paciente;

//@Repository //No es necesario porque ya se está usando JpaRepository
public interface IPacienteRepo extends JpaRepository<Paciente, Integer> {

}
