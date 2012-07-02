package org.onesun.smc.core.services.data;

import java.sql.Connection;

import org.onesun.smc.api.DBService;

public abstract class AbstractDBService implements DBService {
	protected Connection connection = null;
	protected String id = null;
	protected String tableName = null;
	
	protected String server = null;
	protected Integer serverPort = -1;
	protected String schema = null;
	
	protected String username = null;
	protected String password = null;

	public AbstractDBService(){
		super();

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

	private class ShutdownHook extends Thread {
		@Override
		public void run(){
			shutdown();
		}
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setServer(String server) {
		this.server = server;
	}

	@Override
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	@Override
	public String getTableName(){
		return this.tableName;
	}
	
	@Override
	public void setServerPort(Integer serverPort){
		this.serverPort = serverPort;
	}
	
	@Override
	public Connection getConnection(){
		return connection;
	}
}
