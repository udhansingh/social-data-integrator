package org.onesun.sdi.swing.app.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.sdi.core.model.Tasklet;
import org.onesun.sdi.core.tools.XMLImporter;
import org.w3c.dom.Document;

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

				Tasklet tasklet = Tasklet.toTasklet(document);
				tasklets.add(tasklet);
			}
		}
	}

	public static List<Tasklet> getTasklets() {
		return tasklets;
	}
}
