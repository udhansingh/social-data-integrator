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
	protected String id = null;
	protected String tableName = null;
	protected String location = null;

	public AbstractDBService(){
		super();

		id = UUID.randomUUID().toString();
		tableName = "t" + id.replaceAll("-", "");
		
		location = System.getProperty("java.io.tmpdir");
		if(location != null && location.length() > 0){
			if(location.endsWith(File.separator)){
				location += "imdb" + File.separator + id;
			}
			else {
				location += File.separator + "imdb" + File.separator + id;
			}
			
			File file = new File(location);
			file.mkdirs();
		}
		
		logger.info("IMDB Location: " + location);
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

	private class ShutdownHook extends Thread {
		@Override
		public void run(){
			shutdown();
		}
	}
	
	@Override
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
