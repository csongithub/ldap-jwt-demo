/**
 * 
 */
package biz.neustar.idaas.ldap.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import biz.neustar.idaas.ldap.domain.User;

/**
 * @author chandan singh
 *
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	
	@Query("select u from User u where UPPER(u.logonID) = ?1")
	public User getUserByLogonID(String logonID);

}
