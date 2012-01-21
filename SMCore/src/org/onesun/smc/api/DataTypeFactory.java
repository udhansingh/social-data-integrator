package org.onesun.smc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.onesun.smc.core.model.DataType;

public class DataTypeFactory {
	private static Map<String, DataType> dataTypes = new TreeMap<String, DataType>();
	
	private static List<DataType> initConnectors() {
		List<DataType> items = new ArrayList<DataType>();
		DataType item = null;

		item = new DataType();
		item.setName("String");
		item.setClazz(java.lang.String.class);
		items.add(item);
		
		item = new DataType();
		item.setName("Integer");
		item.setClazz(java.lang.Integer.class);
		items.add(item);

		item = new DataType();
		item.setName("Double");
		item.setClazz(java.lang.Double.class);
		items.add(item);
		
		item = new DataType();
		item.setName("Boolean");
		item.setClazz(java.lang.Boolean.class);
		items.add(item);

		return items;
	}
	
	public static void load() {
		List<DataType> list = initConnectors();
		
		for(DataType dataType : list){
			dataTypes.put(dataType.getName(), dataType);
		}
	}
	
	public static Map<String, DataType> getDataTypes(){
		return dataTypes;
	}
}
