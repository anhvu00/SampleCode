package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecfr")
public class EcfrController {

    private final EcfrService ecfrService;

    @Autowired
    public EcfrController(EcfrService ecfrService) {
        this.ecfrService = ecfrService;
    }

    @GetMapping("/agencies")
    public ResponseEntity<?> getAgencies() {
        try {
            AgenciesResponse agencies = ecfrService.getAgencies();
            return ResponseEntity.ok(agencies);
        } catch (EcfrApiException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("Error fetching agencies from eCFR API", e.getMessage()));
        }
    }
}