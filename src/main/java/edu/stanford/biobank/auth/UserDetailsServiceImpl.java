package edu.stanford.biobank.auth;

import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.stanford.biobank.data.dao.user.UserDao;
import edu.stanford.biobank.data.model.user.Role;
import edu.stanford.biobank.data.model.user.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	UserDao userDao;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("START loadUserByUsername " + username);
		User user = null;
		try {
			user = userDao.getUser(username);
			logger.info("Found in irt_client_user : " + username);
			return new org.springframework.security.core.userdetails.User(username, "password", true, true, true, true, getAuthorities(user));
		} catch (NoResultException nre) {
			try {
				return getOpenspecimenUserAuth(username);
			} catch (NoResultException nre2) {
				throw new UsernameNotFoundException(username);
			}
		}
		
	}

	private List<SimpleGrantedAuthority> getAuthorities(User user) {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
		
		for (Role role : roles) {
			auths.add(new SimpleGrantedAuthority(role.getName()));
			logger.info("Role " + role.getName() + " found for user " +  user.getUsername());
		}
		
		return auths;
		
	}

	/**
	 * Temporary solution for OpenSpecimen users
	 */
	private org.springframework.security.core.userdetails.User getOpenspecimenUserAuth(String username) {
		userDao.getOpenSpecimenUser(username);
		logger.info("Found in catissue_user : " + username);
		List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
		auths.add(new SimpleGrantedAuthority("ROLE_user"));
		return new org.springframework.security.core.userdetails.User(username, "password", true, true, true, true, auths);
	}
	
}
