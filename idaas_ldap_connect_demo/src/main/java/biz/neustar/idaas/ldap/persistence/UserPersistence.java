/**
 * 
 */
package biz.neustar.idaas.ldap.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.neustar.idaas.ldap.common.EDeleteIndicator;
import biz.neustar.idaas.ldap.domain.User;
import biz.neustar.idaas.ldap.repository.IUserRepository;

/**
 * @author chandan singh
 *
 */
@Component
public class UserPersistence extends AbstractPersistence{
	
	private static final String	LOGIN				= "select u from User u where UPPER(u.logonID) = ?1 and u.deletedIndicator = ?2";
	
	
	@Autowired
	private IUserRepository repository;
	
	
	
	public List<User> allUsers() {
		try {
			return repository.findAll();
		}catch(NoResultException e) {
			return null;
		}
	}
	
	
	@Transactional
	public User getUserByLogonID(String logonID) {
		try {
			TypedQuery<User> query = em.createQuery(LOGIN, User.class);
			query.setParameter(1, logonID.toUpperCase());
			query.setParameter(2, EDeleteIndicator.ACTIVE);
			User u = query.getSingleResult();
			u.setLastLogonTimestamp(new Date());
			return em.merge(u);
		}catch(NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * This method just get the user from database, and does not update the last logon time.
	 * 
	 * @param logonID
	 * @return
	 */
	public User getActiveUser(String logonID) {
		try {
			TypedQuery<User> query = em.createQuery(LOGIN, User.class);
			query.setParameter(1, logonID.toUpperCase());
			query.setParameter(2, EDeleteIndicator.ACTIVE);
			User u = query.getSingleResult();
			u.setLastLogonTimestamp(new Date());
			return em.merge(u);
		}catch(NoResultException e) {
			return null;
		}
	}

}
