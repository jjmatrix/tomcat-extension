package org.jmatrix.tomcat.monitor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.modeler.Registry;
import org.jmatrix.tomcat.monitor.bo.ServerInfo;
import org.jmatrix.tomcat.monitor.core.LogStore;
import org.jmatrix.tomcat.monitor.core.NamedThreadFactory;
import org.jmatrix.tomcat.monitor.core.Store;

/**
 * 收集服务器性能数据
 * 
 * @author jmatrix
 * 
 */
public class ServerInfoCollection {

	/**
	 * Logger
	 */
	private final Log log = LogFactory.getLog(ServerInfoCollection.class);

	private volatile boolean intialized = false;

	private ScheduledExecutorService serivce = null;

	private Store store = null;

	/**
	 * MBean server.
	 */
	protected MBeanServer mBeanServer = null;

	/**
	 * Vector of thread pools object names.
	 */
	protected Vector<ObjectName> threadPools = new Vector<ObjectName>();

	/**
	 * Vector of global request processors object names.
	 */
	protected Vector<ObjectName> globalRequestProcessors = new Vector<ObjectName>();

	public void init() {

		log.info("init start.");

		// Retrieve the MBean server
		mBeanServer = Registry.getRegistry(null, null).getMBeanServer();

		try {
			// Query Thread Pools
			threadPools.clear();
			String onStr = "*:type=ThreadPool,*";
			ObjectName objectName = new ObjectName(onStr);
			Set<ObjectInstance> set = mBeanServer.queryMBeans(objectName, null);
			Iterator<ObjectInstance> iterator = set.iterator();
			while (iterator.hasNext()) {
				ObjectInstance oi = iterator.next();
				threadPools.addElement(oi.getObjectName());
			}

			// Query Global Request Processors
			globalRequestProcessors.clear();
			onStr = "*:type=GlobalRequestProcessor,*";
			objectName = new ObjectName(onStr);
			set = mBeanServer.queryMBeans(objectName, null);
			iterator = set.iterator();
			while (iterator.hasNext()) {
				ObjectInstance oi = iterator.next();
				globalRequestProcessors.addElement(oi.getObjectName());
			}

		} catch (Exception e) {
			log.error("init failed.", e);
		}

		serivce = Executors
				.newSingleThreadScheduledExecutor(new NamedThreadFactory(
						"collect"));

		store = new LogStore();

		this.intialized = true;

		log.info("init succ.");
	}

	public void startMonitor() {

		if (isIntialized()) {
			return;
		}

		init();

		serivce.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				collect();
			}
		}, 30000, 30000, TimeUnit.MICROSECONDS);

	}

	public void collect() {
		if (!isIntialized()) {
			init();
		}
		if (!isIntialized()) {
			log.error("agent do not init.");
			return;
		}
		try {
			Enumeration<ObjectName> enumeration = threadPools.elements();
			while (enumeration.hasMoreElements()) {
				ObjectName objectName = enumeration.nextElement();
				String name = objectName.getKeyProperty("name");

				ServerInfo serverInfo = new ServerInfo();
				serverInfo.setMaxThreads((Integer) mBeanServer.getAttribute(
						objectName, "maxThreads"));
				serverInfo.setCurrentThreadCount((Integer) mBeanServer
						.getAttribute(objectName, "currentThreadCount"));
				serverInfo.setCurrentThreadsBusy((Integer) mBeanServer
						.getAttribute(objectName, "currentThreadsBusy"));
				try {
					Object value = mBeanServer.getAttribute(objectName,
							"keepAliveCount");
					serverInfo.setKeepAliveCount((Integer) value);
				} catch (Exception e) {
					// Ignore
				}

				ObjectName grpName = null;
				Enumeration<ObjectName> reqEnumer = globalRequestProcessors
						.elements();
				while (reqEnumer.hasMoreElements()) {
					ObjectName reqObjName = reqEnumer.nextElement();
					if (name.equals(reqObjName.getKeyProperty("name"))) {
						grpName = reqObjName;
					}
				}

				if (grpName == null) {
					return;
				}

				serverInfo.setMaxTime((Long) mBeanServer.getAttribute(grpName,
						"maxTime"));
				serverInfo.setProcessingTime((Long) mBeanServer.getAttribute(
						grpName, "processingTime"));
				serverInfo.setRequestCount((Integer) mBeanServer.getAttribute(
						grpName, "requestCount"));
				serverInfo.setErrorCount((Integer) mBeanServer.getAttribute(
						grpName, "errorCount"));
				serverInfo.setBytesReceived((Long) mBeanServer.getAttribute(
						grpName, "bytesReceived"));
				serverInfo.setBytesSent((Long) mBeanServer.getAttribute(
						grpName, "bytesSent"));

				store.storeInfo(serverInfo);

			}
		} catch (Exception e) {
			log.error("collect info failed.", e);
		}
	}

	public void stopMonitor() {
		this.intialized = false;
		if (serivce != null && !serivce.isShutdown())
			serivce.shutdown();
	}

	public boolean isIntialized() {
		return intialized;
	}

}