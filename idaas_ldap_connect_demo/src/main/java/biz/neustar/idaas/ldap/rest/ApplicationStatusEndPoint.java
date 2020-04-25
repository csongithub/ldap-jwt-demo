package biz.neustar.idaas.ldap.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApplicationStatusEndPoint {
	
	@GetMapping
	public String status() {
		return "Application is running....";
	}
	
	
	@GetMapping("/testSecurity")
	public String testSecurity() {
		return "This is secured API";
	}
}
