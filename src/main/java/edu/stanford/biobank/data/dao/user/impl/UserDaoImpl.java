package edu.stanford.biobank.data.dao.user.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import edu.stanford.biobank.data.dao.user.UserDao;
import edu.stanford.biobank.data.model.user.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<User> listUsers() {
		CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
		@SuppressWarnings("unused")
	    Root<User> root = criteriaQuery.from(User.class);
	    return em.createQuery(criteriaQuery).getResultList();
	}

	 @Override
	 public void add(User user) {
	    em.persist(user);
	 }
	 
	 @Override
	 public void update(User user) {
		em.merge(user);
	 }
	 
	 @Override
	 public User getUser(String username) {
		 CriteriaBuilder builder = em.getCriteriaBuilder();
		 CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		 @SuppressWarnings("unused")
		 Root<User> root = criteriaQuery.from(User.class);
		 criteriaQuery.select(root);
		 criteriaQuery.where(builder.equal(root.get("username"), username));
		 return em.createQuery(criteriaQuery).getSingleResult();
	 }
	 
	 @Override
	 public Object getOpenSpecimenUser (String username) {
		 return em.createNativeQuery("SELECT * FROM catissue_user WHERE activity_status='Active' AND login_name = :username" )
		 .setParameter("username", username)
		 .getSingleResult();
		
	 }

}
