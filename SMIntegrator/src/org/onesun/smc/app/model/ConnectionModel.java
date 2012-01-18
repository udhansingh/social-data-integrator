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

import org.onesun.smc.api.Connector;


public class ConnectionModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6432588878507116532L;
	private List<Connector> connections = new ArrayList<Connector>();

	public ConnectionModel(){
		super();
	}
	
	@Override
	public int getRowCount() {
		return (connections != null) ? connections.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case 0: return "Connections";
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
		Connector connection = connections.get(rowIndex);
		
		switch(columnIndex){
		case 0: return connection.getName();
		}
		
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		connections.add((Connector)aValue);
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
		connections = null;
		connections = new ArrayList<Connector>();
	}

	public void removeRow(int index) {
		if(index >= 0 && index < connections.size()){
			connections.remove(index);
			// fireTableDataChanged();
			fireTableRowsDeleted(index, index);
		}
	}
}
