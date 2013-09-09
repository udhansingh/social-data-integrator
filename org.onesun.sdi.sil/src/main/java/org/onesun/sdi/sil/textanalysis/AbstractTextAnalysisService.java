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
package org.onesun.sdi.sil.textanalysis;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.commons.text.classification.TextClassificaionHelper;
import org.onesun.sdi.core.metadata.Metadata;
import org.onesun.sdi.core.model.MetaObject;
import org.onesun.sdi.sil.api.TextAnalysisService;
import org.onesun.sdi.sil.data.AbstractDataService;

public abstract class AbstractTextAnalysisService extends AbstractDataService implements TextAnalysisService {
	private static Logger logger = Logger.getLogger(AbstractTextAnalysisService.class);

	protected Object data = null;
	protected Object metadata = null;
	
	@Override
	public final void setData(Object data){
		this.data = data;
	}
	
	@Override
	public final void setMetadata(Object metadata){
		this.metadata = metadata;
	}
	
	protected String columnName = null;

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	protected String[] columns = null;

	@Override
	public final void setColumns(String[] columns){
		this.columns = columns;
	}

	protected final void validateColumn() {
		if(this.metadata instanceof Metadata){
			Metadata metadata = (Metadata)this.metadata;

			boolean columnExists = metadata.containsKey(columnName);
			if(columnExists == false){
				MetaObject mo = new MetaObject();
				mo.setPath(columnName);

				metadata.put(columnName, mo);
			}
		}
	}

	protected abstract void process(Map<String, String> datum, String text);

	@Override
	public final void execute(){
		if(data instanceof List){
			@SuppressWarnings("unchecked")
			List<Object> objects = (List<Object>)data;
			
			for(Object object : objects){
				if(object instanceof Map){
					@SuppressWarnings("unchecked")
					Map<String, String> datum = (Map<String, String>)object;

					String text = "";
					for(String column : columns){
						String columnValue = datum.get(column); 

						if(columnValue != null){
							text += columnValue + "\n";
						}
					}

					text = TextClassificaionHelper.cleanUpAll(text);
					text = (text != null && text.trim().length() > 0) ? text : null;

					logger.info(text);

					if(text == null) return;

					validateColumn();
					process(datum, text);
				}
			}
		}
	}
}
