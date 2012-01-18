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
package org.onesun.commons.throttler;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SampleThrottlerTask implements ThrottlerTask {
	private static Logger logger = Logger.getLogger(SampleThrottlerTask.class);
	
	private static int count = 1;
	protected long delay;
	protected TimeUnit unit;
	
	public long getDelayInMillis(){
		return unit.toMillis(delay);
	}
	
	public SampleThrottlerTask(long delay, TimeUnit unit){
		this.delay = delay;
		this.unit = unit;
	}
	
	@Override
	public int compareTo(Delayed object) {
		if (  delay < (((SampleThrottlerTask)object).delay)  ) 
			return -1;
		else if (  delay > (((SampleThrottlerTask)object).delay) ) 
			return 1;
		return 0;
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		long n = delay - System.nanoTime();
		
		return unit.convert(n, TimeUnit.NANOSECONDS);
	}
	
	@Override
	public void setDelay(long delay){
		this.delay = delay;
	}
	
	@Override
	public void execute() {
		logger.info((count++) + " Invoked @ " + new Date() + " Delay: " + delay);
	}

	@Override
	public TimeUnit getTimeUnit() {
		return unit;
	}
}
