package com.lunarsky.minipos.db.hibernate;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.lunarsky.minipos.interfaces.PersistenceId;

@MappedSuperclass
public class HibernateDAO {
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid2")	
    private String id;

	@Version
	private Long version;
	
    @CreationTimestamp
    private Date created;
    
    @UpdateTimestamp
    private Date lastModified;

    @Transient
    private EntityManager entityManager;
    
    //used by hibernate
    protected HibernateDAO() {}
    
    protected HibernateDAO(final EntityManager entityManager) {
    	setEntityManager(entityManager); 
    }
    
    protected PersistenceId getId() {
    	final PersistenceId persistenceId = new HibernatePersistenceId(id);
    	return persistenceId; 
    }
    
    protected void setId(final PersistenceId id) {
    	if(null == id) {
    		return;
    	}
    	
    	final HibernatePersistenceId persistenceId = (HibernatePersistenceId)id;
    	this.id = persistenceId.getId(); 
    }
    
    protected EntityManager getEntityManager() {
    	assert(null != entityManager);
    	return entityManager;
    }
    
    protected void setEntityManager(final EntityManager entityManager) {
    	assert(null == this.entityManager);
    	this.entityManager = entityManager;
    }
}
