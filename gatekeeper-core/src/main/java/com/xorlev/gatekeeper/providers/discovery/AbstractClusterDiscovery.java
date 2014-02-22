package com.xorlev.gatekeeper.providers.discovery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.xorlev.gatekeeper.data.Cluster;
import com.xorlev.gatekeeper.providers.output.ClusterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 2013-07-28
 *
 * @author Michael Rose <elementation@gmail.com>
 */
public abstract class AbstractClusterDiscovery extends AbstractIdleService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected List<ClusterHandler> clusterHandlers = Lists.newArrayList();
    protected List<Cluster> previousClusterList = Collections.emptyList();

    public void registerHandler(ClusterHandler clusterHandler) {
        clusterHandlers.add(clusterHandler);
    }

    public abstract void startUp() throws Exception;

    public abstract void shutDown() throws Exception;

    public void updateInstances() {
        List<Cluster> clusterList = clusters();

        if (previousClusterList != clusterList) {
            for (ClusterHandler clusterHandler : clusterHandlers) {
                clusterHandler.processClusters(new ClustersUpdatedEvent(clusterList));
            }
        }
    }

    public abstract List<Cluster> clusters();

    public List<ClusterHandler> handlers() {
        return ImmutableList.copyOf(clusterHandlers);
    }

}
