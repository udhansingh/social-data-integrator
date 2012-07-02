package org.onesun.smc.api;

import java.util.List;

import org.onesun.smc.core.model.DataObject;

import java.sql.Connection;

public interface DBService extends DataService {
	void init();
	void close();
	void write(DataObject dataObject);
	List<DataObject> read();
	List<DataObject> read(int offset, int limit);
	void shutdown();
	int getCount(String column);
	void delete(int begin, int end);
	
	void setServerPort(Integer serverPort);
	void setUsername(String username);
	void setPassword(String password);
	void setServer(String server);
	void setSchema(String schema);
	void setTableName(String tableName);
	String getTableName();
	Connection getConnection();
}
