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
package org.onesun.commons.text.format.detectors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.codehaus.jackson.JsonParseException;
import org.onesun.commons.text.format.detectors.FormatDetector;
import org.onesun.commons.text.format.detectors.JSONFormatDetector;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.text.format.detectors.XMLFormatDetector;

public class TextFormatDetector {
	// Using TreeMap to have an ordered execution of Analyzers
	private static List<FormatDetector> formatDetectors = new ArrayList<FormatDetector>();
	
	static {
		formatDetectors.add(new JSONFormatDetector());
		formatDetectors.add(new XMLFormatDetector());
		formatDetectors.add(new FacetedJSONFormatDetector());
	};
	
	private TextFormat format = TextFormat.UNKNOWN;
	public TextFormat getFormat(){
		return format;
	}

	public TextFormat checkFormat(Object input){
		if(input == null) {
			return format;
		}
		
		// Apply stack of detectors
		for(FormatDetector formatDetector : formatDetectors){
			try {
				if(formatDetector != null) {
					if(input instanceof String){
						formatDetector.analyze((String) input);
					}
					else if(input instanceof File){
						formatDetector.analyze((File) input);
					}

					format = formatDetector.getFormat();

					// continue only if it's unknown
					if(format != null && format.compareTo(TextFormat.UNKNOWN) == 0){
						continue;
					}
					else {
						break;
					}
				}

			} catch(ClassCastException e){
			} catch (JsonParseException e){
			} catch(XMLStreamException e){
			} catch(IllegalArgumentException e){
			} catch (Exception e) {
			} finally {
			}

		}
		
		return format;
	}
}