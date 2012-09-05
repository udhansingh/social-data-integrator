package org.onesun.smc.api;

import java.sql.Connection;
import java.util.List;

import org.onesun.smc.core.services.data.DataSet;

public interface DBService extends DataService {
	void init(String tableName);

	Connection getConnection();
	int getCount(String tableName, String column);

	void write(String tableName, DataSet dataset);
	void update(String tableName, DataSet mandatoryUpdate, String clause);
	
	DataSet read(String tableName);
	DataSet read(String tableName, List<String> columns, int offset, int limit);
	
	void delete(String tableName, int begin, int end);

	DataSet find(String tableName, String clause);
	
	void close();
	void shutdown();

	void setServerPort(Integer serverPort);
	void setUsername(String username);
	void setPassword(String password);
	void setServer(String server);
	void setSchema(String schema);
}
