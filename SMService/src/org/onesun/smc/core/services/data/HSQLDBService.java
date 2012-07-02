package org.onesun.smc.core.services.data;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.onesun.commons.file.FileUtils;
import org.onesun.smc.core.model.DataObject;

public class HSQLDBService extends AbstractDBService {
	private static Logger logger = Logger.getLogger(HSQLDBService.class);
	
	public HSQLDBService(){
		super();
		
		id = UUID.randomUUID().toString();
		tableName = "t" + id.replaceAll("-", "");
		
		schema = System.getProperty("java.io.tmpdir");
		if(schema != null && schema.length() > 0){
			if(schema.endsWith(File.separator)){
				schema += "imdb" + File.separator + id;
			}
			else {
				schema += File.separator + "imdb" + File.separator + id;
			}
			
			File file = new File(schema);
			file.mkdirs();
		}
		
		logger.info("IMDB Location: " + schema);
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
				if(schema != null){
					logger.info("Removing temp IMDB files in " + schema);
					File file = new File(schema);
					boolean status = FileUtils.delete(file);
					if(status == true){
						logger.info("Temp IMDB files in " + schema + " removed successfully");
					}
					else {
						logger.info("Temp IMDB files in " + schema + " could not be removed!");
					}
				}
			}
		}
	}

	
	@Override
	public void init(){
		try {
			// Do not init if a connection is already obtained
			if(connection == null){
				Class.forName("org.hsqldb.jdbcDriver");
				
				connection = DriverManager.getConnection("jdbc:hsqldb:file:" + (schema + File.separator + "database") + ";create=true;shutdown=true", username, password);

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
	public int getCount(String column) {
		String sql = "SELECT count(" + column + ")";

		sql += " FROM " + tableName;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()){
				return rs.getInt(1);
			}
			
			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return 0;
	}

	@Override
	public void delete(int begin, int end) {
		String sql = "DELETE FROM " + tableName + " WHERE internal_id in (SELECT TOP " + end + " internal_id from " + tableName + ")";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate(sql);
			
			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public String getIdentity() {
		return "HSQLDBService";
	}
}
