package edu.stanford.biobank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.biobank.data.model.PatientId;
import edu.stanford.biobank.data.restclient.irt.MrnValidationRestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

import edu.stanford.biobank.service.MrnValidationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MrnValidationController {
	
	private static final Logger logger = Logger.getLogger(MrnValidationController.class);
	
	@Autowired
	MrnValidationService mrnValidationService;

	@Secured({"ROLE_user"})
	@RequestMapping(method = RequestMethod.GET, value = "/patients")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String,Object> getPatients(@RequestParam(value="mrn", required = true) List<String> mrns)  throws Exception {
		logger.info("START MrnValidationController.getPatients");
		
		Map<String,Object> result = mrnValidationService.requestPatients(null, mrns); 
		String exceptionMessage = (String) result.get("exceptionMessage");
		
		logger.info("FINISH MrnValidationController.getPatients");
		if (exceptionMessage!=null) {
			throw new Exception(exceptionMessage);
		} else {
			return result;
		}
		
	}
	
	 @Autowired
	 private FilterChainProxy filterChainProxy;
	 
	 @RequestMapping("/filterChain")
	 @Secured({"ROLE_admin"})
	  public @ResponseBody Map<Integer, Map<Integer, String>> getSecurityFilterChainProxy() {
        Map<Integer, Map<Integer, String>> filterChains= new HashMap<Integer, Map<Integer, String>>();
        int i = 1;
        for(SecurityFilterChain secfc :  this.filterChainProxy.getFilterChains()){
            //filters.put(i++, secfc.getClass().getName());
            Map<Integer, String> filters = new HashMap<Integer, String>();
            int j = 1;
            for(Filter filter : secfc.getFilters()){
                filters.put(j++, filter.getClass().getName());
            }
            filterChains.put(i++, filters);
        }
        return filterChains;
    }

	@Secured({"ROLE_user"})
	@RequestMapping(value = "/deid", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> deidMrns(@RequestBody Map<String, Object> requestBody) throws Exception {
		logger.info("START MrnValidationController.deidMrns");

		List<PatientId> patientIds;
		try {
			// need to convert the LinkedHashMap that Jackson creates to List<PatientId>
			ObjectMapper mapper = new ObjectMapper();
			patientIds = mapper.convertValue(requestBody.get("identifiers"), new TypeReference<List<PatientId>>(){});
		}
		catch(Exception e) { // if there is a parse error, response with a 400 error
			throw new IllegalArgumentException("Error Parsing Request");
		}

		// make sure each PatiendId is valid (ie no half empty PatientIds)
		for(PatientId pid: patientIds) {
			if(pid.getId().isEmpty() || pid.getId_source().isEmpty()) { //error if either field empty
				throw new IllegalArgumentException("Empty Fields not Allowed");
			}
		}

		Map<String, Object> result = mrnValidationService.requestDeidMrns(null, patientIds);
		String responseCode = (String) result.get(MrnValidationRestClient.RESPONSE_CODE);

		logger.info("FINISH MrnValidationController.deidMrns");
		if(!responseCode.equals("200") && !responseCode.equals("201")) {
			throw new Exception("Error Processing Request");
		}
		else {
			return result;
		}
	}

}
