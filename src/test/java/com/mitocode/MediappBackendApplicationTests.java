package com.mitocode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.mitocode.model.Usuario;
import com.mitocode.repo.IUsuarioRepo;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MediappBackendApplicationTests {

	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private IUsuarioRepo repo;
	
	@Test
	public void crearUsuario() {
		Usuario usuarioNuevo = new Usuario();
		/*usuarioNuevo.setIdUsuario(1);
		usuarioNuevo.setUsername("carnol1989@hotmail.com");
		usuarioNuevo.setPassword(bcrypt.encode("123"));
		usuarioNuevo.setEnabled(true);*/
		
		usuarioNuevo.setIdUsuario(2);
		usuarioNuevo.setUsername("carnole1989@gamil.com");
		usuarioNuevo.setPassword(bcrypt.encode("123"));
		usuarioNuevo.setEnabled(true);
		
		Usuario usuarioRetorno = repo.save(usuarioNuevo);
		
		assertTrue(usuarioRetorno.getPassword().equalsIgnoreCase(usuarioNuevo.getPassword()));
	}

}
