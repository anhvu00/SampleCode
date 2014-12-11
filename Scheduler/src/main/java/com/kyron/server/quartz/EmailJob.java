package com.kyron.server.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EmailJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO send email
		System.out.println("Send an email...");
		
	}

}
