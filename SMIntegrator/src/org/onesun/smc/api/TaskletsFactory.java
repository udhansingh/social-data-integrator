package org.onesun.smc.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.smc.core.model.Tasklet;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TaskletsFactory {
	private static Logger logger = Logger.getLogger(TaskletsFactory.class);
	private static List<Tasklet> tasklets = new ArrayList<Tasklet>();
	
	public static void load(String pathToServicesFile){
		File directory = new File(pathToServicesFile);
		
		if(directory.exists() == true){
			File[] files = directory.listFiles();
			
			for(File file : files){
				logger.info("Processing task: " + file.getAbsolutePath());
				if(file.getName().endsWith(".task")){
					try {
						TaskletImporter importer = new TaskletImporter();
						importer.load(file.getAbsolutePath());
					} catch(NullPointerException e){
						e.printStackTrace();
					}
				}
			}
			
			if(tasklets != null){
				logger.info("Found and loaded " + tasklets.size() + " tasklets");
			}
		}
	}
	
	private static class TaskletImporter extends XMLImporter {
		public TaskletImporter() {
			super();
		}

		@Override
		public boolean load(String pathToImports){
			File file = new File(pathToImports);
			setResource(file);
			
			init();
			process();
			
			if(tasklets != null && tasklets.size() > 0){
				return true;
			}
			
			return false;
		}
		
		@Override
		public void process() {
		}

		@Override
		public void parse(Object object) {
			if(object != null && object instanceof Document){
				Document document = (Document)object;

				Tasklet tasklet = new Tasklet();
				Element root = document.getDocumentElement();
				tasklet.setIdentity(root.getAttribute("identity"));
				tasklet.setName(root.getAttribute("name"));

				NodeList nodes = root.getElementsByTagName("item");
				if(nodes != null && nodes.getLength() > 0){
					for(int index = 0; index < nodes.getLength(); index++){
						Element element = (Element)nodes.item(index);

						try{
							// process connection
							
							// process resource
							
							// process metadata
							
							// process filter

						}catch(Exception e){
							logger.error("Exception while extracting tasks : " + e.getMessage());
						}
					}
				}
				
				tasklets.add(tasklet);
			}
		}
	}

	public static List<Tasklet> getTasklets() {
		return tasklets;
	}
}
