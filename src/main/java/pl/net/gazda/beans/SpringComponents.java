package pl.net.gazda.beans;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.services.ServiceContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

public class SpringComponents {
    //workaround for https://issues.apache.org/jira/browse/IGNITE-6555
    @Component
    public static class IgniteServiceDeploymentApplicationContextListener {
        private final List<ServiceConfiguration> serviceConfigurations;

        @Autowired
        public IgniteServiceDeploymentApplicationContextListener(List<ServiceConfiguration> serviceConfigurations) {
            this.serviceConfigurations = serviceConfigurations;
        }

        @EventListener({ContextRefreshedEvent.class})
        void contextRefreshedEvent(ContextRefreshedEvent evt) {
            Ignite ignite = evt.getApplicationContext().getBean(Ignite.class);
            IgniteServices services = ignite.services();
            serviceConfigurations.forEach(services::deploy);
        }
    }

    @Component("springService")
    public static class SpringService {
        private final SpringSubService subService;

        @Autowired
        public SpringService(SpringSubService subService) {
            this.subService = subService;
        }
    }

    @Component
    public static class SpringSubService {
        private final Ignite ignite;

        @Autowired
        public SpringSubService(Ignite ignite) {
            this.ignite = ignite;
        }
    }

    @Component
    public static class SimpleIgniteService implements IgniteService {
        private static final Logger LOG = Logger.getLogger(SimpleIgniteService.class);
        public static final String NAME = "IgniteService";

        @IgniteInstanceResource
        private transient Ignite ignite;

        @SpringResource(resourceName = "springService")
        private transient SpringService springService;

        @Override
        public void cancel(ServiceContext ctx) {}

        @Override
        public void init(ServiceContext ctx) throws Exception {}

        @Override
        public void execute(ServiceContext ctx) throws Exception {
            LOG.info("SERVICE DEPLOYED");
        }

        @Override
        public void performSomeAction() {
            LOG.info("Some action from Simple Ignite Service");
        }
    }

    public interface IgniteService extends Service {
        void performSomeAction();
    }
}
