package org.onesun.smc.core.metadata;

import java.io.File;

import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MetadataImporter extends XMLImporter {
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
			
			metadata = Metadata.toMetadata(root);
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