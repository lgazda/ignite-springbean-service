package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.services.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    }

    @Configuration
    @ComponentScan("pl.net.gazda")
    public static class SpringBeansConfiguration implements ApplicationContextAware {
        private ApplicationContext applicationContext;

        @Primary
        @Bean(name="igniteLazySpringBean")
        public ProxyFactoryBean igniteLazySpringBean() {
            LazyInitTargetSource lazyInitTargetSource = new LazyInitTargetSource();
            lazyInitTargetSource.setTargetClass(Ignite.class);
            lazyInitTargetSource.setTargetBeanName("igniteSpringBean");
            lazyInitTargetSource.setBeanFactory(applicationContext);

            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTargetSource(lazyInitTargetSource);
            proxyFactoryBean.setProxyTargetClass(false);
            return proxyFactoryBean;
        }

        @Bean(name="igniteSpringBean")
        public Ignite igniteSpringBean(IgniteConfiguration configuration) {
            IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
            igniteSpringBean.setConfiguration(configuration);
            return igniteSpringBean;
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

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
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
        public void execute(ServiceContext ctx) throws Exception {}

    }
}
