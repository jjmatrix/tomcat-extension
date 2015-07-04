package org.jmatrix.tomcat.monitor;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

/**
 * 请求上报
 * 
 * @author matrix
 *
 */
public class RequstReportValve extends ValveBase {

	@Override
	public void invoke(Request request, Response response) throws IOException,
			ServletException {
		long startTime = System.currentTimeMillis();
		try {
			getNext().invoke(request, response);
		} finally {
			long timeConsumer = System.currentTimeMillis() - startTime;
			// report
		}
	}

}
