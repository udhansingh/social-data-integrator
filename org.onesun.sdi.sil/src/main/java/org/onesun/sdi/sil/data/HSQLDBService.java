package org.onesun.sdi.sil.data;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.commons.SQLUtils;
import org.onesun.commons.file.FileUtils;

public class HSQLDBService extends AbstractDBService {
	private static Logger logger = Logger.getLogger(HSQLDBService.class);
	private int duplicatesCount = 0;
	private int uniquesCount = 0;
	private int insertErrorsCount = 0;
	private int otherErrorsCount = 0;
	
	public HSQLDBService(String tableName){
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
	public void init(String tableName){
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
	public void write(String tableName, DataSet dataSet){
		Map<String, String> metaMap = dataSet.getMetadata();
		
		String sql = "INSERT INTO " + tableName + " (";
		String values = " VALUES (";
		
		if(metaMap != null && metaMap.size() > 0) {
			for(String key : metaMap.keySet()){
				sql += key + ", ";
			}
		}
				
		List<Map<String, Object>> data = dataSet.getData();
		
		if(data != null && data.size() > 0){
			// Process each row
			for(Map<String, Object> map : data){
				
				// process each column
				for(String key : map.keySet()){
					// Get type name
					String type = metaMap.get(key);
					Object object = map.get(key);
					
					if(type.compareTo(Integer.class.getName()) == 0){
						if(object != null){
							values += (Integer)object + ", ";
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
				catch(SQLException e){
					if(e.getMessage().contains("integrity constraint violation: unique constraint or index violation")) {
						duplicatesCount++;
						logger.info("Duplicates #" + duplicatesCount + "\t\t\tUniques #" + uniquesCount + "\t\t\tAppend Errors #" + insertErrorsCount + "\t\t\tOther Errors #" + otherErrorsCount);
					}
					else {
						insertErrorsCount++;
						logger.info("Duplicates #" + duplicatesCount + "\t\t\tUniques #" + uniquesCount + "\t\t\tAppend Errors #" + insertErrorsCount + "\t\t\tOther Errors #" + otherErrorsCount);
					}
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

		if(offset > -1 && limit > -1){
			sql = "SELECT LIMIT " + offset + " " + limit;
		}

		if(columns == null || (columns != null && columns.size() == 0)){
			sql += " * FROM " + tableName;
		}
		else {
			sql += " " + columns.toString().replace(" ", "").replace("[", "(").replace("]", ")") + " FROM " + tableName;
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
					String type = rsMeta.getColumnTypeName(col);
					
					// column name, column type
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
		return "HSQLDBService";
	}

	@Override
	public void update(String tableName, DataSet mandatoryUpdate, String clause) {
		// TODO Auto-generated method stub
	}

	@Override
	public DataSet find(String tableName, String clause) {
		// TODO Auto-generated method stub
		return null;
	}
}
