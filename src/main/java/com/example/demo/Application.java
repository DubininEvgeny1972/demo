package com.example.demo;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
public class Application extends SpringBootServletInitializer {

	private static RestTemplate restTemplate;
	private static HttpHeaders httpHeaders;

	@Autowired
	public Application(RestTemplate restTemplate, HttpHeaders httpHeaders) {
		this.restTemplate = restTemplate;
		this.httpHeaders = httpHeaders;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		String preCookie = getAllUser().toString();
		String preCookie1 = preCookie.substring(preCookie.indexOf("Set-Cookie")+12);
		String cookie = preCookie1.substring(0, preCookie1.indexOf(",")-1);
		httpHeaders.set("Cookie", cookie);
		System.out.println(addUser(new User(3L, "James", "Brown", (byte) 50)).getBody()
				+ editUser(new User(3L, "Thomas", "Shelby", (byte) 51)).getBody()
				+ deleteUser(3).getBody());

	}

	public static ResponseEntity<List<User>> getAllUser() {
		ResponseEntity<List<User>> responseEntity = restTemplate.exchange(
				"http://91.241.64.178:7081/api/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
		return responseEntity;
	}

	public static ResponseEntity<String> addUser(User user) {
		HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);
		return restTemplate.postForEntity("http://91.241.64.178:7081/api/users", entity, String.class);
	}

	public static ResponseEntity<String> editUser (User user) {
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);
		return restTemplate.exchange("http://91.241.64.178:7081//api/users", HttpMethod.PUT, entity, String.class, user.getId());
	}

	public static ResponseEntity<String> deleteUser(long id) {
		HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
		return restTemplate.exchange("http://91.241.64.178:7081//api/users/{id}", HttpMethod.DELETE, entity, String.class, id);
	}

}
