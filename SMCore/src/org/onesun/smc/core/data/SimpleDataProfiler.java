package org.onesun.smc.core.data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.onesun.smc.api.DataProfiler;
import org.onesun.smc.api.DataTypeFactory;
import org.onesun.smc.core.metadata.Metadata;
import org.onesun.smc.core.model.DataType;
import org.onesun.smc.core.model.MetaObject;

public class SimpleDataProfiler implements DataProfiler {
	private static Logger logger = Logger.getLogger(SimpleDataProfiler.class);
	
	private List<Map<String, String>> data;
	private Metadata metadata;
	
	@Override
	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	@Override
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	private Class<?> checkClassByHeader(String value){
		if(value.toLowerCase().contains("date") || value.toLowerCase().contains("time")){
			return Date.class;
		}
		
		return String.class;
	}
	
	private Class<?> checkClassByValue(String value){
		if(value == null){
			return String.class;
		}
		
		value = value.trim();
		if(value.length() == 0){
			return String.class;
		}
		
		boolean retry = false;
		
		try {
			Integer.parseInt(value);
			return Integer.class;
		}
		catch(NumberFormatException e) {
			retry = true;
		}
		
		if(retry == true){
			retry = false;
			
			try {
				boolean flag = value.compareToIgnoreCase("true") == 0 || value.compareToIgnoreCase("false") == 0;
				
				if(flag == true){
					return Boolean.class;
				}
				else {
					retry = true;
				}
			}
			catch(Exception e) {
				retry = true;
			}
		}
		
		if(retry == true){
			retry = false;
			
			try {
				Double.parseDouble(value);
				return Double.class;
			}
			catch(NumberFormatException e) {
				retry = true;
			}
		}
		
		if(retry == true){
			retry = false;
			
			try {
				Long.parseLong(value);
				return Long.class;
			}
			catch(NumberFormatException e) {
				retry = true;
			}
		}
		
		return String.class;
	}
	
	@Override
	public void execute() {
		logger.info("Starting simple profiling for ---------------------- " + metadata.getUrl());
		for(String key : metadata.keySet()){
			logger.info("Profiling column: " + key);
			
			MetaObject mo = metadata.get(key);
//			MetaObject mo = (MetaObject)object.clone();
			int size = 0;
			
			Map<String, Integer> statistics = new TreeMap<String, Integer>();
			Class<?> classByHeader = checkClassByHeader(key);
			
			for(Map<String, String> datum : data){
				String value = datum.get(key);
				
				if(value != null && value.trim().length() > size){
					size = value.trim().length();
				}
				
				Class<?> classByValue = checkClassByValue(value);
				if(classByValue.getCanonicalName().compareTo(classByHeader.getCanonicalName()) != 0){
					if(!classByHeader.getCanonicalName().contains("String")){
						classByValue = classByHeader;
					}
				}
				
				String name = classByValue.getSimpleName();
				
				Integer count = statistics.get(name);
				if(count == null){
					count = 1;
				}
				else {
					count++;
				}
				statistics.put(name, count);
			}
			
			Integer max = 0;
			DataType d = mo.getType();
			String className = d.getName();
			for(String tk : statistics.keySet()){
				Integer count = statistics.get(tk);
				className = tk;
				
				if(count > max){
					max = count;
					break;
				}
			}
			mo.setSize(size);
			if(size == 0){
				mo.setIgnore(true);
			}

			Class<?> clazz = DataTypeFactory.getDataTypeClass(className);
			logger.info("Suggested column type: " + clazz.getCanonicalName());
			d.setClazz(clazz);
			d.setName(clazz.getSimpleName());
			mo.setType(d);
		}
	}
}