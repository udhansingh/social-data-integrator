package org.onesun.smc.api;

import java.util.List;

import org.onesun.smc.core.model.DataObject;

public interface DBService extends DataService {
	void init();
	void close();
	void write(DataObject dataObject);
	List<DataObject> read();
	List<DataObject> read(int offset, int limit);
	void shutdown();
	int getCount(String column);
	void delete(int begin, int end);
}
