package org.jmatrix.tomcat.monitor.core;

import org.jmatrix.tomcat.monitor.bo.ServerInfo;

/**
 * 信息存储
 * 
 * @author matrix
 *
 */
public interface Store {
	public void storeInfo(ServerInfo serverInfo);
}
