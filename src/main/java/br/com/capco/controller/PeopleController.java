package br.com.capco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.capco.model.entity.User;
import br.com.capco.model.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Serviços disponíveis para operações com usuários.")
@RequestMapping("/user")
public class PeopleController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("")
	@Secured(value = { "ROLE_ADMIN" })
	@ApiOperation(value = "Criar um endpoint que retorne todos os usuários e perfis salvos\n"
			+ "no banco de dados. Este endpoint só poderá ser acessado pelo\n" + "administrador.")
	public ResponseEntity<List<User>> findAll() {
		return ResponseEntity.ok(this.getUserRepository().findAll());
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
