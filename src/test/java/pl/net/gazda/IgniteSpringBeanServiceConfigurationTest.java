package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.services.ServiceContext;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IgniteSpringBeanServiceConfigurationTest.SpringBeansConfiguration.class)
public class IgniteSpringBeanServiceConfigurationTest {
    @Autowired
    private AbstractApplicationContext context;

    @Test
    public void should_startContextAndInjectIgniteProxy() {
        context.getBean(Ignite.class)
                .services()
                .serviceProxy(SimpleIgniteService.NAME, IgniteService.class, false)
                .performSomeOperation();
    }

    @Configuration
    @ComponentScan("pl.net.gazda")
    public static class SpringBeansConfiguration {

        @Bean(destroyMethod = "close")
        public Ignite ignite(IgniteConfiguration configuration) {
            return Ignition.start(configuration);
        }

        @Bean
        public IgniteConfiguration igniteConfiguration(ServiceConfiguration serviceConfiguration) {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setServiceConfiguration(serviceConfiguration);
            return configuration;
        }

        @Bean
        public ServiceConfiguration igniteServiceConfiguration(SimpleIgniteService igniteService) {
            ServiceConfiguration configuration = new ServiceConfiguration();
            configuration.setService(igniteService);
            configuration.setName(SimpleIgniteService.NAME);
            configuration.setMaxPerNodeCount(1);
            return configuration;
        }
    }

    @Component("springService")
    public static class SpringService {
        private final Logger LOG = Logger.getLogger(SpringService.class);
        private final SpringSubService subService;

        @Autowired
        public SpringService(SpringSubService subService) {
            this.subService = subService;
        }

        public void performSomeOperation() {
            LOG.info("performSomeOperation");
            subService.performSomeOperation();
        }
    }

    @Component
    public static class SpringSubService {
        private final Logger LOG = Logger.getLogger(SimpleIgniteService.class);
        private final Ignite ignite;

        @Autowired
        public SpringSubService(Ignite ignite) {
            this.ignite = ignite;
        }

        public void performSomeOperation() {
            LOG.info("performSomeOperation");
            LOG.info(ignite.cluster());
        }
    }

    public interface IgniteService {
         void performSomeOperation();
    }

    @Component
    public static class SimpleIgniteService implements Service, IgniteService {
        Logger LOG = Logger.getLogger(SimpleIgniteService.class);

        public static final String NAME = "IgniteService";

        @IgniteInstanceResource
        private transient Ignite ignite;

        @SpringResource(resourceName = "springService")
        private transient SpringService springService;

        @Override
        public void cancel(ServiceContext ctx) {}

        @Override
        public void init(ServiceContext ctx) throws Exception {
        }

        @Override
        public void execute(ServiceContext ctx) throws Exception {}

        @Override
        public void performSomeOperation() {
            LOG.info("performSomeOperation");
            springService.performSomeOperation();
        }
    }
}
