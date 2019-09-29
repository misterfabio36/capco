package br.com.capco.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.capco.model.dto.People;
import br.com.capco.repository.PeopleRepositoryFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="Serviços disponíveis para operações com personagens.")
@RequestMapping("/people")
public class UserController {
	@Autowired
	private PeopleRepositoryFacade peopleRepositoryFacade;
	
	@GetMapping("list")
	@Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
	@ApiOperation(value = "Obter todos os personagens e ordená-los por quantidade de filmes\n" + 
			"que aparecem, usando nome do personagem como segundo\n" + 
			"critério de ordenação (ordem alfabética). Este endpoint poderá ser\n" + 
			"acessado pelo perfil de usuário e administrador.")
    public ResponseEntity<List<People>> findAll() {
		List<People> peopleList = peopleRepositoryFacade.list();
		
		Collections.sort(peopleList, Comparator.comparing(People::getFilmsTotal)
	            .thenComparing(People::getName));
		
        return ResponseEntity.ok(peopleList);
    }
	
	@GetMapping("/{id}")
	@Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
	@ApiOperation(value = "Obter um personagem por ID e trazer nome do personagem, ano\n" + 
			"de nascimento e nome dos filmes que está. Este endpoint poderá\n" + 
			"ser acessado pelo perfil de usuário e administrador.")
    public ResponseEntity<People> getById(@PathVariable Long id) {
        return ResponseEntity.ok(peopleRepositoryFacade.getById(id));
    }
	
	@GetMapping("/human")
	@Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
	@ApiOperation(value = "Obter a lista de todos os personagens humanos e calcular a média\n" + 
			"de peso dentre eles. Este endpoint poderá ser acessado pelo perfil\n" + 
			"de usuário e administrador.")
    public Map<String, Double> getHuman() {
		 List<People> humans = peopleRepositoryFacade.list().stream()
	                .filter(line -> "male".equals(line.getGender()) || "female".equals(line.getGender()))
	                .collect(Collectors.toList());   
		 Double total = 0D;
		for (People people : humans) {
			if(!"unknown".equals(people.getHeight()))
				total += Double.parseDouble(people.getHeight());
		}
		
		Map<String, Double> response = new HashMap<String, Double>();
		response.put("media", (total / humans.size()));
        return response;
    }
}
