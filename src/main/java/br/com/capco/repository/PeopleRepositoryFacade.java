package br.com.capco.repository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.capco.model.dto.People;
import br.com.capco.model.dto.PeopleResponse;

@Service
public class PeopleRepositoryFacade {
	private static final String baseUrl = "https://swapi.co/api/people/";
	private List<People> peopleList;

	public List<People> list() {
		if(this.peopleList == null) {
			RestTemplate restTemplate = new RestTemplate();
			List<People> resultList = new LinkedList<People>();
	
	        ResponseEntity<PeopleResponse> reponse = getResult(restTemplate, buildHeader(), baseUrl);
	        resultList.addAll(Arrays.asList(reponse.getBody().getResults()));       
	        
	        Integer pages = reponse.getBody().getCount() / 10;
	        for(int i = 2 ; i <= pages ; i++) {
	        	resultList.addAll(Arrays.asList(getResult(restTemplate, buildHeader(), baseUrl + "?page=" + i).getBody().getResults()));
	        }
	        
	        this.peopleList = resultList;
		}
		
        return this.peopleList;
	}

	private HttpHeaders buildHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("User-Agent", "firefox"); 
		return headers;
	}

	public People getById(Long id) {
		RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(baseUrl + id, HttpMethod.GET, new HttpEntity(buildHeader()), People.class).getBody();
	}
	
	private ResponseEntity<PeopleResponse> getResult(RestTemplate restTemplate, HttpHeaders headers, String url) {
		return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(headers), PeopleResponse.class);
	}
	
	
	
}
