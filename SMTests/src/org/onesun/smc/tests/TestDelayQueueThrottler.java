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
package org.onesun.smc.tests;

import java.util.concurrent.TimeUnit;

import org.onesun.smc.core.throttler.AbstractDelayQueueThrottler;
import org.onesun.smc.core.throttler.SampleThrottlerTask;
import org.onesun.smc.core.throttler.Throttler;
import org.onesun.smc.core.throttler.ThrottlerTask;

public class TestDelayQueueThrottler {
	public static void main(String[] args){
		ThrottlerTask task1 = new SampleThrottlerTask(TimeUnit.SECONDS.toNanos(5), TimeUnit.NANOSECONDS);
		ThrottlerTask task2 = new SampleThrottlerTask(TimeUnit.SECONDS.toNanos(5), TimeUnit.NANOSECONDS);
		ThrottlerTask task3 = new SampleThrottlerTask(TimeUnit.SECONDS.toNanos(5), TimeUnit.NANOSECONDS);
		ThrottlerTask task4 = new SampleThrottlerTask(TimeUnit.SECONDS.toNanos(5), TimeUnit.NANOSECONDS);
		ThrottlerTask task5 = new SampleThrottlerTask(TimeUnit.SECONDS.toNanos(5), TimeUnit.NANOSECONDS);

		Throttler throttler = new AbstractDelayQueueThrottler();
		throttler.start();
		
		throttler.add(task1);
		throttler.add(task2);
		throttler.add(task3);
		throttler.add(task4);
		throttler.add(task5);
		
		// Test stop - must end after completing tasks in the queue
		throttler.stop();
	}
}
