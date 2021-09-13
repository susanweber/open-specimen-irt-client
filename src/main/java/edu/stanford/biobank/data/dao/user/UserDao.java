package edu.stanford.biobank.data.dao.user;

import java.util.List;

import edu.stanford.biobank.data.model.user.User;

public interface UserDao {

	void add(User user);
	List<User> listUsers();
	void update(User user);
	User getUser(String username);
	Object getOpenSpecimenUser(String username);
	
}
