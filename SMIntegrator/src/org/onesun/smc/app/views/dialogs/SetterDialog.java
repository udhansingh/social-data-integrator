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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;

import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.handlers.RequestUpdateHandler;
import org.onesun.smc.app.handlers.UserAction;
import org.onesun.smc.app.handlers.UserActionListener;
import org.onesun.smc.app.model.BusinessObject;
import org.onesun.smc.app.views.data.SetterView;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.model.RequestParamObject;

public class SetterDialog extends AbstractDialog {
	private static final long serialVersionUID = 8615404465470079746L;

	private SetterView view = new SetterView();
	private RequestUpdateHandler handler = null;
	
	public SetterView getView(){
		return view;
	}
	
	public SetterDialog(Frame parent) {
		super(parent, "Setup Request", true);
		
		setSize(700, 600);
		setResizable(false);
		setVisible(false);

		contentContainer.add(view, BorderLayout.CENTER);
		
		JButton closeButton = new JButton("Close");
		buttonContainer.add(closeButton);
		UserActionListener listener = null;

		listener = new UserActionListener(okButton, new UserAction() {
			@Override
			public void execute(ActionEvent event) {
				onOkButtonClicked(event);
				
				setVisible(false);
			}
		});
		okButton.addActionListener(listener);
		
		listener = new UserActionListener(closeButton, new UserAction() {
			@Override
			public void execute(ActionEvent event) {
				setVisible(false);
			}
		});
		closeButton.addActionListener(listener);
	}

	void onOkButtonClicked(ActionEvent event) {
		if(handler != null){
			List<RequestParamObject> params = view.getParams();
			List<RequestParamObject> headers = view.getHeaders();
			String requestBody = view.getRequestBody();
			
			BusinessObject bobj = AppCommons.BUSINESS_OBJECT;
			FilterMetadata fm = bobj.getFilterMetadata();
			
			if(fm == null){
				fm = new FilterMetadata();
				bobj.setFilterMetadata(fm);
			}
			
			if(params != null){
				Map<String, RequestParamObject> paramMap = Collections.synchronizedMap(new TreeMap<String, RequestParamObject>());
				for(RequestParamObject o : params){
					paramMap.put(o.getInternalName(), o);
				}
				
				fm.setParams(paramMap);
			}
			else {
				fm.setParams(null);
			}
			if(headers != null){
				Map<String, RequestParamObject> headerMap = Collections.synchronizedMap(new TreeMap<String, RequestParamObject>());
				for(RequestParamObject o : headers){
					headerMap.put(o.getInternalName(), o);
				}
				fm.setHeaders(headerMap);
			}
			else {
				fm.setHeaders(null);
			}
			
			fm.setPayload(new RequestParamObject("payload", "param_payload", requestBody));
			
			handler.update(fm);
			
			// Update log
			AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
		}
	}

	public void setRequestUpdateHandler(RequestUpdateHandler handler) {
		this.handler = handler;
	}
}