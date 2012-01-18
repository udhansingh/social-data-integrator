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
package org.onesun.smc.core.providers.socialmedia;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.Exporter;
import org.onesun.smc.api.Importer;
import org.onesun.smc.api.SocialMediaProvider;
import org.onesun.smc.core.model.Authentication;
import org.onesun.smc.core.model.OAuthVersion;
import org.onesun.smc.core.resources.RESTResource;
import org.scribe.builder.api.Api;
import org.scribe.model.Verb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public abstract class AbstractSocialMediaProvider implements SocialMediaProvider, Exporter, Importer {
	private static Logger logger = Logger.getLogger(AbstractSocialMediaProvider.class);

	protected OAuthVersion oauthVersion = OAuthVersion.VERSION_1;

	protected String apiProvider = null;
	protected String developerUrl = null;
	protected Class<? extends Api> apiClass = null;
	protected List<String> apiScopeList = null;
	protected List<RESTResource> resources = null;

	@Override
	public String getCategory() {
		return "SOCIAL_MEDIA";
	}

	@Override
	public abstract String getIdentity();

	@Override
	final public Authentication getAuthentication() {
		return Authentication.OAUTH;
	}

	public OAuthVersion getOAuthVersion(){
		return oauthVersion;
	}

	public String getApiProvider() {
		return apiProvider;
	}

	public void setApiProvider(String apiProvider) {
		this.apiProvider = apiProvider;
	}

	public String getDeveloperUrl() {
		return developerUrl;
	}

	public void setDeveloperUrl(String developerUrl) {
		this.developerUrl = developerUrl;
	}

	public Class<? extends Api> getApiClass() {
		return apiClass;
	}

	public void setApiClass(Class<? extends Api> apiClass) {
		this.apiClass = apiClass;
	}

	public List<String> getApiScopeList() {
		return apiScopeList;
	}

	public void setApiScopeList(List<String> apiScopeList) {
		this.apiScopeList = apiScopeList;
	}

	public List<RESTResource> getResources() {
		return resources;
	}

	@Override
	public List<RESTResource> getResourcesByScope(String scope) {
		return null;
	}

	public void setResources(List<RESTResource> resources) {
		this.resources = resources;
	}

	public AbstractSocialMediaProvider(){
		super();
	}

	@Override
	public abstract void init();

	@Override
	public final boolean isResponseRequired() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean load(String pathToExports) {
		apiProvider = getIdentity();
		
		String dirPath = pathToExports + getIdentity() + "/";
		File dir = new File(dirPath);
		if(dir.exists() == false){
			return false;
		}

		File file = null;
		Document document = null;
		InputStream is = null;
		String path = null;


		int count = 0;
		try {
			// Process properties
			path = dirPath + "properties.xml";

			file = new File(path);
			if(file.exists() == true){
				is = new FileInputStream(file);

				if(is != null){
					document = XMLUtils.toDocument(is);

					Element root = document.getDocumentElement();

					String value = null;

					value = XMLUtils.getAttributeValue(root, "apiClass");
					try {
						Class<?> clazz = Class.forName(value);
						apiClass = (Class<? extends Api>)clazz;
						value = XMLUtils.getAttributeValue(root, "developerUrl");
						developerUrl = value;
						value = XMLUtils.getAttributeValue(root, "oauthVersion");
						oauthVersion = OAuthVersion.valueOf(value);

						count++;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		}


		try {
			// Process scope
			path = dirPath + "scopes.xml";

			file = new File(path);
			if(file.exists() == true){
				is = new FileInputStream(file);

				if(is != null){
					document = XMLUtils.toDocument(is);

					Element root = document.getDocumentElement();

					NodeList nodeList = root.getChildNodes();
					if(nodeList.getLength() > 0 && apiScopeList == null){
						apiScopeList = new ArrayList<String>();
					}
					for(int index = 0; index < nodeList.getLength(); index++){
						Node node = nodeList.item(index);

						if(node.getNodeName().compareTo("scope") == 0){
							String value = node.getTextContent();
							
							if(value != null){
								apiScopeList.add(value);
							}
						}
					}

					if(apiScopeList.size() > 0){
						count++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		}

		try {
			// Process resources
			path = dirPath + "resources.xml";

			file = new File(path);
			if(file.exists() == true){
				is = new FileInputStream(file);

				if(is != null){
					document = XMLUtils.toDocument(is);

					Element root = document.getDocumentElement();

					NodeList nodeList = root.getChildNodes();
					if(nodeList.getLength() > 0 && resources == null){
						resources = new ArrayList<RESTResource>();
					}
					for(int index = 0; index < nodeList.getLength(); index++){
						Node node = nodeList.item(index);

						RESTResource r = new RESTResource();
						
						if(node.getNodeName().compareTo("resource") == 0){
							NodeList children = node.getChildNodes();

							if(children != null){
								for(int rindex = 0; rindex < children.getLength(); rindex++) {
									Node child = children.item(rindex);


									if(child.getNodeName().compareTo("resourceName") == 0){
										r.setResourceName(child.getTextContent());
									} else if(child.getNodeName().compareTo("url") == 0){
										r.setUrl(child.getTextContent());
									} else if(child.getNodeName().compareTo("verb") == 0){
										r.setVerb(Verb.valueOf(child.getTextContent()));
									} else if(child.getNodeName().compareTo("isAccessTokenRequired") == 0){
										r.setAccessTokenRequired(Boolean.valueOf(child.getTextContent()));
									}

								}
							}
						}
						
						if(r.getResourceName() != null && r.getUrl() != null){
							resources.add(r);
						}
					}

					if(resources.size() > 0){
						count++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		}

		if(count > 0){
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean save(String pathToExports) {
		try {
			String dirPath = pathToExports + getIdentity() + "/";
			File dir = new File(dirPath);
			if(dir.exists() == false){
				logger.info("Export: " + dir.getAbsolutePath());
				dir.mkdirs();
			}

			DocumentBuilderFactory documentBuilderFactory = 
					DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = 
					documentBuilderFactory.newDocumentBuilder();
			Document document = null;
			Element root = null;

			OutputFormat format = null;
			String path = null;
			File file = null;

			// apiClass can't be null ... Provider must be initialized (lazy loading)
			if(apiClass == null){
				init();
			}

			// Write properties
			path = dirPath + "properties.xml";
			file = new File(path);
			if(file.exists() == false){
				document = documentBuilder.newDocument();
				root = document.createElement("root");
				root.setAttribute("oauthVersion", oauthVersion.name());
				root.setAttribute("developerUrl", developerUrl);
				root.setAttribute("apiClass", apiClass.getCanonicalName());
				root.setAttribute("category", getCategory());
				root.setAttribute("identity", getIdentity());
				root.setAttribute("authentication", getAuthentication().name());
				document.appendChild(root);

				format = new OutputFormat(document);
				format.setIndenting(true);

				XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
				logger.info("Serializing completed for: " + file.getAbsolutePath());
				serializer.serialize(document);
			}

			// Write Scope
			path = dirPath + "scopes.xml";
			file = new File(path);
			if(file.exists() == false && apiScopeList != null && apiScopeList.size() > 0){
				document = documentBuilder.newDocument();
				root = document.createElement("root");

				for(String apiScope : apiScopeList){
					Element scope = document.createElement("scope");
					scope.setTextContent(apiScope);

					root.appendChild(scope);
				}

				document.appendChild(root);

				format = new OutputFormat(document);
				format.setIndenting(true);

				XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
				logger.info("Serializing completed for: " + file.getAbsolutePath());
				serializer.serialize(document);
			}

			// Write Resources
			path = dirPath + "resources.xml";
			file = new File(path);
			if(file.exists() == false && resources != null && resources.size() > 0){
				document = documentBuilder.newDocument();
				root = document.createElement("root");

				for(RESTResource resource : resources){
					Element e = null;
					Element r = document.createElement("resource");

					e = document.createElement("resourceName");
					e.setTextContent(resource.getResourceName());
					r.appendChild(e);


					String text = null;

					e = document.createElement("url");
					text = StringEscapeUtils.escapeXml(resource.getUrl());
					e.setTextContent(text);
					r.appendChild(e);

					e = document.createElement("verb");
					e.setTextContent(resource.getVerb().name());
					r.appendChild(e);


					text = resource.getTextFormat().name();
					if(text != null && text.trim().compareToIgnoreCase("UNKNOWN") != 0) {
						e = document.createElement("textFormat");
						e.setTextContent(text);
						r.appendChild(e);
					}

					text = resource.getPayload();
					if(text != null && text.trim().length() > 0) {
						e = document.createElement("payload");
						e.setTextContent(text);
						r.appendChild(e);
					}

					text = resource.getParameters();
					if(text != null && text.trim().length() > 0) {
						e = document.createElement("parameters");
						e.setTextContent(text);
						r.appendChild(e);
					}

					boolean flag = resource.isAccessTokenRequired();
					e = document.createElement("isAccessTokenRequired");
					e.setTextContent(Boolean.toString(flag));
					r.appendChild(e);

					// Write Headers
					Map<String, String> headers = resource.getHeaders();
					if(headers != null && headers.size() > 0){
						e = document.createElement("headers");

						for(String key : headers.keySet()){
							Element he = document.createElement("header");
							he.setAttribute(key, headers.get(key));

							e.appendChild(he);
						}

						r.appendChild(e);
					}

					// Write to parent
					root.appendChild(r);
				}

				document.appendChild(root);

				format = new OutputFormat(document);
				format.setIndenting(true);

				XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
				logger.info("Serializing completed for: " + file.getAbsolutePath());
				serializer.serialize(document);

				return true;
			}

		} catch (ParserConfigurationException e) {
			logger.error("ParserConfigurationException while creating new document instance: " + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException while creating new document instance: " + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException while creating new document instance: " + e.getMessage());
		}finally {
		}

		return false;
	}
}
