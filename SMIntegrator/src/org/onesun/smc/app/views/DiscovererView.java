package org.onesun.smc.app.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.onesun.smc.api.DataPreviewer;
import org.onesun.smc.app.handlers.UITask;
import org.onesun.smc.core.metadata.Metadata;

public class DiscovererView extends JPanel {
	private static final long serialVersionUID = 1284906946001890574L;
	
	private MetadataTableView metadataTableView = new MetadataTableView();	
	private DataPreviewTableView dataPreviewTableView = new DataPreviewTableView();
	
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, metadataTableView, dataPreviewTableView);
	
	public DiscovererView(){
		super();

		this.setLayout(new BorderLayout(5, 5));

		initControls();
	}

	private void initControls(){
		metadataTableView.setDataPreviewer(new DataPreviewer() {
			@Override
			public void generate() {
				dataPreviewTableView.generateDataPreview(true);
			}
		});
		
		dataPreviewTableView.setMetadataRefeshCompleted(new UITask() {
			@Override
			public void execute(Object object) {
				if(object instanceof Metadata){
					Metadata metadata = (Metadata)object;
					
					metadataTableView.setMetadata(metadata);
					metadataTableView.updateView();
					
					metadataTableView.invalidate();
					metadataTableView.repaint();
				}
			}
		});
		
		splitPane.setDividerSize(5);
		
		double weight = splitPane.getResizeWeight(); // 0.0 by default
	    weight = .5D;
	    splitPane.setResizeWeight(weight);
	    
		this.add(splitPane);
	}
}
