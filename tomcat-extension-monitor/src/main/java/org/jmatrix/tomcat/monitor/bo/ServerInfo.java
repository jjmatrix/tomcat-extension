package org.jmatrix.tomcat.monitor.bo;

/**
 * 线程池信息
 * 
 * @author matrix
 *
 */
public class ServerInfo {

	private int maxThreads;

	private int currentThreadCount;

	private int currentThreadsBusy;

	private int keepAliveCount;

	/**
	 * processor info
	 */
	private long maxTime;

	private long processingTime;

	private int requestCount;

	private int errorCount;

	private long bytesReceived;

	private long bytesSent;

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getCurrentThreadCount() {
		return currentThreadCount;
	}

	public void setCurrentThreadCount(int currentThreadCount) {
		this.currentThreadCount = currentThreadCount;
	}

	public int getCurrentThreadsBusy() {
		return currentThreadsBusy;
	}

	public void setCurrentThreadsBusy(int currentThreadsBusy) {
		this.currentThreadsBusy = currentThreadsBusy;
	}

	public int getKeepAliveCount() {
		return keepAliveCount;
	}

	public void setKeepAliveCount(int keepAliveCount) {
		this.keepAliveCount = keepAliveCount;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public long getBytesReceived() {
		return bytesReceived;
	}

	public void setBytesReceived(long bytesReceived) {
		this.bytesReceived = bytesReceived;
	}

	public long getBytesSent() {
		return bytesSent;
	}

	public void setBytesSent(long bytesSent) {
		this.bytesSent = bytesSent;
	}

}
