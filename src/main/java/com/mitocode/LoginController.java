package com.mitocode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.model.ResetToken;
import com.mitocode.model.Usuario;
import com.mitocode.service.ILoginService;
import com.mitocode.service.IResetTokenService;
import com.mitocode.util.EmailUtil;
import com.mitocode.util.Mail;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private IResetTokenService tokenService;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private ILoginService service;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@PostMapping(value = "/enviarCorreo", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Integer> enviarCorreoController(@RequestBody String correo){
		int rpta = 0;

		try {
			Usuario us = service.verificarNombreUsuarioService(correo);
			
			if (us != null && us.getIdUsuario() > 0) {
				ResetToken token = new ResetToken();
				token.setToken(UUID.randomUUID().toString());
				token.setUser(us);
				token.setExpiracion(5);
				tokenService.guardarService(token);
				
				Mail mail = new Mail();
				mail.setFrom("jdfs01.cano@gmail.com");
				mail.setTo(us.getUsername());
				mail.setSubject("RESTABLECER CONTRASENIA - MEDIAPP");
				
				Map<String, Object> model = new HashMap<>();
				String url = "http://localhost:4200/recuperar/" + token.getToken();
				model.put("user", token.getUser().getUsername());
				model.put("resetUrl", url);
				mail.setModel(model);
//				System.out.println(model.get("user"));
//				System.out.println(model.get("resetUrl"));
				emailUtil.enviarMail(mail);
				rpta = 1;
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	@GetMapping(value = "/restablecer/verificar/{token}")
	public ResponseEntity<Integer> restablecerVerificarController(@PathVariable("token") String token) {
		int rpta = 0;
		try {
			if (token != null && !token.isEmpty()) {
				ResetToken resetToken = tokenService.findByTokenService(token);
				if (resetToken != null && resetToken.getId() > 0) {
					if (!resetToken.estaExpirado()) {
						rpta = 1;
					}
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	@PostMapping(value = "/restablecer/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> restablecerClaveController(@PathVariable("token") String token, 
			@RequestBody String clave) {
		int rpta = 0;
		try {
			ResetToken resetToken = tokenService.findByTokenService(token);
			String claveHash = bcrypt.encode(clave);
			rpta = service.cambiarClaveService(claveHash, resetToken.getUser().getUsername());
			tokenService.eliminarService(resetToken);
		} catch (Exception e) {
			return new ResponseEntity<Integer>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
}
