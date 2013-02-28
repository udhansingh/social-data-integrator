package org.onesun.smc.core.services.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataSet {
	private Map<String, String> metadata = new ConcurrentHashMap<String, String>();
	private List<Map<String, Object>> data = Collections.synchronizedList(new ArrayList<Map<String,Object>>());
	
	public List<Map<String, Object>> getData() {
		return data;
	}

	public void addColumn(String columnName, String columnType) {
		metadata.put(columnName, columnType);
	}

	public Set<String> getColumnNames() {
		return metadata.keySet();
	}

	public void append(Map<String, Object> datum) {
		data.add(datum);
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void clearData() {
		data.clear();
	}

	public int getRowCount() {
		return data.size();
	}
	
	public int getColumnCount() {
		return metadata.size();
	}
}
