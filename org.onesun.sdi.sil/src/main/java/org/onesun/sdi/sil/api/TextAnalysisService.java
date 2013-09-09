package org.onesun.sdi.sil.api;

public interface TextAnalysisService extends DataService {
	void execute();
	
	void setColumns(String[] columns);
	String getColumnName();
	void setColumnName(String columnName);
	
	void setData(Object data);
	void setMetadata(Object metadata);
}
