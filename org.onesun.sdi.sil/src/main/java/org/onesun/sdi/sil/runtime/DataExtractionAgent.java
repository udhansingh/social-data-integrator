package org.onesun.sdi.sil.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.DataReader;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.data.reader.FacetedDataReader;
import org.onesun.sdi.core.data.reader.JSONDataReader;
import org.onesun.sdi.core.data.reader.XMLDataReader;
import org.onesun.sdi.core.metadata.FilterMetadata;
import org.onesun.sdi.core.metadata.Metadata;
import org.onesun.sdi.core.model.DataObject;
import org.onesun.sdi.core.model.Parameter;
import org.onesun.sdi.core.model.Tasklet;
import org.onesun.sdi.core.model.connection.KapowConnection;
import org.onesun.sdi.core.resources.RESTResource;
import org.onesun.sdi.core.resources.WebResource;
import org.onesun.sdi.sil.api.DBService;
import org.onesun.sdi.sil.api.DataService;
import org.onesun.sdi.sil.client.KapowStreamingClient;
import org.onesun.sdi.sil.client.RESTClient;
import org.onesun.sdi.sil.handler.ConnectionHandler;
import org.onesun.sdi.sil.handler.DataHandler;
import org.onesun.sdi.spi.api.ProviderFactory;

import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;

public class DataExtractionAgent {
	private boolean cached = false;
	private	DataService service = null;
	private Tasklet tasklet;
	
	public DataExtractionAgent(){
	}
	
	public Tasklet getTasklet() {
		return tasklet;
	}

	public void setTasklet(Tasklet tasklet) {
		this.tasklet = tasklet;
	}
	
	public void execute(){
		System.out.println("Executing " + ((tasklet != null) ? tasklet.getName() : "NONE"));
		
		Connection cp = tasklet.getConnectionProperties();
		ServiceProvider provider = ProviderFactory.getProvider(cp.getIdentity().toLowerCase(), cp.getCategory());
		
		if(cp.getAuthentication().compareTo("KAPOW") == 0){
			WebResource resource = (WebResource)((WebResource)tasklet.getResource()).clone();
			
			KapowStreamingClient listener = new KapowStreamingClient(
					new DataHandler() {
						@Override
						public void flush(Object object) {
							if(object != null && object instanceof RQLObject){
								RQLObject rqlObject = (RQLObject)object;
								
								if(isCached() == true && service != null){
									if(service instanceof DBService){
										DBService dbs = (DBService)service;
										
										// TODO: Revisit this when needed - Interfaces where changed on Jul 9, 2012
										
//										DataObject dataObject = new DataObject();
//										dataObject.setType("RQLOBJECT");
//										dataObject.setObject(rqlObject);
//										
//										dbs.write(dataObject);
									}
								}
							}
						}
					},
					
					new ConnectionHandler(){
						@Override
						public void setStatusCode(int statusCode) {
						}
					}
			);
			
			listener.setConnectionProperties((KapowConnection) tasklet.getConnectionProperties());
			// TODO: Set the right parameters for Kapow
			listener.setProjectName(null);
			listener.setRobotName(null);
			
			listener.start();
		}
		else if(cp.getAuthentication().compareTo("REST") == 0 || cp.getAuthentication().compareTo("OAUTH") == 0){
			RESTResource resource = (RESTResource)((RESTResource)tasklet.getResource()).clone();
			FilterMetadata filterMetadata = tasklet.getFilterMetadata();
			
			Metadata metadata = (Metadata)tasklet.getMetadata().clone();
			
			String url = resource.getUrl();
			if(url != null && url.trim().length() <= 0) return;
			
			// Set Query Params
			String params = null;
			Collection<Parameter> requestParams = null;
			if(filterMetadata != null) {
				requestParams = filterMetadata.paramValues();
			}
			if(requestParams != null){
				boolean start = true;
				for(Parameter param : requestParams){
					String en = param.getExternalName();
					if(en.startsWith("$")){
						url = url.replace(en, param.getDefaultValue());
					}
					else {
						if(start == true){
							params = "?" + en + "=" + param.getDefaultValue();
							start = false;
						}
						else {
							params += "&" + en + "=" + param.getDefaultValue();
						}
					}
				}
			}
			resource.setParameters(params);
			
			// Update URL
			resource.setUrl(url);
			
			// Set Headers
			Collection<Parameter> requestHeaders = null;
			if(filterMetadata != null){
				requestHeaders = filterMetadata.headerValues();
			}
			if(requestHeaders != null){
				Map<String, String> headers = new HashMap<String, String>();
				for(Parameter header : requestHeaders){
					headers.put(header.getExternalName(), header.getDefaultValue());
				}
				resource.setHeaders(headers);
			}
			
			// Set Payload
			Parameter payloadObject = null;
			if(filterMetadata != null) {
				payloadObject = filterMetadata.getPayload();
			}
			String payload = null;
			if(payloadObject != null){
				payload = payloadObject.getDefaultValue();
			}
			if(payload != null && payload.trim().length() > 0){
				resource.setPayload(payload);
			}
			
			RESTClient listener = new RESTClient(provider, resource, cp.getAuthentication());
			listener.setConnection(cp);
			
			listener.execute();
			
			int status = listener.getResponseCode();
			if(status == 200){
				String responseBody = listener.getResponseBody();
				
				DataReader reader = null;
				switch(resource.getTextFormat()){
				case JSON:{
					try {
						reader = new JSONDataReader(responseBody);
					
					} catch (JSONException e) {
						e.printStackTrace();
					}
					finally{
					}
					
					break;
				}
				
				case XML:{
					reader = new XMLDataReader(responseBody);
					
					break;
				}
				
				case FACETED_JSON:{
					reader = new FacetedDataReader(responseBody);
					
					break;
				}
				
				default:
				break;
				}
				
				if(reader != null){
					reader.setMetadata(metadata);
					reader.initialize();
					reader.load();
					
					List<Map<String, String>> list = reader.getData();
					
					for(Map<String, String> item : list){
						DataObject dataObject = new DataObject();
						
						dataObject.setType("MAP");
						dataObject.setObject(item);
						
						if(isCached() == true && service != null){
							if(service instanceof DBService){
								DBService dbs = (DBService)service;
								// TODO: Revisit this when needed - Interfaces where changed on Jul 9, 2012
								//	dbs.write(dataObject);
							}
						}
					}
				}
			}
			else {
				// TODO: Log errors
			}
			
		}
	}
	
	public boolean isCached() {
		return cached;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}

	public DataService getDataService() {
		return service;
	}

	public void setDataService(DataService service) {
		this.service = service;
	}
	
	public void init(String tableName) throws Exception {
		if(service instanceof DBService){
//			DBService dbs = (DBService)service;
// TODO: Determine the table name;
//			dbs.init(tableName);
			
			throw new Exception("TODO Pending");
		}
	}

	public void deinit() {
		if(service instanceof DBService){
			DBService dbs = (DBService)service;
			dbs.shutdown();
		}
	}
}
