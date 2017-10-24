package pl.net.gazda.common.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.ServiceContext;
import org.apache.log4j.Logger;
import pl.net.gazda.server.components.SpringService;

public class SimpleIgniteService implements IgniteService {
    private static final Logger LOG = Logger.getLogger(SimpleIgniteService.class);
    public static final String NAME = "IgniteService";

    @IgniteInstanceResource
    private transient Ignite ignite;

    @SpringResource(resourceName = "springService")
    private transient SpringService springService;

    @Override
    public void cancel(ServiceContext ctx) {}

    @Override
    public void init(ServiceContext ctx) throws Exception {
        LOG.info("SERVICE INITIALIZED");
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        LOG.info("SERVICE DEPLOYED");
    }

    @Override
    public void performSomeAction() {
        LOG.info("Some action from Simple Ignite Service");
    }
}
