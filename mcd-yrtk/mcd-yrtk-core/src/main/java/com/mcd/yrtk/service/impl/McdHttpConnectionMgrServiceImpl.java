package com.mcd.yrtk.service.impl;

import com.mcd.yrtk.service.McdHttpConnectionMgrService;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;


@Component(label = "McDonald's YRTK HTTP client serivce", metatype = true)
@Service(McdHttpConnectionMgrService.class)
public final class McdHttpConnectionMgrServiceImpl implements McdHttpConnectionMgrService {

    public static final Logger LOGGER = LoggerFactory.getLogger(McdHttpConnectionMgrService.class);

    @Property(label = "Connection Timeout", description = "Determines the timeout until a connection is established")
    public static final String CONNECTION_TIMEOUT = "connectionTimeout";

    @Property(label = "Socket Timeout", description = "Defines the default socket timeout in milliseconds which is "
            + "the timeout for waiting for data")
    public static final String SO_TIMEOUT = "soTimeout";

    @Property(label = "Max total Connections", description = "Maximum total connections")
    public static final String MAX_TOTAL_CONNECTIONS = "maxTotalConn";

    @Property(label = "Default Max Connections Per Host",
            description = "The default maximum number of connections allowed per host")
    public static final String DEFAULT_MAX_CONNECTIONS_PER_HOST = "defaultMaxConnPerHost";

    public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    public static final int DEFAULT_SO_TIMEOUT = 15000;
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 1000;
    public static final int DEF_DEFAULT_MAX_CONNECTIONS_PER_HOST = 1000;

    private  MultiThreadedHttpConnectionManager conMgr;
    private int connectionTimeout;
    private int soTimeout;
    private int maxTotalConn;
    private int defaultMaxConnPerHost;

    @Activate
    protected void activate(final ComponentContext componentContext) {
        final Dictionary configurations = componentContext.getProperties();
        connectionTimeout = PropertiesUtil.toInteger(configurations.get(CONNECTION_TIMEOUT),
                DEFAULT_CONNECTION_TIMEOUT);
        soTimeout = PropertiesUtil.toInteger(configurations.get(SO_TIMEOUT), DEFAULT_SO_TIMEOUT);
        maxTotalConn = PropertiesUtil.toInteger(configurations.get(MAX_TOTAL_CONNECTIONS),
                DEFAULT_MAX_TOTAL_CONNECTIONS);
        defaultMaxConnPerHost = PropertiesUtil.toInteger(configurations.get(DEFAULT_MAX_CONNECTIONS_PER_HOST),
                DEF_DEFAULT_MAX_CONNECTIONS_PER_HOST);
    }

    @Override
    public MultiThreadedHttpConnectionManager getMultiThreadedConf() {
        if (conMgr == null) {
            conMgr = new MultiThreadedHttpConnectionManager();
            conMgr.getParams().setConnectionTimeout(connectionTimeout);
            conMgr.getParams().setSoTimeout(soTimeout);
            conMgr.getParams().setMaxTotalConnections(maxTotalConn);
            conMgr.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
        }
        return conMgr;
    }
}
