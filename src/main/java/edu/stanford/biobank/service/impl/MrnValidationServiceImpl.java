package edu.stanford.biobank.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.stanford.biobank.data.model.PatientId;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import edu.stanford.biobank.data.restclient.irt.MrnValidationRestClient;
import edu.stanford.biobank.service.MrnValidationService;

@Service
public class MrnValidationServiceImpl implements MrnValidationService {
	
	private static final Logger logger = Logger.getLogger(MrnValidationServiceImpl.class);
	
	@Override
	public Map<String, Object> requestPatients(String accessToken, List<String> mrns) throws Exception {
		return MrnValidationRestClient.getPatients(accessToken, mrns);
	}

	@Override
	public Map<String, Object> requestDeidMrns(String accessToken, List<PatientId> mrns) throws IOException {
		return MrnValidationRestClient.getDeidMrns(accessToken, mrns);
	}
}
