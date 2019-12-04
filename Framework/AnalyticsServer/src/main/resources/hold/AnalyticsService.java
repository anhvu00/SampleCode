package com.kyron;

// provide analytics service to a client (ex. Wild Turkey)
public interface AnalyticsService {

    public String getConfig(String svcName);
    public String analyze(String author);
    
}

