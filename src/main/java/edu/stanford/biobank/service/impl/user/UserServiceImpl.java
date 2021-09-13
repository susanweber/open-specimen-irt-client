package edu.stanford.biobank.service.impl.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.stanford.biobank.data.dao.user.UserDao;
import edu.stanford.biobank.data.model.user.Role;
import edu.stanford.biobank.data.model.user.Status;
import edu.stanford.biobank.data.model.user.User;
import edu.stanford.biobank.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDao userDao;
	
	@Override
	public User registerUser(String username, List<String> roles) throws Exception {
		Set<Role> roleSet = new HashSet<Role>();
		//for (String role : roles) {
		//	roleSet.add();
		//}
		
		User user = null;
		try {
			user = userDao.getUser(username);
			if(user.getStatus().equals(Status.DELETED)) {
				user.setStatus(Status.ACTIVE);
				user.setRoles(roleSet);
				userDao.update(user);
			} else {
				throw new Exception("User " + username + " is already registered.");
			}
		} catch (NoResultException nre) {
			user = new User(username, Status.ACTIVE, roleSet);
			userDao.add(user);
		}
		
		return user;
	}

}
