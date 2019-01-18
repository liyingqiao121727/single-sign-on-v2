package com.liyingqiao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.prometheus.client.hotspot.DefaultExports;

public class PrometheusServletContextListener implements ServletContextListener {

	@Override
    public void contextInitialized(final ServletContextEvent sce) {
		DefaultExports.initialize();
	}
	
	@Override
    public void contextDestroyed(final ServletContextEvent sce) {
		
    }
	
}
