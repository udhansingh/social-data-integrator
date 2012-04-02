package org.onesun.smc.core.services.data;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.smc.core.model.DataObject;

public class HSQLDBService extends AbstractDBService {
	private static Logger logger = Logger.getLogger(HSQLDBService.class);

	public HSQLDBService(){
		super();
	}

	@Override
	public void init(){
		try {
			// Do not init if a connection is already obtained
			if(connection == null){
				location = System.getProperty("java.io.tmpdir");
				
				if(location != null && location.length() > 0){
					location += File.separator + "imdb" + File.separator + id;
				}
				
				Class.forName("org.hsqldb.jdbcDriver");
				
				connection = DriverManager.getConnection("jdbc:hsqldb:file:" + location + ";create=true;shutdown=true", "SA", "");

				String sql = "CREATE TABLE " + tableName + " (internal_id INTEGER IDENTITY PRIMARY KEY, java_type VARCHAR(32), java_object OBJECT)";
				logger.info("CREATE SQL: " + sql);

				PreparedStatement statement = connection.prepareStatement(sql);
				statement.executeUpdate();
			}
		} catch (Exception e) {
			logger.info("Exception while initializing IMDB: " + e.getMessage());
		} finally {
		}
	}

	@Override
	public void write(DataObject dataObject){
		String cn = "java_type, java_object";
		String qm = "?, ?";

		String sql = "INSERT INTO " + tableName + "(" + cn + ") VALUES(" + qm + ")";
		logger.info("INSERT SQL: " + sql);

		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, dataObject.getType());
			statement.setObject(2, dataObject.getObject());
			statement.executeUpdate();
			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public List<DataObject> read(){
		return read(-1, -1);
	}

	@Override
	public List<DataObject> read(int offset, int limit){
		List<DataObject> objects = null;

		String sql = "SELECT";

		if(offset > -1 && limit > -1){
			sql = "SELECT LIMIT " + offset + " " + limit;
		}

		sql += " * FROM " + tableName;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			objects = new ArrayList<DataObject>();
			
			while(rs.next()){
				Object o = rs.getObject("java_object");
				String t = rs.getString("java_type");

				DataObject dc = new DataObject();
				dc.setType(t);
				dc.setObject(o);
				objects.add(dc);
			}

			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return objects;
	}

	@Override
	public void close(){
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			logger.info("Exception while closing IMDB: " + e.getMessage());
		}finally {
		}
	}

	@Override
	public String getIdentity() {
		return "HSQLDBService";
	}
}
