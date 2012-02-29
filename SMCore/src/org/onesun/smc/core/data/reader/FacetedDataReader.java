package org.onesun.smc.core.data.reader;

import java.util.List;

import org.onesun.smc.api.DataReader;
import org.onesun.smc.api.MetadataReader;
import org.onesun.smc.core.metadata.JSONMetadataReader;
import org.onesun.smc.core.metadata.Metadata;

public class FacetedDataReader extends AbstractDataReader {
	private Object object;
	
	public FacetedDataReader(Object object){
		this.object = object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		try {
			List<String> facetedJSONData = (List<String>) object;
			for (String jsonData : facetedJSONData) {

				MetadataReader metadatareader = new JSONMetadataReader(jsonData);
				metadatareader.initialize();
				metadatareader.generateMetadata();
				Metadata discoveredMetadata = metadatareader.getMetadata();
				if (isMatchMetadata(discoveredMetadata)) {
					DataReader dataReader = new JSONDataReader(jsonData);
					dataReader.setMetadata(metadata);
					dataReader.initialize();
					dataReader.load();
					data.addAll(dataReader.getData());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}
	
private float toleranceRatio = 0.1f;
	
	private boolean isMatchMetadata(Metadata discoveredMetadata){
		int tolerance = (int) (discoveredMetadata.size()*toleranceRatio);
				if(Math.abs(discoveredMetadata.size()-metadata.size()) > tolerance){
					return false;
				}
				int diff = 0;
				for(String field : discoveredMetadata.keySet()){
					if(!metadata.containsKey(field))
						diff++;
					if(diff > tolerance)
						return false;
				}
				if(diff <= tolerance)
					return true;
		return false;
	}

}
