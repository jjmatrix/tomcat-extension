package org.jmatrix.tomcat.monitor.core;

/**
 * store创建工厂
 * 
 * @author matrix
 *
 */
public interface StoreFactory {
	public Store createStore(int type);
}
