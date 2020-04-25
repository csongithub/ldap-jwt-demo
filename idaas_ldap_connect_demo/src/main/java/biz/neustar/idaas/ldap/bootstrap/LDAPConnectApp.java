package biz.neustar.idaas.ldap.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableConfigurationProperties
@EntityScan("biz.neustar.idaas.ldap.domain")
@EnableJpaRepositories("biz.neustar.idaas.ldap.repository")
@ComponentScan({"biz.neustar.idaas.ldap"})
public class LDAPConnectApp {

	public static void main(String[] args) {
		SpringApplication.run(LDAPConnectApp.class, args);
	}
}
