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


public interface QuartzScheduler {
	void start() throws Exception;
	void startDelayed(int delay) throws Exception;
	void addJob(String jobName) throws Exception;
	void setInterval(String grain, int value);
	void setInterval(ScheduleUnit grain, int value);
}