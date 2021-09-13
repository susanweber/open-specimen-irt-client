package edu.stanford.biobank.data.model.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="IRT_CLIENT_ROLE")
public class Role {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	 @Column(name = "NAME", nullable = false, unique = true)
    private String name;
	 
	 @ManyToMany(mappedBy = "roles")
	 private Set<User> user = new HashSet<User>();
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}
	
	
	
	
	
}
