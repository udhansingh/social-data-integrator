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
package org.onesun.commons.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JTableUtils {
	public static void packColumns(final JTable table, final int margin) {
	    for (int index = 0; index < table.getColumnCount(); index++) {
	        packColumn(table, index, margin);
	    }
	}
	
	private static void packColumn(final JTable table, final int vColIndex, final int margin) {
		DefaultTableColumnModel columnModel = (DefaultTableColumnModel)table.getColumnModel();
		TableColumn column = columnModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = column.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component component = renderer.getTableCellRendererComponent(
				table, column.getHeaderValue(), false, false, 0, 0);
		width = component.getPreferredSize().width;

		// Get maximum width of column data
		for (int r=0; r<table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			component = renderer.getTableCellRendererComponent(
					table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, component.getPreferredSize().width);
		}

		// Add margin
		width += (2 * margin);

		// Set the width
		column.setPreferredWidth(width);
	}

	public static void packRows(JTable table, int margin) {
	    packRows(table, 0, table.getRowCount(), margin);
	}

	private static void packRows(JTable table, int start, int end, int margin) {
	    for (int r=0; r<table.getRowCount(); r++) {
	        // Get the preferred height
	        int h = getPreferredRowHeight(table, r, margin);

	        // Now set the row height using the preferred height
	        if (table.getRowHeight(r) != h) {
	            table.setRowHeight(r, h);
	        }
	    }
	}
	
	private static int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
	    // Get the current default height for all rows
	    int height = table.getRowHeight();

	    // Determine highest cell in the row
	    for (int c=0; c<table.getColumnCount(); c++) {
	        TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
	        Component comp = table.prepareRenderer(renderer, rowIndex, c);
	        int h = comp.getPreferredSize().height + 2*margin;
	        height = Math.max(height, h);
	    }
	    return height;
	}
}
