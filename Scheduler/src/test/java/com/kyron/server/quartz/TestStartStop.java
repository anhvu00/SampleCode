package com.kyron.server.quartz;

import static org.junit.Assert.*;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.SimpleScheduleBuilder;

public class TestStartStop {

	@Test
	public void test() {
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();

			// define the job and tie it to our HelloJob class
			JobDetail job = JobBuilder.newJob(EmailJob.class)
			    .withIdentity("job1", "group1")
			    .build();

			// Trigger the job to run now, and then repeat every 40 seconds
			Trigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("trigger1", "group1")
			    .startNow()
			    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
			            .withIntervalInSeconds(10)
			            .repeatForever())
			    .build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, trigger);
			
			// wait for a little bit
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scheduler.shutdown();
			
			System.out.println("Done");

		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}

}
