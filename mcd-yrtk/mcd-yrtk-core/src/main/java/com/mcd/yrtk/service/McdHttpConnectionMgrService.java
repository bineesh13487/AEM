package com.mcd.yrtk.service;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public interface McdHttpConnectionMgrService {
    MultiThreadedHttpConnectionManager getMultiThreadedConf();
}
