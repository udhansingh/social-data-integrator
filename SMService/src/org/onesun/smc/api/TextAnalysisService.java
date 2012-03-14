package org.onesun.smc.api;

public interface TextAnalysisService extends DataService {
	void execute();
	
	void setColumns(String[] columns);
	String getColumnName();
	void setColumnName(String columnName);
}
