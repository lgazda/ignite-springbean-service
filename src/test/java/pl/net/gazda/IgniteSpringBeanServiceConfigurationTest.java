package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.IgniteSpringBean;
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
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IgniteSpringBeanServiceConfigurationTest.SpringBeansConfiguration.class)
public class IgniteSpringBeanServiceConfigurationTest {
    @Test
    public void should_startContextAndInjectIgniteProxy() {

    }

    @Configuration
    @ComponentScan("pl.net.gazda")
    public static class SpringBeansConfiguration {

        @Bean(name="igniteSpringBean")
        public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
            IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
            igniteSpringBean.setConfiguration(configuration);
            return igniteSpringBean;
        }

        @Bean
        public IgniteConfiguration igniteConfiguration(ServiceConfiguration[] serviceConfigurations) {
            IgniteConfiguration configuration = new IgniteConfiguration();
            //https://issues.apache.org/jira/browse/IGNITE-6555
            //configuration.setServiceConfiguration(serviceConfigurations);
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

        @Bean
        public ServiceConfiguration igniteServiceConfiguration2(SimpleIgniteService igniteService) {
            ServiceConfiguration configuration = new ServiceConfiguration();
            configuration.setService(igniteService);
            configuration.setName(SimpleIgniteService.NAME + "_2");
            configuration.setMaxPerNodeCount(1);
            return configuration;
        }
    }

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
    public static class SimpleIgniteService implements Service {
        private static final Logger LOG = Logger.getLogger(SimpleIgniteService.class);
        static final String NAME = "IgniteService";

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

    }
}
