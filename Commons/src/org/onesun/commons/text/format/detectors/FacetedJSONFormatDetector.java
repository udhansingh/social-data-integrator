package org.onesun.commons.text.format.detectors;

import java.io.InputStream;

public class FacetedJSONFormatDetector extends AbstractFormatDetector {
	
	

	@Override
	protected void process(InputStream is) throws Exception {
		format = TextFormat.UNKNOWN;
		
	}

}
