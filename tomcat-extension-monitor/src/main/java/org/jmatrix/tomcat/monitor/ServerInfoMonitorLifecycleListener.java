package org.jmatrix.tomcat.monitor;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Server;
import org.apache.catalina.Service;

/**
 * 监听Server启动事件，启动任务定时收集服务器状态信息
 * 
 * @author matrix
 * 
 */
public class ServerInfoMonitorLifecycleListener implements LifecycleListener {
	
	private ServerInfoCollection collect = new ServerInfoCollection();
	
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		Lifecycle lifecycle = event.getLifecycle();
		if (Lifecycle.AFTER_START_EVENT.equals(event.getType())
				&& lifecycle instanceof Server) {
			collect.startMonitor();
		}
		if (Lifecycle.BEFORE_STOP_EVENT.equals(event.getType())
				&& lifecycle instanceof Server) {
			collect.stopMonitor();
		}
	}

	private void registerListenersForServer(Server server) {
        for (Service service : server.findServices()) {
            Engine engine = (Engine) service.getContainer();
            System.out.println("engine:"+engine.getName());
            registerListenersForEngine(engine);
        }

    }

    private void registerListenersForEngine(Engine engine) {
        for (Container hostContainer : engine.findChildren()) {
            Host host = (Host) hostContainer;
            System.out.println("host:"+host.getName());
            registerListenersForHost(host);
        }
    }

    private void registerListenersForHost(Host host) {
        for (Container contextContainer : host.findChildren()) {
            Context context = (Context) contextContainer;
            System.out.println("context:"+context.getName());
            registerContextListener(context);
        }
    }

    private void registerContextListener(Context context) {
        context.addLifecycleListener(this);
    }
    
}
