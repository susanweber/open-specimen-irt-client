package edu.stanford.biobank.data.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import edu.stanford.biobank.data.dao.IrtPropertyDao;
import edu.stanford.biobank.data.model.IrtProperty;

@Repository
public class IrtPropertyDaoImpl implements IrtPropertyDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<IrtProperty> listIrtProperties() {
		CriteriaQuery<IrtProperty> criteriaQuery = em.getCriteriaBuilder().createQuery(IrtProperty.class);
		@SuppressWarnings("unused")
	    Root<IrtProperty> root = criteriaQuery.from(IrtProperty.class);
	    return em.createQuery(criteriaQuery).getResultList();
	}

	 @Override
	 public void add(IrtProperty irtProperty) {
	    em.persist(irtProperty);
	 }
	 
	 @Override
	 public void update(IrtProperty irtProperty) {
		em.merge(irtProperty);
	 }

}
