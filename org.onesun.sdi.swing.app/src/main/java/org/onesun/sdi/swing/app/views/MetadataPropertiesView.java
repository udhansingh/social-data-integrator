package org.onesun.sdi.swing.app.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.sdi.swing.app.model.MetadataPropertiesModel;

public class MetadataPropertiesView extends JPanel {
	private static final long serialVersionUID = 642973035480695988L;

	private static final Map<String, String> defaultPropertiesMap = new TreeMap<String, String>();
	{
		defaultPropertiesMap.put("paging.enabled", "false");
		defaultPropertiesMap.put("paging.node.name", "");
		defaultPropertiesMap.put("api.throttled", "false");
		defaultPropertiesMap.put("api.request.max", "-1");
		defaultPropertiesMap.put("api.request.time", "-1");
		defaultPropertiesMap.put("api.request.time.unit", "seconds");
		defaultPropertiesMap.put("data.node.name", "");
		defaultPropertiesMap.put("data.format", "");
		defaultPropertiesMap.put("data.sampling.mode", "single");
		defaultPropertiesMap.put("schema.identity", "<<untitled>>");
		defaultPropertiesMap.put("schema.discovered", "true");
		defaultPropertiesMap.put("http.request.url", "");
		defaultPropertiesMap.put("http.request.method", "GET");
	}
	
	private MetadataPropertiesModel model = null;
	private JTable table = null;
	private JScrollPane scrollPane = null;
	
	public MetadataPropertiesView() {
		super();
		
		this.setLayout(new BorderLayout(5, 5));
		
		initControls();
	}
	
	private void initControls(){
		model = new MetadataPropertiesModel();
		
		model.load(defaultPropertiesMap);
		
		table = new JTable(model);
		scrollPane = new JScrollPane(table);;
		
		JPanel panel = null;
		JLabel label = null;
		
		panel = new JPanel(new SpringLayout());
		label = new JLabel("Schema Properties", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(label);
		label.setLabelFor(scrollPane);
		panel.add(scrollPane);
		
		SpringLayoutUtils.makeCompactGrid(panel, 2, 1, 5, 5,	0, 0);
		
		this.add(panel, BorderLayout.CENTER);
	}
}
