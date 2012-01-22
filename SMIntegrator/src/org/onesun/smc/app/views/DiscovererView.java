package org.onesun.smc.app.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.onesun.smc.api.DataPreviewer;
import org.onesun.smc.app.handlers.UITask;
import org.onesun.smc.core.metadata.Metadata;

public class DiscovererView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1581982691500247615L;
	
	private JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	private MetadataTableView metaView = new MetadataTableView();
	private DataPreviewTableView preView = new DataPreviewTableView();
	
	public DiscovererView(){
		this.setLayout(new BorderLayout(5, 5));

		initControls();
	}

	private void initControls(){
		mainPane.add(metaView);
		mainPane.add(preView);
		
		metaView.setDataPreviewer(new DataPreviewer() {
			@Override
			public void generate() {
				preView.generateDataPreview(true);
			}
		});
		
		preView.setMetadataRefeshCompleted(new UITask() {
			@Override
			public void execute(Object object) {
				if(object instanceof Metadata){
					Metadata metadata = (Metadata)object;
					
					metaView.setMetadata(metadata);
					metaView.updateView();
					
					metaView.invalidate();
					metaView.repaint();
				}
			}
		});
		
		this.add(mainPane, BorderLayout.CENTER);
	}
}
