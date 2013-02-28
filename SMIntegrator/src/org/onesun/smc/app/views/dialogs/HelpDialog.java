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
package org.onesun.smc.app.views.dialogs;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import org.onesun.commons.swing.HTMLDocumentLoader;

public class HelpDialog extends AbstractDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8615404465470079746L;
	
	public HelpDialog(Frame parent) {
		super(parent, "Help", true);
		
		setSize(800, 600);
		setResizable(false);
		setVisible(false);
		
		JTextPane htmlPane = new JTextPane();
		htmlPane.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		htmlPane.setContentType("text/html");
		htmlPane.setPreferredSize(new Dimension(800, 600));
		htmlPane.setEditable(false);
		htmlPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				HyperlinkEvent.EventType type = e.getEventType();
			    final URL url = e.getURL();
			    
			    if(type == HyperlinkEvent.EventType.ACTIVATED){
			    	Runnable r = new Runnable() {
			    		@Override
						public void run() {
							try {
								URI uri = url.toURI();
								Desktop.getDesktop().browse(uri);
							} catch (URISyntaxException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					
					SwingUtilities.invokeLater(r);
			    }
			}
		});
		
		try {
			HTMLDocumentLoader loader = new HTMLDocumentLoader();
			URL url = HelpDialog.class.getResource("/html/help.html");
			HTMLDocument doc = loader.loadDocument(url);
			htmlPane.setDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane(htmlPane);
		contentContainer.add(scrollPane, BorderLayout.CENTER);
	}
}