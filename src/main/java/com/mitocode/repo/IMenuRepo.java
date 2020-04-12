package com.mitocode.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Menu;

public interface IMenuRepo extends JpaRepository<Menu, Integer> {

	@Query(value = "SELECT m.* FROM menu_rol mr INNER JOIN usuario_rol ur ON ur.id_rol = mr.id_rol INNER JOIN menu m ON m.id_menu = mr.id_menu INNER JOIN usuario u ON u.id_usuario = ur.id_usuario WHERE u.nombre = :nombre", nativeQuery = true)
	List<Object[]> listarMenuPorUsuario(@Param("nombre") String nombre);
	
}
