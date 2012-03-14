package org.onesun.smc.core.services.data;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;

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

				Class.forName("org.hsqldb.jdbcDriver");
				connection = DriverManager.getConnection("jdbc:hsqldb:file:/imdb/" + id + ";create=true;shutdown=true", "SA", "");

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
	public void write(){
		String cn = "java_type, java_object";
		String qm = "?, ?";

		String sql = "INSERT INTO " + tableName + "(" + cn + ") VALUES(" + qm + ")";
		logger.info("INSERT SQL: " + sql);

		if(data != null) {
			if(data instanceof RQLObject){
				try {
					PreparedStatement statement = connection.prepareStatement(sql);

					statement.setString(1, "RQLOBJECT");
					statement.setObject(2, data);
					statement.executeUpdate();
					statement.close();
				} catch (Exception e) {
					logger.info("Exception while executing statement: " + e.getMessage());
					e.printStackTrace();
				} finally {
				}
			}
			else if(data instanceof List){
				@SuppressWarnings("unchecked")
				List<Object> objects = (List<Object>)data;

				if(objects.size() > 0){ 
					for(int index = 0; index < objects.size(); index++){
						logger.info("Inserting Row #" + (index + 1));

						Object object = objects.get(index);

						try {
							PreparedStatement statement = connection.prepareStatement(sql);

							if(object instanceof Map){
								statement.setString(1, "MAP");
							}

							statement.setObject(2, object);
							statement.executeUpdate();
							statement.close();
						} catch (Exception e) {
							logger.info("Exception while executing statement: " + e.getMessage());
							e.printStackTrace();
						} finally {
						}
					}
				}
			}
		}
	}

	@Override
	public List<Object> read(){
		return read(-1, -1);
	}

	@Override
	public List<Object> read(int offset, int limit){
		List<Object> list = null;

		String sql = "SELECT";

		if(offset > -1 && limit > -1){
			sql = "SELECT LIMIT " + offset + " " + limit;
		}

		sql += " * FROM " + tableName;
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while(rs.next()){
				Object o = rs.getObject("java_object");
				String t = rs.getString("java_type");

				if(t.compareToIgnoreCase("MAP") == 0){
					@SuppressWarnings("unchecked")
					Map<String, String> datum = (Map<String, String>)o;

					if(datum != null){
						for(String key : datum.keySet()){
							System.out.print(datum.get(key) + "\t\t\t");
						}
						System.out.println("\n---------------------------");
					}
				}
			}

			statement.close();
		} catch (Exception e) {
			logger.info("Exception while executing statement: " + e.getMessage());
			e.printStackTrace();
		} finally {
		}

		return list;
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
		return "HSQLDB Database Service";
	}
}
