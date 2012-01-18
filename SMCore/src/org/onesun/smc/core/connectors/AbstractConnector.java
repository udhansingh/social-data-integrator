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
package org.onesun.smc.core.connectors;

import java.util.Properties;

import org.onesun.smc.api.Connector;
import org.onesun.smc.core.model.Authentication;

public abstract class AbstractConnector implements Connector {
	protected final String FILE_EXTENSION = ".cnx";
	protected String category = null;
	protected String connectionName = null;
	protected Authentication authentication = null;
	protected String identity = null;

	public AbstractConnector(String category, Authentication authentication){
		this.category = category;
		this.authentication = authentication;
	}
	
	public String getCategory() {
		return category;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	public Authentication getAuthentication() {
		return authentication;
	}
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	
	@Override 
	public abstract void read(Properties properties);
}
