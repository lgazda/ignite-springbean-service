package pl.net.gazda.server;

import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.net.gazda.common.IgniteBusinessService;

/**
 * Ignite service injects spring bean to run business logic.
 */
@Component
public class SimpleIgniteService implements IgniteBusinessService {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleIgniteService.class);

    @IgniteInstanceResource
    private transient Ignite ignite;

    @SpringResource(resourceName = "springService")
    private transient SpringService springService;

    @Override
    public void cancel(ServiceContext ctx) {
        LOG.info("Service is being canceled / un-deployed.");
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        LOG.info("Node: {} = service is being initialized.", localNode().id());
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        LOG.info("Node: {} - service deployed.", localNode().id());
    }

    @Override
    public Object someOperation() {
        LOG.info("Node: {} - delegating operation to spring service.", localNode().id());
        return springService.someOperation();
    }

    private ClusterNode localNode() {
        return ignite.cluster().forLocal().node();
    }
}
