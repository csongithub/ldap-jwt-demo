/**
 * 
 */
package biz.neustar.idaas.ldap.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import biz.neustar.idaas.ldap.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * JwtProvider is responsible for generating Json Web Token. 
 * @author chandan singh
 *
 */
@Component
@Slf4j
public class JwtProvider {
	
	
	private static final long  DEFAULT_EXPIRY_TIME_IN_MINUTE = 5 * 60 * 1000;
	
	/**
	 * The secret key is combined with the header and the payload to create a unique hash. 
	 * We are only able to verify this hash if you have the secret key.
	 */
	@Value("${jwt.secret}")
	private String jwtSecretKey;
	
	/**
	 * The jwt token expiry time can be in Minutes, Hours, Days.
	 * For Example: 5-M, 10-Minutes, 6-Day, 8-Hour etc
	 * 		5-M or 5-Minute or 5-Minutes:	Stands for 5 Minutes.
	 * 		3-H or 3-Hour or 3-Hours	:	Stands for 3 Hour
	 * 		10-D or 10-Day or 10-Days	:	Stands for 10 Days 
	 * 		
	 */
	@Value("${jwt.token.expiry.time}")
	private String jwtTokenExpiryTime;
	
	@Value("${jwt.signature.algo}")
	private SignatureAlgorithm signatureAlgorithm;
	
	
	private long tokenExpiryTime;
	
	
	
	@PostConstruct
	public void init() {
		log.debug("Activated: " + JwtProvider.class.getName());
		this.tokenExpiryTime = getTokenExpiryTime();
	}
	
	//generate token for user
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, user.getLogonID());
	}
	
	/*
	 * 
	 * while creating the token -
	 * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	 * 2. Sign the JWT using the HS512 algorithm and secret key.
	 * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	 *	compaction of the JWT to a URL-safe string 
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		long currentTimeMillis = System.currentTimeMillis();
		Date tokenExpiryTime = new Date(currentTimeMillis + this.tokenExpiryTime);
		
		log.debug("Token Issuing Date: " + new Date(currentTimeMillis));
		log.debug("Token Expiry Date: " + tokenExpiryTime);
		
		String token= Jwts.builder().setClaims(claims)
				   					.setSubject(subject)
				   					.setIssuedAt(new Date(System.currentTimeMillis()))
				   					.setExpiration(tokenExpiryTime)
				   					.signWith(signatureAlgorithm, jwtSecretKey).compact();
		return token;
	}
	
	
	
	private long getTokenExpiryTime() {		
		long expiryTimeMillis	= System.currentTimeMillis();
		
		if(jwtTokenExpiryTime != null && jwtTokenExpiryTime.contains("-")) {
			
			String[] s = jwtTokenExpiryTime.split("-");
			Long time  = Long.parseLong(s[0]);
			String unit = s[1];
			
			long oneMinuteMillis	= 	60 * 1000;
			long oneHourMillis		=	oneMinuteMillis * 60;
			long oneDaysMillis		=	oneHourMillis * 24;
			
			if("M".equalsIgnoreCase(unit) || "Minute".equalsIgnoreCase(unit) || "Minutes".equalsIgnoreCase(unit)) {
				expiryTimeMillis =(time * oneMinuteMillis);
			}else if("H".equalsIgnoreCase(unit) || "Hour".equalsIgnoreCase(unit) || "Hours".equalsIgnoreCase(unit)) {
				expiryTimeMillis =(time * oneHourMillis);
			}else  if("D".equalsIgnoreCase(unit) || "Day".equalsIgnoreCase(unit) || "Days".equalsIgnoreCase(unit)) {
				expiryTimeMillis =(time * oneDaysMillis);
			}
		}else {
			expiryTimeMillis = DEFAULT_EXPIRY_TIME_IN_MINUTE;
		}
		
		return expiryTimeMillis;
	}

}
