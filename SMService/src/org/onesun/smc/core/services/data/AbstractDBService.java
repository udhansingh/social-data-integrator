package org.onesun.smc.core.services.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.onesun.smc.api.DBService;

public abstract class AbstractDBService extends AbstractDataService implements DBService {
	private static Logger logger = Logger.getLogger(AbstractDBService.class);
	
	protected Connection connection = null;
	protected String id = UUID.randomUUID().toString();
	protected final String tableName = "t" + id.replaceAll("-", "");

	public AbstractDBService(){
		super();

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

	private class ShutdownHook extends Thread {
		@Override
		public void run(){
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
				}
			}
		}
	}
}
