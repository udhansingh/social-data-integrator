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

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.onesun.smc.core.metadata.Metadata;
import org.onesun.smc.core.model.DataType;
import org.onesun.smc.core.model.MetaObject;

public class MetadataTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;

	private Metadata metadata = null;
	
	private String[] headers = {"Path", "Name", "Type", "Size", "Ignore"};
	
	@Override
	public int getRowCount() {
		if(metadata != null) return metadata.size();
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
		if(metadata == null) return null;
		
		Object[] items = metadata.keySet().toArray();
		String key = (String)items[rowIndex];
		
		MetaObject meta = metadata.get(key);
		
		switch(columnIndex){
		case 4: return meta.isIgnore();
		case 3: return meta.getSize();
		case 2: return meta.getType();
		case 1: return meta.getName();
		case 0: return meta.getPath();
		}
		
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(metadata == null) return;
		
		Object[] items = metadata.keySet().toArray();
		String key = (String)items[rowIndex];
		MetaObject object = metadata.get(key);
		
		switch(columnIndex){
		case 4:
			if(aValue instanceof Boolean){
				object.setIgnore((Boolean)aValue);
				metadata.put(key, object);
			}
			break;
		case 3:
			if(aValue instanceof Integer){
				object.setSize((Integer)aValue);
				metadata.put(key, object);
			}
			break;
		case 2:
			if(aValue instanceof DataType){
				object.setType((DataType)aValue);
				metadata.put(key, object);
			}
			break;
			
		default:
			metadata.put(key, object);
			break;
		}
		
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
	
	public void setMetadata(Metadata m) {
		this.metadata = m;
		fireTableStructureChanged();
		fireTableDataChanged();
	}

	public Metadata getMetadata() {
		return metadata;
	}
}
