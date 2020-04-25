/**
 * 
 */
package biz.neustar.idaas.ldap.jwt;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * The {@link JwtRequestFilter} extends the Spring Web Filter {@link OncePerRequestFilter} class. 
 * For any incoming request, this Filter class gets executed only once. 
 * It checks if the request has a valid JWT token. 
 * If it has a valid JWT Token, then it sets the authentication in context to specify 
 * that the current user is authenticated.
 * 
 * @author chandan singh
 *
 */

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
	
	
	private static final String JWT_TOKEN_PREFIX = "Bearer ";
	private static final String REQUEST_TOKEN_HEADER = "Authorization";

	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		
		String requestTokenHeader = request.getHeader(REQUEST_TOKEN_HEADER);
		if (requestTokenHeader == null)
			requestTokenHeader = request.getHeader(REQUEST_TOKEN_HEADER.toLowerCase());
		
		Enumeration<String> headerNames = request.getHeaderNames();

	    if (headerNames != null) {
	            while (headerNames.hasMoreElements()) {
	            	    String headerName = headerNames.nextElement();
	                    System.out.println("Header[" + headerName + "]: " + request.getHeader(headerName));
	            }
	    }
		
		String username = null;
		String jwtToken = null;
		
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(JWT_TOKEN_PREFIX)) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				log.error("JWT Token has expired");
			} catch (Throwable e) {
				log.error("Invalid JWT Token");
			}
		}
		
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// After setting the Authentication in the context, we specify that the current user is authenticated. 
				// So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
