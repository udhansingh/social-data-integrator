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

import java.util.Stack;

public class AbstractStackThrottler implements Throttler {
	protected volatile Integer count = 1;
	protected volatile Stack<ThrottlerTask> tasks = new Stack<ThrottlerTask>();
	protected volatile boolean running = true;
	
	@Override
	public void add(ThrottlerTask throttlerTask) {
		tasks.push(throttlerTask);
	
		synchronized(count){
			// Adjust to the actual task count
			if(count == 1 && tasks.size() == 1){
				return;
			}

			count++;
		}
	}
	
	@Override
	public void stop(){
		Thread thread = new Thread(){
			@Override
			public void run(){
				while(running == true){
					if(count <= 0){
						running = false;
					}
				}
			}
		};
		
		thread.start();
	}
	
	@Override
	public void start(){
		Thread thread = new Thread(){
			@Override
			public void run(){
				while(running == true){
					if(tasks.empty() == false){
						ThrottlerTask task = tasks.pop();
						task.execute();
						
						synchronized(count){
							count--;
						}
						
						try {
							long duration = task.getDelayInMillis();
							Thread.sleep(duration);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		thread.start();
	}
}
