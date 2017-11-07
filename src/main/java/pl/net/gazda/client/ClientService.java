package pl.net.gazda.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.net.gazda.common.IgniteBusinessService;

@Service
public class ClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);
    private final Ignite ignite;

    @Autowired
    public ClientService(Ignite ignite) {
        this.ignite = ignite;
    }

    @Scheduled(fixedRate = 1000, initialDelay = 1000)
    public void simulateIgniteServiceExecution() {
        LOG.info("Executing service and getting result: " + igniteService().someOperation());
    }

    private IgniteBusinessService igniteService() {
        return ignite.services(getServerCluster()) //getting service from all server nodes
                .serviceProxy(IgniteBusinessService.NAME, IgniteBusinessService.class, false);
    }

    private ClusterGroup getServerCluster() {
        return ignite.cluster().forServers();
    }
}
