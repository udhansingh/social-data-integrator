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
package org.onesun.smc.core.providers.web.kapow;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.smc.core.providers.web.AbstractWebProvider;
import org.onesun.smc.core.resources.WebResource;

import com.kapowtech.robosuite.api.java.repository.construct.InputObject;
import com.kapowtech.robosuite.api.java.repository.construct.Project;
import com.kapowtech.robosuite.api.java.repository.construct.Robot;
import com.kapowtech.robosuite.api.java.repository.construct.RobotSignature;
import com.kapowtech.robosuite.api.java.repository.construct.Type;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClient;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClientException;
import com.kapowtech.robosuite.api.java.repository.engine.RepositoryClientFactory;
import com.kapowtech.robosuite.api.java.rql.ExecuteRequestBuilder;
import com.kapowtech.robosuite.api.java.rql.RQLException;
import com.kapowtech.robosuite.api.java.rql.RQLResult;
import com.kapowtech.robosuite.api.java.rql.RobotExecutor;
import com.kapowtech.robosuite.api.java.rql.construct.RepositoryRobotLibrary;
import com.kapowtech.robosuite.api.java.rql.engine.RQLEngine;
import com.kapowtech.robosuite.api.java.rql.engine.dist.ClientSideDistributingRQLEngine;
import com.kapowtech.robosuite.api.java.rql.engine.dist.LoadBalancingRoundRobinDistributionPolicy;
import com.kapowtech.robosuite.api.java.rql.engine.remote.RemoteRQLEngine;
import com.kapowtech.robosuite.api.java.rql.engine.remote.socket.SocketBasedObjectRQLProtocol;

public class KapowProvider extends AbstractWebProvider {
	private static Logger logger = Logger.getLogger(KapowProvider.class);
			
	private RepositoryClient client = null;
	private List<RemoteRQLEngine> engines = new ArrayList<RemoteRQLEngine>();
	private RemoteRQLEngine localhostEngine = null;

	@Override
	public void init() {
	}

	@Override
	public void validate(){
		if(client == null){
			try {
				client = RepositoryClientFactory.createRepositoryClient(connection.getUrl(), connection.getUsername(), connection.getPassword());
				
				String host = "localhost";
				int port = 50000;
				
				try {
					URI uri = new URI(connection.getUrl());
					host = uri.getHost();
					// port = uri.getPort();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				
				localhostEngine = new RemoteRQLEngine(new SocketBasedObjectRQLProtocol(host, port));
				
				engines.add(localhostEngine);
			} catch (RepositoryClientException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object execute(WebResource webResource) {
		KapowObject object = (KapowObject)webResource.getObject();
		
		Project project = null;
		Robot robot = null;
		if(object != null){
			project = object.getProject();
			robot = object.getRobot();
		}
		
		ExecuteRequestBuilder builder = new ExecuteRequestBuilder("Library:/" + robot.getName());

		// Use the repository as Robot Library
		RepositoryRobotLibrary lib = new RepositoryRobotLibrary(connection.getUrl(),
				project.getName(), 10000L, connection.getUsername(), connection.getPassword());
		builder.setRobotLibrary(lib);

		RQLEngine engine = new ClientSideDistributingRQLEngine(new LoadBalancingRoundRobinDistributionPolicy(engines));

		// Execute the robot, and process the result
		RobotExecutor robotExecutor = RobotExecutor.getRobotExecutor(engine);
		
		try {
			RQLResult result = robotExecutor.execute(builder);
			return result;
		} catch (RQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void refreshMetadata() {
		try {
			Project[] projects = client.getProjects();
			resources.clear();
			
			for(Project project : projects){
				Robot[] robots = client.getRobotsInProject(project.getName());
				
				for(Robot robot : robots){
					RobotSignature signature = client.getRobotSignature(project.getName(), robot.getName());
					Type[] types = signature.getReturnedTypes();

					InputObject[] inputObjects = signature.getInputObjects();
					
					// Record robot's signature
					KapowObject object = new KapowObject();
					object.setProject(project);
					object.setRobot(robot);
					object.setReturnedTypes(types);
					object.setInputObjects(inputObjects);
					
					WebResource resource = new WebResource(project.getName() + "/" + robot.getName(), object);
					logger.info(resource);
					
					resources.add(resource);
				}
			}
		} catch (RepositoryClientException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getIdentity() {
		return "Kapow";
	}
	
	@Override
	public String getCategory() {
		return "KAPOW";
	}
}
