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

import org.onesun.smc.core.throttler.AbstractStackThrottler;
import org.onesun.smc.core.throttler.SampleThrottlerTask;
import org.onesun.smc.core.throttler.Throttler;
import org.onesun.smc.core.throttler.ThrottlerTask;

public class TestStackThrottler {
	public static void main(String[] args){
		ThrottlerTask task = new SampleThrottlerTask(5, TimeUnit.SECONDS);

		Throttler throttler = new AbstractStackThrottler();
		
		throttler.add(task);
		throttler.add(task);
		throttler.add(task);
		throttler.add(task);
		throttler.add(task);
		
		throttler.start();
		
		// Test stop - must end after completing tasks in the queue
		throttler.stop();
	}
}
