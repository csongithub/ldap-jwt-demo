/**
 * 
 */
package biz.neustar.idaas.ldap.persistence;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chandan
 *
 */
public abstract class AbstractPersistence {
	
	@Autowired
	protected EntityManager em;
}
