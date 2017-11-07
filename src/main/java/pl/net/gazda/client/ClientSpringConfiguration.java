package pl.net.gazda.client;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.net.gazda.common.service.SimpleIgniteService;

@Configuration
@ComponentScan({
    "pl.net.gazda.common",
    "pl.net.gazda.client.components"
})
public class ClientSpringConfiguration {
    @Bean(name = "igniteClientSpringBean")
    public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(configuration);
        return igniteSpringBean;
    }

    @Bean(name = "igniteClientConfiguration")
    public IgniteConfiguration igniteConfiguration(
            ServiceConfiguration[] serviceConfigurations,
            TcpDiscoverySpi discoverySpi) {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setIgniteInstanceName("Client node");
        configuration.setClientMode(true);
        configuration.setDiscoverySpi(discoverySpi);
        configuration.setServiceConfiguration(serviceConfigurations);

        return configuration;
    }

    @Bean
    ServiceConfiguration igniteServiceConfiguration() {
        ServiceConfiguration configuration = new ServiceConfiguration();
        configuration.setService(new SimpleIgniteService());
        configuration.setName(SimpleIgniteService.NAME);
        configuration.setMaxPerNodeCount(1);
        configuration.setNodeFilter(node -> !node.isClient());
        return configuration;
    }

    @Bean
    ServiceConfiguration igniteServiceConfiguration2() {
        ServiceConfiguration configuration = new ServiceConfiguration();
        configuration.setService(new SimpleIgniteService());
        configuration.setName(SimpleIgniteService.NAME + "_2");
        configuration.setMaxPerNodeCount(1);
        configuration.setNodeFilter(node -> !node.isClient());
        return configuration;
    }
}
