package org.iyamjeremy.alorarspsbot.api;


public class InstanceWrapper {
	
	private Object instance;
	
	public InstanceWrapper(Object instance) {
		this.instance = instance;
	}
	
	protected Object getInstance() {
		return this.instance;
	}

}
