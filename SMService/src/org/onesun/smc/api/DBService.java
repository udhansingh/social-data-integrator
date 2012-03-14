package org.onesun.smc.api;

import java.util.List;

public interface DBService extends DataService {
	void init();
	void close();
	void write();
	List<Object> read();
	List<Object> read(int offset, int limit);
}
