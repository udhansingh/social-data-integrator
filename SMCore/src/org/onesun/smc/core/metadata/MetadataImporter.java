package org.onesun.smc.core.metadata;

import java.io.File;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.DataTypeFactory;
import org.onesun.smc.core.model.MetaObject;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MetadataImporter extends XMLImporter {
	private static Logger logger = Logger.getLogger(MetadataImporter.class);

	private Metadata metadata = new Metadata();

	public Metadata getMetadata(){
		return metadata;
	}
	
	public MetadataImporter() {
		super();
	}

	@Override
	public void process() {
	}

	@Override
	public void parse(Object object) {
		if(object != null && object instanceof Document){
			Document document = (Document)object;

			Element root = document.getDocumentElement();
			
			metadata.setVerb(XMLUtils.getValue(root, "verb"));
			metadata.setUrl(XMLUtils.getValue(root, "url"));
			
			String value = XMLUtils.getValue(root, "discovered");
			metadata.setDiscovered(Boolean.parseBoolean((value == null)? "false" : value));

			metadata.setName(XMLUtils.getValue(root, "name"));
			metadata.setNodeName(XMLUtils.getValue(root, "nodeName"));

			NodeList nodes = root.getElementsByTagName("item");
			if(nodes != null && nodes.getLength() > 0){
				for(int index = 0; index < nodes.getLength(); index++){
					Element element = (Element)nodes.item(index);

					try{
						MetaObject item = new MetaObject();
						item.setName(XMLUtils.getValue(element, "name"));
						item.setPath(XMLUtils.getValue(element, "path"));
						item.setType(
								DataTypeFactory.getDataType(
										XMLUtils.getValue(element, "type")
										)
								);

						metadata.put(item.getPath(), item);
					}catch(Exception e){
						logger.error("Exception while extracting subscriptions : " + e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public boolean load(String pathToImports) {
		File file = new File(pathToImports);
		setResource(file);

		init();
		process();

		if(metadata.size() > 0){
			return true;
		}

		return false;
	}
}