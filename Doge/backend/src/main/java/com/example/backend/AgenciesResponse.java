package com.example.backend;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgenciesResponse {
    @JsonProperty("agencies")
    private List<Agency> agencies;

    public List<Agency> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<Agency> agencies) {
        this.agencies = agencies;
    }
}