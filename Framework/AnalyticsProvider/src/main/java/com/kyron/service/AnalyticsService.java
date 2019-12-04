package com.kyron.service;

// provide analytics service to a client (ex. Wild Turkey)
public interface AnalyticsService {

    public String getConfig(String svcName);
    public void analyze(String author);
    
}

