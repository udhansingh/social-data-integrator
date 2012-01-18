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
package org.onesun.smc.app.model;

import org.onesun.smc.api.Connector;
import org.onesun.smc.api.Resource;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.metadata.Metadata;
import org.onesun.smc.core.model.Schedule;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class BusinessObject {
	private String name;
	private Connector connection;
	private Resource resource;
	private Metadata metadata;
	private FilterMetadata filterMetadata;
	private Schedule schedule;
	
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Connector getConnection() {
		return connection;
	}
	public void setConnection(Connector connection) {
		this.connection = connection;
	}

	public String toJSON(){
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		xstream.alias("business_object", BusinessObject.class);
		
		return xstream.toXML(this);
	}
	
	public void reset() {
		name = null;
		connection = null;
		resource = null;
		metadata = null;
		filterMetadata = null;
		schedule = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public FilterMetadata getFilterMetadata() {
		return filterMetadata;
	}

	public void setFilterMetadata(FilterMetadata filterMetadata) {
		this.filterMetadata = filterMetadata;
	}
}
