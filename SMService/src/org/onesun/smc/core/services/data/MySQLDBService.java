package org.onesun.smc.core.services.data;

import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.commons.SQLUtils;

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
	public void init(String tableName){
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
	public void write(String tableName, DataSet dataSet){
		Map<String, String> metaMap = dataSet.getMetadata();
		
		String sql = "INSERT INTO " + tableName + " (";
		
		if(metaMap != null && metaMap.size() > 0) {
			for(String key : metaMap.keySet()){
				sql += key + ", ";
			}
			
			sql = sql.substring(0, sql.length() - 2) + ")";
		}
				
		List<Map<String, Object>> data = dataSet.getData();
		
		if(data != null && data.size() > 0){
			// Process each row
			for(Map<String, Object> map : data){
				String values = " VALUES (";
				
				// process each column
				for(String key : map.keySet()){
					// Get type name
					String type = metaMap.get(key);
					Object object = map.get(key);
					
					// logger.info("Processing: Key = " + key + "\t\tType =  " + type + "\t\tObject = " + object);
					
					// TODO: Add more support for types
					if(type != null){
						if(type.compareTo(Integer.class.getName()) == 0){
							if(object != null){
								values += (Integer)object + ", ";
							} else {
								values +=  "-1, ";
							}
						}
						else if(type.compareTo(BigInteger.class.getName()) == 0){
							if(object != null){
								values += (BigInteger)object + ", ";
							} else {
								values +=  "-1, ";
							}
						}
						else if(type.compareTo(String.class.getName()) == 0){
							if(object != null){
								String escaped = StringEscapeUtils.escapeXml((String)object);
								values += SQLUtils.quote(escaped) + ", ";
							}
							else {
								values += SQLUtils.quote("") + ", ";
							}
						}
					}
				}
				
				values = values.substring(0, values.length() - 2) + ")";
				
				try {
					String sqlStatement = sql + values;
					// logger.info("INSERT SQL: " + sqlStatement);

					PreparedStatement statement = connection.prepareStatement(sqlStatement);
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
	public DataSet read(String tableName){
		return read(tableName, null, -1, -1);
	}

	@Override
	public DataSet read(String tableName, List<String> columns, int offset, int limit){
		DataSet dataSet = null;
		String sql = "SELECT";

		// Select ALL columns by default
		if(columns == null || (columns != null && columns.size() == 0)){
			sql += " * FROM " + tableName;
		}
		else {
			sql += " " + columns.toString().replace(" ", "").replace("[", "(").replace("]", ")") + " FROM " + tableName;
		}
		
		if(offset > -1 && limit > -1){
			sql += " LIMIT " + offset + ", " + limit;
		}
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			ResultSetMetaData rsMeta = rs.getMetaData();
			final int maxCols = rsMeta.getColumnCount();
			
			
			if(maxCols > 0){
				dataSet = new DataSet();

				for(int col = 1; col <= maxCols; col++){
					String name = rsMeta.getColumnName(col);
					String type = rsMeta.getColumnClassName(col);
					
					// column name, column type based on java
					dataSet.addColumn(name, type);
				}
			}

			while(rs.next()) {
				Map<String, Object> datum = new ConcurrentHashMap<String, Object>();
				
				for(String columnName : dataSet.getColumnNames()){
					Object o = rs.getObject(columnName);
				
					datum.put(columnName, o);
				}
				
				dataSet.append(datum);
			}

			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return dataSet;
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
	public int getCount(String tableName, String column) {
		String sql = "SELECT count(" + column + ")";

		sql += " FROM " + tableName;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			Integer count = -1;
			while(rs.next()){
				 count = rs.getInt(1);
			}
			statement.close();
			
			return count;
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return 0;
	}

	@Override
	public void delete(String tableName, int begin, int end) {
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

	@Override
	public void update(String tableName, DataSet dataSet, String clause) {
		Map<String, String> metaMap = dataSet.getMetadata();
		
		String sql = "UPDATE " + tableName + " SET ";
		
		List<Map<String, Object>> data = dataSet.getData();
		
		if(data != null && data.size() > 0){
			// Process each row
			for(Map<String, Object> map : data){
				String values = " ";
				
				// process each column
				for(String key : map.keySet()){
					// Get type name
					String type = metaMap.get(key);
					Object object = map.get(key);
					
					// logger.info("Processing: Key = " + key + "\t\tType =  " + type + "\t\tObject = " + object);
					
					// TODO: Add more support for types
					if(type != null){
						if(type.compareTo(Integer.class.getName()) == 0){
							if(object != null){
								values += key + "=" + (Integer)object + ", ";
							} else {
								values +=  key + "=" + "-1, ";
							}
						}
						else if(type.compareTo(BigInteger.class.getName()) == 0){
							if(object != null){
								values += key + "=" + (BigInteger)object + ", ";
							} else {
								values +=  key + "=" + "-1, ";
							}
						}
						else if(type.compareTo(String.class.getName()) == 0){
							if(object != null){
								String escaped = StringEscapeUtils.escapeXml((String)object);
								values += key + "=" + SQLUtils.quote(escaped) + ", ";
							}
							else {
								values += key + "=" + SQLUtils.quote("") + ", ";
							}
						}
					}
				}
				
				values = values.substring(0, values.length() - 2);
				
				try {
					String sqlStatement = sql + values;
					
					if(clause != null){
						sqlStatement += " WHERE " + clause;
					}
					
					// logger.info("UPDATE SQL: " + sqlStatement);

					PreparedStatement statement = connection.prepareStatement(sqlStatement);
					statement.executeUpdate();
					statement.close();
				} 
				catch(SQLException e){
					logger.info("SQLException during update: " + e.getMessage());
				}
				catch (Exception e) {
					logger.error("Generic Exception during update: " + e.getMessage());
				} finally {
				}

			}
		}
	}

	@Override
	public DataSet find(String tableName, String clause) {
		DataSet dataSet = null;
		
		String sql = "SELECT * FROM " + tableName;
		
		if(clause != null){
			sql += " WHERE " + clause;
		}
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			ResultSetMetaData rsMeta = rs.getMetaData();
			final int maxCols = rsMeta.getColumnCount();
			
			if(maxCols > 0){
				dataSet = new DataSet();

				for(int col = 1; col <= maxCols; col++){
					String name = rsMeta.getColumnName(col);
					String type = rsMeta.getColumnClassName(col);
					
					// column name, column type based on java
					dataSet.addColumn(name, type);
				}
			}

			while(rs.next()) {
				Map<String, Object> datum = new ConcurrentHashMap<String, Object>();
				
				for(String columnName : dataSet.getColumnNames()){
					Object o = rs.getObject(columnName);
				
					datum.put(columnName, o);
				}
				
				dataSet.append(datum);
			}

			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		
		return dataSet;
	}
}
