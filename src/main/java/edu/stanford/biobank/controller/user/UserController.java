package edu.stanford.biobank.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.biobank.data.model.user.User;
import edu.stanford.biobank.service.user.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PreAuthorize("hasRole('admin')")
	@RequestMapping(method = RequestMethod.POST, value = "/user")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String,Object> registerUser(@RequestParam(value="username", required = true) String username, 
			@RequestParam(value="role", required = true) List<String> roles)  throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		
		User user = userService.registerUser(username, roles);
		result.put("result", user);
		return result;	
	}
}
