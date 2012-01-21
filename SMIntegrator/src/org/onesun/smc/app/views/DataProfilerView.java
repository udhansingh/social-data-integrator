package org.onesun.smc.app.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.onesun.smc.api.DataPreviewer;

public class DataProfilerView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1581982691500247615L;
	
	private JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	private MetadataTableView metaView = new MetadataTableView();
	private DataPreviewTableView preView = new DataPreviewTableView();
	
	public DataProfilerView(){
		this.setLayout(new BorderLayout(5, 5));

		initControls();
	}

	private void initControls(){
		mainPane.add(metaView);
		mainPane.add(preView);
		
		metaView.setDataPreviewer(new DataPreviewer() {
			@Override
			public void generate() {
				preView.generateDataPreview();
			}
		});
		
		this.add(mainPane, BorderLayout.CENTER);
	}
}
