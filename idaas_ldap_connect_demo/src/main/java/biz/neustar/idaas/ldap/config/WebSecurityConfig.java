/**
 * 
 */
package biz.neustar.idaas.ldap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import biz.neustar.idaas.ldap.jwt.JwtAuthenticationEntryPoint;
import biz.neustar.idaas.ldap.jwt.JwtRequestFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class extends the WebSecurityConfigurerAdapter. 
 * This is a convenience class that allows customization to both WebSecurity and HttpSecurity.
 * 
 * @author chandan singh
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String LOG_IN_END_POINT_ENTRY = "/api/login";
	public static final String APPLICATION_STATUS_ENTRY = "/";
	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
	
	@Value("${jwt.skip.security.pattern}")
	private String skipSecurityPattern;
	
	

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		try {
			String[] skipSecurityFilters = skipSecurityPattern.split(",");
//			String[] enableSecurityFilters = enableSecurityPattern.split(",");
			
			httpSecurity.csrf().disable()
			// dont authenticate this particular request
			.authorizeRequests().antMatchers(skipSecurityFilters).permitAll()
			// all other requests need to be authenticated
			.anyRequest().authenticated()
			// make sure we use stateless session; session won't be used to store user's state.
			.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			// Add a filter to validate the tokens with every request
			httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);;
		} catch (Exception e) {
			log.error("Error while Authenticating User {} ", e.getMessage());
		}
	}
	
	@SuppressWarnings({ "all" })
	@Bean
	public FilterRegistrationBean platformCorsFilter() {
	    org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

	    CorsConfiguration configAutenticacao = new CorsConfiguration();
	    configAutenticacao.setAllowCredentials(true);
	    configAutenticacao.addAllowedOrigin("*");
	    configAutenticacao.addAllowedHeader("Authorization");
	    configAutenticacao.addAllowedHeader("Content-Type");
	    configAutenticacao.addAllowedHeader("Accept");
	    configAutenticacao.addAllowedMethod("POST");
	    configAutenticacao.addAllowedMethod("GET");
	    configAutenticacao.addAllowedMethod("DELETE");
	    configAutenticacao.addAllowedMethod("PUT");
	    configAutenticacao.addAllowedMethod("OPTIONS");
	    configAutenticacao.setMaxAge(3600L);
	    source.registerCorsConfiguration("/**", configAutenticacao);

	    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
	    bean.setOrder(-110);
	    return bean;
	}
}
