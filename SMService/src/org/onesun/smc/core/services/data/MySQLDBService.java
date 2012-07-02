package org.onesun.smc.core.services.data;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.commons.SQLUtils;
import org.onesun.smc.core.model.DataObject;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class MySQLDBService extends AbstractDBService {
	private static Logger logger = Logger.getLogger(MySQLDBService.class);
	private int duplicatesCount = 0;
	private int uniquesCount = 0;
	private int insertErrorsCount = 0;
	private int otherErrorsCount = 0;
	
	public MySQLDBService(){
		super();
	}

	@Override
	public void init(){
		try {
			// Do not init if a connection is already obtained
			if(connection == null){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + server  + ":" + serverPort + "/" + schema, username, password);
				
//				String sql = "CREATE TABLE " + tableName + " (internal_id INTEGER IDENTITY PRIMARY KEY, java_type VARCHAR(32), java_object OBJECT)";
//				logger.info("CREATE SQL: " + sql);
//
//				PreparedStatement statement = connection.prepareStatement(sql);
//				statement.executeUpdate();
			}
		} catch (Exception e) {
			logger.info("Exception while initializing IMDB: " + e.getMessage());
		} finally {
		}
	}

	@Override
	public void write(DataObject dataObject){
		if(dataObject != null && (dataObject.getType().compareTo(Map.class.getName()) == 0)){
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)dataObject.getObject();
			
			
			if(map != null && map.size() > 0){
				String sql = "INSERT INTO " + tableName + " (";
				
				String values = " VALUES (";
				for(String key : map.keySet()){
					String[] tokens = key.split(";");
					
					if(tokens.length >= 2){
						sql += tokens[0]  + ", ";
						String value = null;
						
						value = map.get(key);
						if(tokens[1].compareTo(Integer.class.getName()) == 0){
							if(value == null || value.length() == 0){
								value = "-1";
							}
							Integer intValue = Integer.parseInt(value);
							values +=  intValue + ", ";
						}
						else if(tokens[1].compareTo(String.class.getName()) == 0){
							if(value != null && value.length() > 0){
								String escaped = StringEscapeUtils.escapeXml(value);
								values += SQLUtils.quote(escaped) + ", ";
							}
							else {
								values += SQLUtils.quote("") + ", ";
							}
						}
					} else {
						// Default to String
						sql += key + ", ";
						String value = map.get(key);
						
						if(value != null && value.length() > 0){
							String escaped = StringEscapeUtils.escapeXml(value);
							values += SQLUtils.quote(escaped) + ", ";
						}
						else {
							values += SQLUtils.quote("") + ", ";
						}
					}
					
				}
				
				sql = sql.substring(0, sql.length() - 2) + ")";
				values = values.substring(0, values.length() - 2) + ")";
				
				sql += values;
				
//				logger.info("INSERT SQL: " + sql);
				
				try {
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.executeUpdate();
					statement.close();
					
					uniquesCount++;
				} 
				catch(MySQLIntegrityConstraintViolationException e){
					duplicatesCount++;
					logger.info("Duplicates #" + duplicatesCount + "\t\t\tUniques #" + uniquesCount + "\t\t\tAppend Errors #" + insertErrorsCount + "\t\t\tOther Errors #" + otherErrorsCount);
				}
				catch(SQLException e){
					insertErrorsCount++;
					logger.info("Duplicates #" + duplicatesCount + "\t\t\tUniques #" + uniquesCount + "\t\t\tAppend Errors #" + insertErrorsCount + "\t\t\tOther Errors #" + otherErrorsCount);
				}
				catch (Exception e) {
					otherErrorsCount++;
					logger.error("Exception while executing statement: " + e.getMessage());
				} finally {
				}

			}
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
		return "MySQLDBService";
	}

	@Override
	public void shutdown() {
		// TODO: Shutdown the database
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}
}
