package org.onesun.smc.core.providers.web.kapow;

import com.kapowtech.robosuite.api.java.repository.construct.InputObject;
import com.kapowtech.robosuite.api.java.repository.construct.Project;
import com.kapowtech.robosuite.api.java.repository.construct.Robot;
import com.kapowtech.robosuite.api.java.repository.construct.Type;

public class KapowObject {
	private Project project;
	private Robot robot;
	private Type[] types;
	private InputObject[] inputObjects;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	
	public Robot getRobot() {
		return robot;
	}

	public void setReturnedTypes(Type[] types) {
		this.types = types;
	}
	
	public Type[] getReturnedTypes(){
		return types;
	}

	public void setInputObjects(InputObject[] inputObjects) {
		this.inputObjects = inputObjects;
	}
	
	public InputObject[] getInputObjects(){
		return inputObjects;
	}
}
