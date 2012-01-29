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

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.onesun.smc.core.model.Parameter;

public class RequestParamModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;
	private List<Parameter> data = new ArrayList<Parameter>();

	private String[] headers = {"External name", "Internal name", "Default Value"};
	
	public List<Parameter> getData(){
		return data;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
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
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0: 
			String v = (String)getValueAt(rowIndex, columnIndex);
			if(v.startsWith("$") && v.endsWith("$")){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Parameter object = data.get(rowIndex);
		
		switch(columnIndex){
		case 0: return object.getExternalName();
		case 1: return object.getInternalName();
		case 2: return object.getDefaultValue();
		}
		
		return null;
	}

	public void add(Parameter aValue){
		data.add(aValue);
		fireTableDataChanged();
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Parameter value = null;
		if(rowIndex >= 0){
			value = data.get(rowIndex);
		}
		
		if(value == null) return;
		
		if(aValue instanceof String){
			switch(columnIndex){
			case 0:
				value.setExternalName((String)aValue);
				break;
			
			case 1:
				value.setInternalName((String)aValue);
				break;
				
			case 2:
				value.setDefaultValue((String)aValue);
				break;
			}
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
	
	public void removeAll(){
		data = null;
		data = new ArrayList<Parameter>();
	}
}
