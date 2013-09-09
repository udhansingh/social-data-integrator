package org.onesun.sdi.core.api;

import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.sdi.core.resources.FileResource;
import org.onesun.sdi.core.resources.RESTResource;
import org.onesun.sdi.core.resources.StreamingResource;
import org.onesun.sdi.core.resources.WebResource;
import org.w3c.dom.Element;

public class ResourceFactory {
	private ResourceFactory() {}
	
	private static Map<String, Class<?>> resources = new TreeMap<String, Class<?>>();
	
	static {
		resources.put("FILE", FileResource.class);
		resources.put("REST", RESTResource.class);
		resources.put("STREAMING", StreamingResource.class);
		resources.put("WEB", WebResource.class);
	}
	
	public static Resource toResource(String type, 
			Element element) throws ParserConfigurationException, InstantiationException, IllegalAccessException{
		// Process Properties
		Resource resource = null;
		
		if(resources.containsKey(type)){
			Class<?> instance = resources.get(type);

			if(instance != null){
				resource = (Resource)instance.newInstance();
				resource.toResource(element);
			}
		}
		
		return resource;
	}
}
