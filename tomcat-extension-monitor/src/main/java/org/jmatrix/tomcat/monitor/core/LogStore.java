package org.jmatrix.tomcat.monitor.core;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.jmatrix.tomcat.monitor.bo.ServerInfo;

/**
 * 直接简单的写日志，供测试
 * 
 * @author matrix
 *
 */
public class LogStore implements Store {

	private final Log log = LogFactory.getLog(LogStore.class);

	@Override
	public void storeInfo(ServerInfo serverInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append("maxThreads:").append(serverInfo.getMaxThreads())
				.append(",currentThreadCount:").append(serverInfo.getCurrentThreadCount())
				.append(",busyThreadCount:").append(serverInfo.getCurrentThreadsBusy())
				.append(",keepAliveCount:").append(serverInfo.getKeepAliveCount())
				.append(",maxTime:").append(serverInfo.getMaxTime())
				.append(",requestCount:").append(serverInfo.getRequestCount())
				.append(",errorCount:").append(serverInfo.getErrorCount())
				.append(",processTime:").append(serverInfo.getProcessingTime())
				.append(",bytesRec:").append(serverInfo.getBytesReceived())
				.append(",bytesSend:").append(serverInfo.getBytesSent());
		log.info("ServerInfo:" + builder.toString());
	}

}
