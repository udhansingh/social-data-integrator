package org.onesun.smc.core.model;

import org.onesun.smc.api.DataTypeFactory;

public class MetaObject implements Cloneable {
	private String name;
	private String path;
	private DataType type = DataTypeFactory.getDataType("String");
	private Boolean ignore = false;
	private Integer size = 0;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Boolean isIgnore() {
		return ignore;
	}
	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return this;
		}
	}
}
