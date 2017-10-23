package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.net.gazda.beans.SpringComponents;

import javax.annotation.PostConstruct;

import static java.util.Collections.singletonList;

public class IgniteSpringBeanServiceConfiguration {
    /**
     * Starts 2 spring context. One for Ignite client and one for Ignite server node.
     * Client context tries to access Ignite Service when this service is not fully deployed.
     * This code should demonstrate that IgniteServiceDeploymentApplicationContextListener is not enough workaround and we can still have race for accessing Ignite Service.
     */
    public static void main(String[] args) {
        new Thread(() -> startSpringContext(IgniteClientSpringBeansConfiguration.class)).start();
        new Thread(() -> startSpringContext(IgniteServerSpringBeansConfiguration.class)).start();
    }

    private static ApplicationContext startSpringContext(Class configurationClass) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(configurationClass);
        ctx.refresh();
        return ctx;
    }

    @Configuration
    public static class IgniteClientSpringBeansConfiguration {
        @Bean(name="igniteClientSpringBean")
        public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
            IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
            igniteSpringBean.setConfiguration(configuration);
            return igniteSpringBean;
        }

        @Bean(name = "igniteClientConfiguration")
        public IgniteConfiguration igniteConfiguration() {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setIgniteInstanceName("Client node");
            configuration.setClientMode(true);
            configuration.setDiscoverySpi(getLocalhostTcpDiscoverySpi());
            return configuration;
        }

        @Bean
        public ClientService clientService(Ignite ignite) {
            return new ClientService(ignite);
        }

        static class ClientService {
            private final Ignite ignite;

            ClientService(Ignite ignite) {
                this.ignite = ignite;
            }

            @PostConstruct
            private void init() throws InterruptedException {
                //sleep(4000);
                ignite.services().serviceProxy(SpringComponents.SimpleIgniteService.NAME, SpringComponents.IgniteService.class, false).performSomeAction();
            }
        }
    }

    @Configuration
    @ComponentScan("pl.net.gazda.beans")
    public static class IgniteServerSpringBeansConfiguration {

        @Bean(name="igniteServerSpringBean")
        public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
            IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
            igniteSpringBean.setConfiguration(configuration);
            return igniteSpringBean;
        }

        @Bean(name = "igniteServerConfiguration")
        public IgniteConfiguration igniteConfiguration(ServiceConfiguration[] serviceConfigurations) {
            IgniteConfiguration configuration = new IgniteConfiguration();
            configuration.setClientMode(false);
            configuration.setIgniteInstanceName("Server Node");
            configuration.setDiscoverySpi(getLocalhostTcpDiscoverySpi());
            //https://issues.apache.org/jira/browse/IGNITE-6555
            //configuration.setServiceConfiguration(serviceConfigurations);
            return configuration;
        }

        @Bean
        public ServiceConfiguration igniteServiceConfiguration(SpringComponents.SimpleIgniteService igniteService) {
            ServiceConfiguration configuration = new ServiceConfiguration();
            configuration.setService(igniteService);
            configuration.setName(SpringComponents.SimpleIgniteService.NAME);
            configuration.setMaxPerNodeCount(1);
            configuration.setNodeFilter(node -> !node.isClient());
            return configuration;
        }

        @Bean
        public ServiceConfiguration igniteServiceConfiguration2(SpringComponents.SimpleIgniteService igniteService) {
            ServiceConfiguration configuration = new ServiceConfiguration();
            configuration.setService(igniteService);
            configuration.setName(SpringComponents.SimpleIgniteService.NAME + "_2");
            configuration.setMaxPerNodeCount(1);
            configuration.setNodeFilter(node -> !node.isClient());
            return configuration;
        }
    }

    @NotNull
    private static TcpDiscoverySpi getLocalhostTcpDiscoverySpi() {
        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(singletonList("127.0.0.1"));
        discoSpi.setIpFinder(ipFinder);
        return discoSpi;
    }
}
