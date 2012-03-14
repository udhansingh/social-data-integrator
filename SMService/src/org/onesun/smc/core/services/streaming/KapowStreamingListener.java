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
package org.onesun.smc.core.services.streaming;

import java.net.URI;
import java.net.URISyntaxException;

import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.core.connection.properties.KapowConnectionProperties;
import org.onesun.smc.core.providers.web.kapow.KapowObject;
import org.onesun.smc.core.resources.WebResource;
import org.onesun.smc.core.services.handler.ConnectionHandler;
import org.onesun.smc.core.services.handler.DataHandler;

import com.kapowtech.robosuite.api.java.rql.ExecuteRequestBuilder;
import com.kapowtech.robosuite.api.java.rql.RQLException;
import com.kapowtech.robosuite.api.java.rql.RobotExecutor;
import com.kapowtech.robosuite.api.java.rql.construct.PingResponse;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;
import com.kapowtech.robosuite.api.java.rql.construct.RQLResponse;
import com.kapowtech.robosuite.api.java.rql.construct.RepositoryRobotLibrary;
import com.kapowtech.robosuite.api.java.rql.construct.RobotErrorResponse;
import com.kapowtech.robosuite.api.java.rql.construct.RobotMessageResponse;
import com.kapowtech.robosuite.api.java.rql.construct.RobotOutputObjectResponse;
import com.kapowtech.robosuite.api.java.rql.construct.ServerErrorResponse;
import com.kapowtech.robosuite.api.java.rql.construct.ServerExecutionEventResponse;
import com.kapowtech.robosuite.api.java.rql.engine.DemultiplexingRQLHandler;
import com.kapowtech.robosuite.api.java.rql.engine.RQLEngine;
import com.kapowtech.robosuite.api.java.rql.engine.RQLHandler;
import com.kapowtech.robosuite.api.java.rql.engine.ResponseFilteringRQLHandler;
import com.kapowtech.robosuite.api.java.rql.engine.remote.RemoteRQLEngine;
import com.kapowtech.robosuite.api.java.rql.engine.remote.socket.SocketBasedObjectRQLProtocol;

public class KapowStreamingListener {
	private WebResource								resource 			= null;
	private KapowConnectionProperties				connectionProperties 			= null;
	private StreamProcessor							processor 			= null;

	private DataHandler 							dataHandler 		= null;
	private ConnectionHandler 						connectionHandler 	= null;

	private String									executionId			= null;
	private RQLEngine								engine				= null;

	public KapowStreamingListener(DataHandler datahandler, ConnectionHandler connectionHandler){
		this.dataHandler = datahandler;
		this.connectionHandler = connectionHandler;
	}

	public WebResource getResource() {
		return resource;
	}

	public void setResource(WebResource resource) {
		this.resource = resource;
	}

	public ConnectionProperties getConnection() {
		return connectionProperties;
	}

	public void setConnectionProperties(KapowConnectionProperties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public void start() {
		processor = new StreamProcessor();
		processor.start();
	}

	public void stop(){
		processor.terminate();
	}

	private class StreamProcessor extends Thread{
		public StreamProcessor(){
		}

		public void terminate(){
			try {
				RobotExecutor.getRobotExecutor(engine).stop(executionId);
			} catch (RQLException e) {
				e.printStackTrace();
			}
		}

		@Override 
		public void run(){
			try {
				KapowObject object = (KapowObject)resource.getObject();

				String projectName = null;
				String robotName = null;
				if(object != null){
					projectName = object.getProject().getName();
					robotName = object.getRobot().getName();
				}
				else {
					// Obtain project and robot name from WebResource
					String[] tokens = resource.getName().split("/");
					
					if(tokens != null && tokens.length >= 2){
						projectName = tokens[0];
						robotName = tokens[1];
					}
				}

				URI uri = new URI(connectionProperties.getUrl());
				engine = new RemoteRQLEngine(new SocketBasedObjectRQLProtocol(uri.getHost(), connectionProperties.getRqlPort()));

				RQLHandler handler = new MyDemultiplexingRQLHandler(engine, true, dataHandler, connectionHandler);

				String username = connectionProperties.getUsername();
				if(username == null){
					username = "";
				}

				String password = connectionProperties.getPassword();
				if(password == null){
					password = "";
				}

				RepositoryRobotLibrary lib = new RepositoryRobotLibrary(connectionProperties.getUrl(),
						projectName, 10000L, username, password);

				ExecuteRequestBuilder builder = new ExecuteRequestBuilder("Library:/" + robotName);
				builder.setRobotLibrary(lib);

				try {
					RobotExecutor.getRobotExecutor(engine, handler).execute(builder);
				} catch (RQLException e) {
					e.printStackTrace();
				}

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			finally {
			}
		}
	}

	private class MyDemultiplexingRQLHandler extends DemultiplexingRQLHandler implements ResponseFilteringRQLHandler {
		private final RQLEngine engine;
		private final boolean exceptionOnError;
		private DataHandler dataHandler = null;
		private ConnectionHandler connectionHandler = null;

		/**
		 * Creates a new instance 
		 * @param engine the engine the robot was execute on, in case we need to stop it
		 * @param exceptionOnError true if the handler should throw an exception when the robot generates an error
		 * @param dataHandler 
		 * @param connectionHandler 
		 */
		public MyDemultiplexingRQLHandler(RQLEngine engine, boolean exceptionOnError, DataHandler dataHandler, ConnectionHandler connectionHandler) {
			this.engine = engine;
			this.exceptionOnError = exceptionOnError;
			this.dataHandler = dataHandler;
			this.connectionHandler = connectionHandler;
		}

		/**
		 * Implements the ResponseFilteringRQLHandler. This allows us to control which responses will be added to the RQLResult  
		 */
		public boolean filterResponse(RQLResponse response) {
			// this filters out all Returned values, thus preventing OutOfMemory for robots which collect a lot of data  
			return response instanceof RobotOutputObjectResponse;
		}

		/**
		 * You should not override this method unless you call super.handleResponse
		 * Since this is the method in the DemultiplexingRQLHandler which calls the various handle.. methods (like the service() method in Servlet)
		 */
		public void handleResponse(RQLResponse response) throws RQLException {
			super.handleResponse(response);
		}

		public void handleServerExecutionEvent(ServerExecutionEventResponse response) throws RQLException {
			// Server execution events arrive here, this is the first place you can get the robots execution Id
			if (executionId == null ) {
				executionId = response.getExecutionId();
			}
		}

		public void handleRobotError(RobotErrorResponse response) throws RQLException {
			// Handle RobotErrors. Even if the robot generates an error it may still be running (if inside a loop, or if there are more branches)
			// if you throw an exception from this method, it will be bobble up through RobotExecutor.execute(), if you thrown 
			// an exception it is a good idea to send the stop request to the robot

			if (exceptionOnError) {
				RobotExecutor.getRobotExecutor(engine).stop(executionId);
				throw new RQLException(response.getErrorMessage());
			} else {
				// just log the error
			}
		}

		public void handleRobotOutputObject(RobotOutputObjectResponse response) throws RQLException {
			// here we receive all Returned Values, and their metadata.
			// For Ajax like web behavior, add the result to a synchronized list which the GUI can then access

			if(response != null && dataHandler != null){
				RQLObject rqlObject = response.getOutputObject();
				dataHandler.flush(rqlObject);
			}
		}

		public void handleRobotMessage(RobotMessageResponse response) throws RQLException {
			// here we receive all log messages, from the WriteLog step
			System.out.println(response);
		}

		public void handleServerError(ServerErrorResponse response) throws RQLException {
			// here we handle all server errors, like invalid licenses or other errors that prevents the server from running robots
			// you should always throw an exception from this method, the robot will always be stopped (or never started) on the server

			String message = response.getErrorMessage();

			if(message != null){
				connectionHandler.setStatusCode(400);
				dataHandler.flush(message);
			}

			throw new RQLException(message);
		}


		public void handlePingResponse(PingResponse response) throws RQLException {
			// In most cases you don't have to override this method
		}

		public void handleError(RQLException e) throws RQLException {
			// handle engine Errors, you should always throw an exception here
			throw e;
		}

		public void handleFatal(RQLException e) throws RQLException {
			// handle errors the engine can't recover from.
			throw e;
		}
	} 
}
