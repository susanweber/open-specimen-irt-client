package edu.stanford.biobank.service;

import edu.stanford.biobank.data.model.PatientId;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MrnValidationService {

	public Map<String, Object> requestPatients(String accessToken, List<String> mrns) throws Exception;
	public Map<String, Object> requestDeidMrns(String accessToken, List<PatientId> mrns) throws IOException;
	
}
