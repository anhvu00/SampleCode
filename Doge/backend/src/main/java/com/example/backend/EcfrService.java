package com.example.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EcfrService {

    private static final Logger logger = LoggerFactory.getLogger(EcfrService.class);
    private static final String ECFR_AGENCIES_URL = "https://www.ecfr.gov/api/admin/v1/agencies.json";

    private final RestTemplate restTemplate;

    @Autowired
    public EcfrService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AgenciesResponse getAgencies() {
        try {
            logger.info("Fetching agencies from eCFR API");
            ResponseEntity<AgenciesResponse> response = restTemplate.getForEntity(ECFR_AGENCIES_URL, AgenciesResponse.class);
            logger.info("Successfully retrieved {} agencies",
                    response.getBody() != null ? response.getBody().getAgencies().size() : 0);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Client error when calling eCFR API: {}", e.getMessage());
            throw new EcfrApiException("Error calling eCFR API: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error when calling eCFR API: {}", e.getMessage());
            throw new EcfrApiException("eCFR API server error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error when calling eCFR API: {}", e.getMessage());
            throw new EcfrApiException("Unexpected error when calling eCFR API: " + e.getMessage(), e);
        }
    }
}