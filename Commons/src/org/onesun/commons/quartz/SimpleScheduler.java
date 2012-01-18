/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.commons.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.DailyCalendar;


public class SimpleScheduler extends AbstractScheduler {
	private String triggerName = null;
	private String groupName = null;
	
	private SchedulerFactory factory = null;
	private Scheduler scheduler = null;
	
	public SimpleScheduler(Class<? extends Job> jobClass, String triggerName, String groupName) throws SchedulerException {
		super(jobClass);
		
		this.triggerName = triggerName;
		this.groupName = groupName;
		
		this.factory =  new StdSchedulerFactory();
		this.scheduler = factory.getScheduler();
	}

	public void addJob(String jobName) throws SchedulerException{
		JobDetail job = newJob(jobClass).withIdentity(jobName, groupName).build();
		
		scheduler.scheduleJob(job, trigger);
	}
	
	public void setInterval(String grainStr, int value){
		// TODO: Validation of input grain String
		ScheduleUnit grain = ScheduleUnit.valueOf(grainStr);
		
		setInterval(grain, value);
	}
	
	public void setInterval(ScheduleUnit grain, int value){
		// Start 1 minute later
		Date date = new Date();
		
		switch(grain){
			default:
				trigger = newTrigger()
					.withIdentity(triggerName, groupName)
					.startAt(date)
					.withSchedule(simpleSchedule()
							.withIntervalInMinutes(value)
							.repeatForever())
					.build();
				
			break;
				
			case SECOND: 
				trigger = newTrigger()
				.withIdentity(triggerName, groupName)
				.startAt(date)
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(value)
						.repeatForever())
				.build();
			break;
			
			case HOUR: 
				trigger = newTrigger()
				.withIdentity(triggerName, groupName)
				.startAt(date)
				.withSchedule(simpleSchedule()
						.withIntervalInHours(value)
						.repeatForever())
				.build();
			break;

			case DAY:
				int hour = value / 60;
				// int minutes = value % 60;
				
				trigger = newTrigger()
				.withIdentity(triggerName, groupName)
				.startAt(date)
				.withSchedule(simpleSchedule()
						.withIntervalInHours(hour)
						.repeatForever())
				.build();
			break;
		}
	}
	
	public void setCalendar(Date start, Date end) throws SchedulerException{
		DailyCalendar dailyCalendar = new DailyCalendar(start.getTime(), end.getTime());
		dailyCalendar.setInvertTimeRange(true);
		
		scheduler.addCalendar("dailyCalendar", dailyCalendar, true, true);
	}
	
	@Override
	public void start() throws SchedulerException{
		scheduler.start();
	}
}
