package edu.stanford.biobank.data.dao;

import java.util.List;

import edu.stanford.biobank.data.model.IrtProperty;


public interface IrtPropertyDao {

	void add(IrtProperty at);
	List<IrtProperty> listIrtProperties();
	void update(IrtProperty irtProperty);
	
}
