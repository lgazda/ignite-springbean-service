package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteAtomicReference;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteAtomicStamped;
import org.apache.ignite.IgniteBinary;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteCountDownLatch;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.IgniteEvents;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.IgniteLock;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.IgniteScheduler;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.MemoryMetrics;
import org.apache.ignite.PersistenceMetrics;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import org.apache.ignite.lang.IgniteProductVersion;
import org.apache.ignite.plugin.IgnitePlugin;
import org.apache.ignite.plugin.PluginNotFoundException;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.services.ServiceContext;
import org.jetbrains.annotations.Nullable;
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
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.cache.CacheException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IgniteSpringBeanServiceConfigurationTest.SpringBeansConfiguration.class)
public class IgniteSpringBeanServiceConfigurationTest {
    @Autowired
    private AbstractApplicationContext context;

    @Test
    public void should_startContextAndInjectIgniteProxy() {
        context.getBean(SpringService.class);
    }

    @Configuration
    @ComponentScan("pl.net.gazda")
    public static class SpringBeansConfiguration implements ApplicationContextAware{
        private ApplicationContext applicationContext;

        @Primary
        @Bean(name="igniteProxy")
        public ProxyFactoryBean igniteProxy() {
            LazyInitTargetSource targetSource = new LazyInitTargetSource();
            targetSource.setTargetBeanName("igniteSpringBean");
            targetSource.setBeanFactory(applicationContext);
            targetSource.setTargetClass(Ignite.class);

            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setInterfaces(Ignite.class);
            proxyFactoryBean.setTargetSource(targetSource);

            return proxyFactoryBean;
        }

        @Bean(name="igniteSpringBean")
        public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
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
        private final Ignite ignite;

        //@Lazy
        @Autowired
        public SpringService(Ignite ignite) {
            this.ignite = ignite;
        }
    }

    @Component
    public static class SimpleIgniteService implements Service {
        public static final String NAME = "IgniteService";

        @IgniteInstanceResource
        private transient Ignite ignite;

        //@SpringResource(resourceName = "springService")
        @Autowired
        private transient SpringService springService;

        @Override
        public void cancel(ServiceContext ctx) {}

        @Override
        public void init(ServiceContext ctx) throws Exception {}

        @Override
        public void execute(ServiceContext ctx) throws Exception {}

    }
}
