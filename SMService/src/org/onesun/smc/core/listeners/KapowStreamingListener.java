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
package org.onesun.smc.core.listeners;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.core.connection.properties.KapowConnectionProperties;
import org.onesun.smc.core.model.DataObject;
import org.onesun.smc.core.services.handler.ConnectionHandler;
import org.onesun.smc.core.services.handler.DataHandler;

import com.kapowtech.robosuite.api.java.repository.construct.Cluster;
import com.kapowtech.robosuite.api.java.repository.construct.InputAttribute;
import com.kapowtech.robosuite.api.java.repository.construct.InputObject;
import com.kapowtech.robosuite.api.java.repository.construct.RoboServer;
import com.kapowtech.robosuite.api.java.repository.construct.RobotSignature;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClient;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClientException;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClientFactory;
import com.kapowtech.robosuite.api.java.rql.ClusterAlreadyDefinedException;
import com.kapowtech.robosuite.api.java.rql.RQLException;
import com.kapowtech.robosuite.api.java.rql.RQLObjectBuilder;
import com.kapowtech.robosuite.api.java.rql.Request;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;
import com.kapowtech.robosuite.api.java.rql.construct.RepositoryRobotLibrary;
import com.kapowtech.robosuite.api.java.rql.construct.RobotErrorResponse;
import com.kapowtech.robosuite.api.java.rql.construct.RobotOutputObjectResponse;
import com.kapowtech.robosuite.api.java.rql.engine.hotstandby.AbstractFailFastRobotResponseHandler;
import com.kapowtech.robosuite.api.java.rql.engine.hotstandby.ExecutorLogger;
import com.kapowtech.robosuite.api.java.rql.engine.hotstandby.Stoppable;

public class KapowStreamingListener {
	private KapowConnectionProperties				connectionProperties 			= null;
	private StreamProcessor							processor 			= null;

	private DataHandler 							dataHandler 		= null;
	private ConnectionHandler 						connectionHandler 	= null;

	private String									robotName			= null;
	private String 									projectName			= null;
	
	private String									inputTypeName		= null;
	private Map<String, String> 					inputAttributes		= null;

	public KapowStreamingListener(DataHandler datahandler, ConnectionHandler connectionHandler){
		this.dataHandler = datahandler;
		this.connectionHandler = connectionHandler;
	}

	public ConnectionProperties getConnection() {
		return connectionProperties;
	}

	public void setConnectionProperties(KapowConnectionProperties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public void start() {
		try {
			processor = new StreamProcessor(connectionProperties.getUrl(), connectionProperties.getRqlPort(), 
					connectionProperties.getUsername(), connectionProperties.getPassword(), 
					projectName, robotName, dataHandler);
			
			processor.setInputTypeName(inputTypeName);
			processor.setInputAttributes(inputAttributes);
			
			processor.init();
			processor.start();
		} catch(Exception e){
			logger.error("Fatal Error initializing robot executor: " + e.getMessage());
		}
	}

	public void stop(){
		processor.terminate();
	}

	private static Logger logger = Logger.getLogger(StreamProcessor.class);
	private class StreamProcessor extends Thread{

		private static final String OEM_LICENSE_KEY = "Kjh23FuwUkWKc";
		
		private Map<String, String> inputAttributes = null;		
		private String url = null;
		private int servicePort = -1;
		private int consolePort = -1;
		private String username = null;
		private String password = null;
		private DataHandler dataHandler = null;
		private RepositoryRobotLibrary lib = null;
		private Request request = null;
		private Cluster cluster = null;
		private boolean ssl = false;
		private RoboServer server = null;
		private String clusterName = "Select a Cluster";
		
		private String projectName = null;
		private String robotName = null;

		private boolean running = false;
		
		private String hostname = null;
		
		private long seconds = -1;
		private String inputTypeName = null;
		
		private InputObject inputObject = null;
		
		private Stoppable stoppableObj = null;

		public StreamProcessor(String url, Integer consolePort, String username, String password, String projectName, String robotName, DataHandler dataHandler){
			this.url = url;
			this.username = username;
			this.password = password;
			
			this.projectName = projectName;
			this.robotName = robotName;
			
			this.dataHandler = dataHandler;

			this.consolePort = consolePort;
			
			try {
				URI uri = new URI(url);
				this.servicePort = uri.getPort();
				this.hostname = uri.getHost();
				
			} catch (URISyntaxException e) {
			}
		}

		public void init() throws Exception{
			if(username == null){
				username = "";
			}

			if(password == null){
				password = "";
			}
			
			if(lib == null){
				try {
					lib = new RepositoryRobotLibrary(url, projectName, 10000L, username, password);
				}
				catch(Exception e){
					logger.error("Exception while instantiating RepositoryRobotLibrary: " + e.getMessage());
					throw e;
				}
			}
			
			if(server == null){
				try {
					server = new RoboServer(hostname, consolePort, true);
				} catch (Exception e) {
					logger.error("Exception while creating RoboServer: " + e.getMessage());
					throw e;
				}
			}
			
			if(cluster == null){
				cluster = new Cluster(clusterName, new RoboServer[]{server}, ssl);
				try {
					Request.registerCluster(cluster, new ExecutorLogger() {
	                    public void onNotEnoughStandbyServers(int unavailablePrimaryServers, int missingStandByServers) {
	                        logger.error("onNotEnoughStandbyServers" + unavailablePrimaryServers + ", " + missingStandByServers);
	                    }

	                    public void onInvalidLicense(RoboServer roboServer, String invalidMessage) {
	                    	logger.error("onInvalidLicense " + roboServer + " " + invalidMessage);
	                    }

	                    public void onShutDownNotCalled(String error) {
	                    	logger.error("ShutDownNotCalled " + error);
	                    }

	                    public void onRepositoryException(RepositoryClientException e) {
	                    	logger.error("Repository exception" + e);
	                    }

	                    public void onServerOffline(RoboServer server, String details) {
	                    	logger.error("Server offline " + server + " " + details);
	                    }
	                });
				} catch (ClusterAlreadyDefinedException e) {
					logger.error("ClusterAlreadyDefinedException while registering cluster: " + e.getMessage());
				} catch (Exception e) {
					logger.error("Exception while registering cluster: " + e.getMessage());
					throw e;
				}
			}
			
			if(request == null){
				request = new Request("Library:/" + robotName);
				request.setRobotLibrary(lib);
				
				request.setUsername(username);
				request.setPassword(password);
				
				request.setStopOnConnectionLost(true);
				request.setStopRobotOnApiException(false);
				
				if(seconds > 0){
					request.setMaxExecutionTime((int) seconds);
				}
				
				request.setOEMKey(OEM_LICENSE_KEY);
				
				// setup input params for request to work
				if(inputTypeName != null && !inputTypeName.isEmpty()){
					inputTypeName = inputTypeName.replace(".type", "");
					
					RepositoryClient repositoryClient = null;
					RobotSignature robotSignature = null;
					try {
						repositoryClient = RepositoryClientFactory.createRepositoryClient(url, username, password);
						robotSignature = repositoryClient.getRobotSignature(projectName, robotName);
					} catch (RepositoryClientException e) {
						logger.error("RepositoryClientException: while creating client " + e.getMessage());
						throw e;
					}
					
					InputObject inputObjects[] = robotSignature.getInputObjects();
					if(inputObjects != null && inputObjects.length > 0){
						for(InputObject object : inputObjects){
							if(object.getTypeName().compareTo(inputTypeName) == 0){
								inputObject = object;
								break;
							}
						}
						
						// Check if all the required attributes are present
						List<String> missingAttributesList = new ArrayList<String>();
						
						if(inputObject != null){
							InputAttribute[] attributes = inputObject.getAttributes();

							for (InputAttribute attribute:attributes) {
								if (attribute.isRequired()) {
									if (!inputAttributes.containsKey(attribute.getName())) {
										missingAttributesList.add(attribute.getName());
									}
								}
							}

							if (missingAttributesList.size() > 0) {
								StringBuilder missingAttributes = new StringBuilder();
								for (String attribute:missingAttributesList) {
									missingAttributes = missingAttributes.append(attribute + ",");
								}

								missingAttributes.substring(0, missingAttributes.length() - 1);

								throw new Exception("The required Kapow Attribute(s) " + missingAttributesList.toString() + " are missing");
							}

						}
						
						List<String> invalidAttributesList = new ArrayList<String>();
						// Check if all the attributes are valid
						if (inputObject != null && inputAttributes.size() > 0) {
							Set<String> keySet = inputAttributes.keySet();
							
							for (String key : keySet) {
								InputAttribute inputAttribute = inputObject.getAttributeByName(key);
								if (inputAttribute == null) {
									invalidAttributesList.add(key);
								}
							}
						}
						
						if (invalidAttributesList.size() > 0) {
							StringBuilder invalidAttributes = new StringBuilder();
							for (String attribute:invalidAttributesList) {
								invalidAttributes = invalidAttributes.append(attribute + ",");
							}
							
							invalidAttributes.substring(0, invalidAttributes.length() - 1);
							
							throw new Exception("The Kapow Attribute(s) " + invalidAttributes.toString() + " are invalid");
						}
					}
					else {
						// TODO: log the error and push it to UI 
					}
					
					// Set the input type name and the input attributes to the Robot
					RQLObjectBuilder objectBuilder = request.createInputVariable(inputTypeName);
					
					Set<String> inputKeySet = inputAttributes.keySet();
					List<String> invalidValues = new ArrayList<String>();
					
					for (String inputKey: inputKeySet) {
						InputAttribute inputAttribute = inputObject.getAttributeByName(inputKey);
						
						String attributeType = inputAttribute.getType().getName();
						
						// Text Data Type
						if (attributeType.compareToIgnoreCase("Text") == 0) {
							objectBuilder.setAttribute(inputKey, inputAttributes.get(inputKey));
						}
						
						// Integer Data Type
						else if (attributeType.compareToIgnoreCase("Integer") == 0) {
							try {
								long longValue = Long.parseLong(inputAttributes.get(inputKey));
								objectBuilder.setAttribute(inputKey, longValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Boolean Data Type
						else if (attributeType.compareToIgnoreCase("Boolean") == 0) {
							try {
								Boolean booleanValue = Boolean.parseBoolean(inputAttributes.get(inputKey));
								objectBuilder.setAttribute(inputKey, booleanValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Number Data Type
						else if (attributeType.compareToIgnoreCase("Number") == 0) {
							try {
								Double doubleValue = Double.parseDouble(inputAttributes.get(inputKey));
								objectBuilder.setAttribute(inputKey, doubleValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// String Data Type
						else if (attributeType.compareToIgnoreCase("String") == 0) {
							try {
								objectBuilder.setAttribute(inputKey, inputAttributes.get(inputKey));
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Date Data Type
						else if (attributeType.compareToIgnoreCase("Date") == 0) {
							try {
								DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
								Date dateValue = (Date) formatter.parse(inputAttributes.get(inputKey));
						
								objectBuilder.setAttribute(inputKey, dateValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Byte[] Data Type
						else if (attributeType.compareToIgnoreCase("Byte") == 0) {
							try {

								byte[] bytes = inputAttributes.get(inputKey).getBytes();
								
								objectBuilder.setAttribute(inputKey, bytes);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Double Data Type
						else if (attributeType.compareToIgnoreCase("Double") == 0) {
							try {
								Double doubleValue = Double.parseDouble(inputAttributes.get(inputKey));
								objectBuilder.setAttribute(inputKey, doubleValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Long Data Type
						else if (attributeType.compareToIgnoreCase("Long") == 0) {
							try {
								long longValue = Long.parseLong(inputAttributes.get(inputKey));
								objectBuilder.setAttribute(inputKey, longValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Char Data Type
						else if (attributeType.compareToIgnoreCase("Char") == 0) {
							try {
								char charValue = inputAttributes.get(inputKey).charAt(0);
								objectBuilder.setAttribute(inputKey, charValue);
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Object Data Type
						else if (attributeType.compareToIgnoreCase("Object") == 0) {
							try {
								objectBuilder.setAttribute(inputKey, (Object) inputAttributes.get(inputKey));
							} catch(Exception e) {
								invalidValues.add(inputKey);
							}
						}
						
						// Write as String for any other Data Types
						else {
							objectBuilder.setAttribute(inputKey, inputAttributes.get(inputKey));
						}
					}
					
					// Log all the invalid values
					if (invalidValues.size() > 0) {
						StringBuilder invalidAttributeValues = new StringBuilder();
						for (String invalidAttributeValue:invalidValues) {
							invalidAttributeValues = invalidAttributeValues.append(invalidAttributeValue + ",");
						}
						
						invalidAttributeValues.substring(0, invalidAttributeValues.length() - 1);
						
						throw new Exception("The Kapow Attribute(s) " + invalidAttributeValues.toString() + " have invalid value. Please enter using the right datatype");
					}
				}
				
			}
		}
		
		public void terminate(){
			running = false;

			if (this.stoppableObj != null) {
				try {
					this.stoppableObj.stop();
				} catch (RQLException e) {
					logger.error("Error in stopping the Kapow Robot - " + e.getMessage());
				}
			}
		}

		private void execute() {
			ErrorCollectingRobotResponseHandler responseHandler = new ErrorCollectingRobotResponseHandler(dataHandler, logger, this);
			
			if(request != null && responseHandler != null){
				try {
					logger.info("Executing Robot Executor for robot: " + robotName);
					request.execute(clusterName, responseHandler);
					running = true;
				} catch (RQLException e) {
					running = false;
					logger.error("RQLException while executing Robot Executor for: " + robotName + " Error: " + e.getMessage());
				} catch (Exception e) {
					running = false;
					logger.error("Exception while executing Robot Executor for: " + robotName + " Error: " + e.getMessage());
				}
			}
		}
		
		@Override
		public void run(){
			if(running == false){
				execute();
			}
		}
		
		public void setStoppableObject(Stoppable stoppable) {
			this.stoppableObj = stoppable;
		}

		public void setInputTypeName(String inputTypeName) {
			this.inputTypeName = inputTypeName;
		}

		public void setInputAttributes(Map<String, String> inputAttributes) {
			this.inputAttributes = inputAttributes;
		}
	}
	
	public class ErrorCollectingRobotResponseHandler extends AbstractFailFastRobotResponseHandler {
		private DataHandler dataHandler = null;
		private Logger logger = null;
		private StreamProcessor streamProcessor = null;
		
		public ErrorCollectingRobotResponseHandler(DataHandler dataHandler, Logger logger){
			this.dataHandler = dataHandler;
			this.logger = logger;
		}
		
		public ErrorCollectingRobotResponseHandler(DataHandler dataHandler, Logger logger, StreamProcessor streamProcessor) {
			this.dataHandler = dataHandler;
			this.logger = logger;
			this.streamProcessor = streamProcessor;
		}
		
		public void handleReturnedValue(RobotOutputObjectResponse response, Stoppable stoppable) throws RQLException {
			if(dataHandler != null){
				RQLObject rqlObject = response.getOutputObject();
				
				if(rqlObject != null){
					DataObject dataObject = new DataObject();
					dataObject.setType("RQLOBJECT");
					dataObject.setObject(rqlObject);

					try {
						dataHandler.flush(dataObject);
					} catch(Exception e){
						logger.error("Exception while flushing response from robot: " + e.getMessage());
					}
				}
			}
		}

		@Override
		public void handleRobotError(RobotErrorResponse response, Stoppable stoppable) throws RQLException {
			// do not call super as this will stop the robot
			logger.error("Robot Execution Error: " + response.getErrorMessage());
		}
		
		@Override
		public void robotStarted(Stoppable stoppable) {
			this.streamProcessor.setStoppableObject(stoppable);
			logger.info("Successfully got the stoppable object");
		}
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public void setInputTypeName(String inputTypeName) {
		this.inputTypeName = inputTypeName;
	}

	public void setInputAttributes(Map<String, String> inputAttributes) {
		this.inputAttributes = inputAttributes;
	}
}
