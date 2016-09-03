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

import com.lunarsky.minipos.model.dto.PersistenceIdDTO;

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
    
    protected PersistenceIdDTO getId() {
    	final PersistenceIdDTO dto = new PersistenceIdDTO(id);
    	return dto; 
    }
    
    protected void setId(final PersistenceIdDTO id) {
    	this.id = id.getId(); 
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
