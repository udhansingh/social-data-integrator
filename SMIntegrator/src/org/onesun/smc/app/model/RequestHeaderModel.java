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

import org.onesun.smc.core.model.RequestHeaderObject;

public class RequestHeaderModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;
	private List<RequestHeaderObject> data = new ArrayList<RequestHeaderObject>();

	private String[] headers = {"Name", "Value"};
	
	public List<RequestHeaderObject> getData(){
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RequestHeaderObject object = data.get(rowIndex);
		
		switch(columnIndex){
		case 0: return object.getName();
		case 1: return object.getValue();
		}
		
		return null;
	}

	public void add(RequestHeaderObject aValue){
		data.add(aValue);
		fireTableDataChanged();
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data.add((RequestHeaderObject)aValue);
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
		data = new ArrayList<RequestHeaderObject>();
	}
}
