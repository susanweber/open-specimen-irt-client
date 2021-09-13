package edu.stanford.biobank.service.user;

import java.util.List;

import edu.stanford.biobank.data.model.user.User;

public interface UserService {
	public User registerUser(String username, List<String> roles) throws Exception;
}
