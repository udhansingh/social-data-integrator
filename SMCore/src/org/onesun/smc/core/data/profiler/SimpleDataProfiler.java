package org.onesun.smc.core.data.profiler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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

	private TreeSet<Double> blocks = new TreeSet<Double>();
	public SimpleDataProfiler(){
		for(int i = 0; i <= 16; i++){
			double blockSize = 8 * (Math.pow(2, i));
			blocks.add(blockSize);
		}
	}
	private double minBlock(double min) {
		Double d = blocks.higher(min);
		if(d == null) {
			d = blocks.first();
		}

		return d;
	}

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
		// Ignore NULL or empty values
		if(value == null || (value != null && value.trim().length() == 0)){
			return null;
		}

		try {
			Integer.parseInt(value);
			return Integer.class;
		}
		catch(NumberFormatException e) {
		}

		try {
			boolean flag = value.compareToIgnoreCase("true") == 0 || value.compareToIgnoreCase("false") == 0;

			if(flag == true){
				return Boolean.class;
			}
		}
		catch(Exception e) {
		}

		try {
			Double.parseDouble(value);
			return Double.class;
		}
		catch(NumberFormatException e) {
		}

		try {
			Long.parseLong(value);
			return Long.class;
		}
		catch(NumberFormatException e) {
		}

		// URL - Specialization
		try {
			new URL((String)value);
			return URL.class;
		} catch (MalformedURLException e) {
		}

		return String.class;
	}

	@Override
	public void execute() {
		logger.info("Starting simple profiling for ---------------------- " + metadata.getUrl());
		for(String key : metadata.keySet()){
			logger.info("Profiling column: " + key);

			MetaObject mo = metadata.get(key);
			double size = 0;

			Map<String, Integer> statistics = new TreeMap<String, Integer>();
			Class<?> classByHeader = checkClassByHeader(key);

			for(Map<String, String> datum : data){
				String value = datum.get(key);

				if(value != null && value.trim().length() > size){
					size = value.trim().length();
				}

				Class<?> classByValue = checkClassByValue(value);
				// Ignore NULL or empty values
				if(classByValue == null) {
					continue;
				}

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

			if(key.compareTo("description") == 0){
				logger.debug("Wait here");
			}
			Integer max = 0;
			DataType d = mo.getType();
			String className = d.getName();
			for(String tk : statistics.keySet()){
				Integer count = statistics.get(tk);

				if(count > max){
					max = count;
					className = tk;
				}
			}


			final double dtSize = DataTypeFactory.getDataTypeSize(className);
			if(size == 0){
				mo.setIgnore(true);
				mo.setSize(-1);
			} else {
				double mb = minBlock(size);

				logger.info("Minumum: " + mb + " Size: " + size + " DTSize: " + dtSize);
				double blocks = size / mb;

				logger.info("Minumum: " + mb + " Size: " + size + " DTSize: " + dtSize + " Blocks: " + blocks);
				if(blocks > 1){
					size = mb * blocks;
				}
				else {
					size = mb;
				}

				// Update size in blocks

				mo.setSize(size);
			}

			Class<?> clazz = DataTypeFactory.getDataTypeClass(className);
			logger.info("Suggested column type: " + clazz.getCanonicalName());
			d.setClazz(clazz);
			d.setName(clazz.getSimpleName());
			d.setSize(size);

			mo.setType(d);
		}
	}
}