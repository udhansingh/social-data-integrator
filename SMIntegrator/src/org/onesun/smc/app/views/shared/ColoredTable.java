/*
   Copyright 2011 Udayakumar Dhansingh (Udy)
   				  Vikas Kumar Jha

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
package org.onesun.smc.app.views.shared;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.smc.app.views.ColoredTableView;

public class ColoredTable extends JTable implements MouseListener {
	private static final long serialVersionUID = -5023452298923221116L;
	private TextFormat dataType = TextFormat.XML;

	public TextFormat getDataType() {
		return dataType;
	}

	public ColoredTable(TableModel model) {
		super(model);
		initialize();

	}

	public ColoredTable() {
		// TODO Auto-generated constructor stub
		super();
		initialize();
	}

	private void initialize() {
		this.setRowSelectionAllowed(true);
		addMouseListener(this);
	}

	public void setDataType(TextFormat dataType) {
		this.dataType = dataType;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
			int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		String cellText = (String) this.getValueAt(rowIndex, vColIndex);
		if (cellText != null
				&& ((cellText.endsWith("]") && cellText.startsWith("[") && dataType == TextFormat.JSON) || (cellText
						.endsWith(">") && cellText.startsWith("<") && dataType == TextFormat.XML))) {
			c.setBackground(Color.decode("0xE0FFE0"));
		} else {
			c.setBackground(getBackground());
		}
		c.setForeground(Color.BLACK);
		return c;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 2) {
			JTable table = (JTable) e.getSource();
			int col = table.getSelectedColumn();
			int row = table.getSelectedRow();
			ColoredTableView dataFrame = new ColoredTableView(
					(String) table.getValueAt(row, col), dataType);
			dataFrame.setTitle(table.getColumnName(col));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
