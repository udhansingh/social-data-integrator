package org.onesun.smc.core.services.data;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.onesun.commons.file.FileUtils;
import org.onesun.smc.api.DBService;

public abstract class AbstractDBService implements DBService {
	private static Logger logger = Logger.getLogger(AbstractDBService.class);
	
	protected Connection connection = null;
	protected String id = UUID.randomUUID().toString();
	protected final String tableName = "t" + id.replaceAll("-", "");
	protected String location = null;

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
	
	public void shutdown(){
		if(connection != null){
			String sql = "SHUTDOWN";
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.execute();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				logger.info(e.getMessage());
			}finally{
				if(location != null){
					logger.info("Removing temp IMDB files in " + location);
					File file = new File(location);
					boolean status = FileUtils.delete(file);
					if(status == true){
						logger.info("Temp IMDB files in " + location + " removed successfully");
					}
					else {
						logger.info("Temp IMDB files in " + location + " could not be removed!");
					}
				}
			}
		}
	}
}
