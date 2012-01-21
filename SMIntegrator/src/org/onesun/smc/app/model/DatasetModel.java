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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.onesun.smc.core.metadata.Metadata;
import org.onesun.smc.core.model.MetaObject;


public class DatasetModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;

	private Metadata metadata = null;
	private List<Map<String, String>> data = null;
	
	@Override
	public int getRowCount() {
		return (data != null) ? data.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return (metadata != null) ? metadata.size() : 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(metadata != null && columnIndex < metadata.size()){
			Object[] object = metadata.keySet().toArray();
			String key = (String)object[columnIndex];
			
			// Value contains SQL Compatible column name
			if(key != null){
				MetaObject o = metadata.get(key); 
				return o.getName();
			}
		}
		
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(data != null && rowIndex < data.size()){
			Map<String, String> values = data.get(rowIndex);
			
			if(values != null && columnIndex < values.size()){
				Object[] object = values.keySet().toArray();
				String key = (String)object[columnIndex];
				
				if(key != null){
					return values.get(key);
				}
			}
		}
		
		return null;
	}

	public void addKeyValueAt(String key, String value, int rowIndex){
		data.get(rowIndex).put(key, value);
		fireTableStructureChanged();
		fireTableDataChanged();
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}
	
	public void clear(){
		metadata = null;
		data = null;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
		fireTableStructureChanged();
	}
	
	public void setData(List<Map<String, String>> data) {
		if(data == null){
			this.data = null;
		}
		else {
			if(metadata != null){
				// meta data is mandatory for aligning columns
				this.data = new ArrayList<Map<String, String>>();
				
				
				for(Map<String, String> row : data){
					Map<String, String> temp = new TreeMap<String, String>();

					for(final String columnName : metadata.keySet()){
						if(row.containsKey(columnName)){
							temp.put(columnName, row.get(columnName));
						}
						else {
							temp.put(columnName, null);
						}
					}
					
					this.data.add(temp);
				}
			}
			else {
				this.data = data;
			}
		}

		fireTableDataChanged();
	}

	public void addMetadata(String key, MetaObject value) {
		metadata.put(key, value);
	}

	public Metadata getMetadata() {
		return metadata;
	}
	
	public List<Map<String, String>> getData() {
		return data;
	}
}
