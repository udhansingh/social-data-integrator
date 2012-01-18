/*
 **
	Copyright 2010 Udaya Kumar (Udy)

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 **	
 */
package org.onesun.commons.text.classification.uclassify;

public enum Classifier {
	AGE_ANALYZER("http://uclassify.com/browse/uClassify", "Ageanalyzer")
	, TEXT_LANGUAGE("http://uclassify.com/browse/uClassify", "Text%20Language")
	, MOOD("http://www.uclassify.com/browse/prfekt", "Mood")
	, TONALITY("http://uclassify.com/browse/prfekt", "Tonality")
	, TOPICS("http://uclassify.com/browse/uClassify", "Topics")
	, SENTIMENT("http://uclassify.com/browse/uClassify", "Sentiment");

	private String url = null;
	private String classifier = null;

	public String getUrl() {
		return url;
	}

	public String getClassifier(){
		return classifier;
	}
	
	private Classifier(String url, String classifier) {
		this.url = url;
		this.classifier = classifier;
	}
}