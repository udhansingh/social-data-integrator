/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.smc.app.model;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class MetadataPropertiesModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;

	private Map<String, String> data = new TreeMap<String, String>();
	
	private String[] headers = {"Name", "Value"};
	
	public MetadataPropertiesModel(){
		super();
	}
	
	@Override
	public int getRowCount() {
		if(data != null) return data.size();
		return 0;
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return (columnIndex < headers.length) ? headers[columnIndex] : null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(getRowCount() > 0){
			Object x = getValueAt(0, columnIndex);
			
			if(x != null){
				return (x.getClass());
			}
		}
		
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 4:
		case 3:
		case 2: 
			return true;
		}
		
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object[] items = data.keySet().toArray();
		String key = (String)items[rowIndex];
		
		String value = data.get(key);
		
		switch(columnIndex){
		case 1: return value;
		case 0: return key;
		}
		
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Object[] items = data.keySet().toArray();
		
		String key = (String)items[rowIndex];
		String value = data.get(key);
		data.put(key, value);

		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	public void load(Map<String, String> data) {
		this.data.putAll(data);
	}
}
