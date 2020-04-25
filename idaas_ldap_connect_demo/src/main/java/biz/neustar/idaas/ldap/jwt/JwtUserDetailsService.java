/**
 * 
 */
package biz.neustar.idaas.ldap.jwt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import biz.neustar.idaas.ldap.common.EDeleteIndicator;
import biz.neustar.idaas.ldap.domain.User;
import biz.neustar.idaas.ldap.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * For every API call this service is called to validate the user.
 * It basically checks if the user is currently active in application database or not.
 * 
 * To avoid the database call for every API validation, It uses a in-memory cache.
 * So on first API call for any user, it loads the user from database, validates the loaded user and also put that user in cache.
 * So next time if the same user makes any API call, the user is loaded from cache and not from database.
 * 
 * 
 * Now there might be some case where a user might have been removed from the application database, but he could be still in cache.
 * So with a valid token this user can still make any API call.
 * To prevent this, we check if the user has logged in today.
 * And if not then again user is looked up in application database.
 * 
 * @author chandan singh
 *
 */
@Component
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
	
	
	@PostConstruct
	public void init() {
		log.debug("Activated: " + JwtUserDetailsService.class.getName());
	}

	@Autowired
	private UserPersistence userPersistence;
	
	private Map<String, User> userCache = new HashMap<String, User>();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = null;
		
		//To avoid the database call every time, First check if the user is already in cache memory
		//Also if the user in cache has not logged in today then fetch the user from database.
		if(userCache.containsKey(username)) {
			user = userCache.get(username);
			
			//Okay user is in cache, So now check if user has not logged in today.
			//If not then fetch user from database and update in cache
			if(!loggedInToday(user.getLastLogonTimestamp())) {
				user = userPersistence.getActiveUser(username);

				//If the user is removed from application database, then remove from cache.
				//otherwise update the user in cache.
				if(user == null)
					userCache.remove(username);
				else
					userCache.put(username, user);
			}
		}else {
			//So user is not present in cache memory, So fetch the user from database.
			//Also update the cache. 
			user = userPersistence.getActiveUser(username);
			userCache.put(username, user);
		}
		
		//Okay now check if user is there and currently active
		if(user != null && user.getDeletedIndicator().equals(EDeleteIndicator.ACTIVE)) {
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogonID(), "" , new ArrayList<>());
			return userDetails;
			
		}else {
			throw new UsernameNotFoundException("User Not Found or Not Active: " + username);
		}
	}
	
	
	private boolean loggedInToday(Date loggedInDate) {
		
		if(loggedInDate != null) {
			Calendar loggedInCal = Calendar.getInstance();
			loggedInCal.setTime(loggedInDate);
			
			Calendar todaysCal = Calendar.getInstance();
			todaysCal.setTime(new Date());
			
			if(loggedInCal.get(Calendar.YEAR) == todaysCal.get(Calendar.YEAR)
					&& loggedInCal.get(Calendar.MONTH) == todaysCal.get(Calendar.MONTH)
					&& loggedInCal.get(Calendar.DAY_OF_MONTH) == todaysCal.get(Calendar.DAY_OF_MONTH))
				return true;
			else
				return false;
		}else
			return false;
	}
}
