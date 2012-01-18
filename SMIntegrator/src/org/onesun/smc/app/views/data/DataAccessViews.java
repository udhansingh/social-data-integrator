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
package org.onesun.smc.app.views.data;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.onesun.smc.app.AppMessages;

public final class DataAccessViews {
	public static final RESTDataAccessView 			RESTFUL_DATA_ACCESS_INPUT_VIEW	= new RESTDataAccessView();
	public static final SocialMediaDataAccessView 		SOCIAL_DATA_ACCESS_LIST_VIEW	= new SocialMediaDataAccessView();
	public static final FileDataAccessView 				FILE_DATA_ACCESS_VIEW			= new FileDataAccessView();
	public static final WebDataAccessView 				WEB_DATA_ACCESS_VIEW			= new WebDataAccessView();
	public static final StreamingDataAccessView 		STREAMING_DATA_ACCESS_VIEW		= new StreamingDataAccessView();
	
	public static final JPanel 							DEFAULT_DATA_ACCESS_VIEW		= new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
	
	static {
		final String text = "<html><body>" +
				"<h2>" +
				AppMessages.INFORMATION_CHOOSE_A_CONNECTION_OR_UNSUPPORTED_FEATURE +
				"</h2>" +
				"</body></html>";
		JLabel label = new JLabel(text);
		
		DEFAULT_DATA_ACCESS_VIEW.add(label);
	}
}
