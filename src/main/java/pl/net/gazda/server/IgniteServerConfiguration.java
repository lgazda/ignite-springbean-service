package pl.net.gazda.server;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.log4j2.Log4J2Logger;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import pl.net.gazda.common.IgniteBusinessService;
import pl.net.gazda.common.IgniteCommonConfiguration;

import java.io.IOException;
import java.util.UUID;

@Configuration
@Import(IgniteCommonConfiguration.class)
@ComponentScan("pl.net.gazda.server")
public class IgniteServerConfiguration {
    @Bean(name="igniteServerSpringBean")
    public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(configuration);
        return igniteSpringBean;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration(ServiceConfiguration[] serviceConfigurations, DiscoverySpi discoverySpi, TcpCommunicationSpi tcpCommunicationSpi) throws IgniteCheckedException, IOException {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setClientMode(false);
        configuration.setIgniteInstanceName("Server Node " + UUID.randomUUID());
        configuration.setDiscoverySpi(discoverySpi);
        configuration.setServiceConfiguration(serviceConfigurations);
        configuration.setCommunicationSpi(tcpCommunicationSpi);
        configuration.setGridLogger(getIgniteLogger());
        return configuration;
    }

    @NotNull
    private IgniteLogger getIgniteLogger() throws IgniteCheckedException, IOException {
        return new Log4J2Logger(new ClassPathResource("log4j2.xml").getFile());
    }

    @Bean
    public ServiceConfiguration igniteServiceConfiguration(SimpleIgniteService igniteService) {
        ServiceConfiguration configuration = new ServiceConfiguration();
        configuration.setService(igniteService);
        configuration.setName(IgniteBusinessService.NAME);
        configuration.setMaxPerNodeCount(1);
        //filter is not needed - we are deploying the service only within ignite server bean configuration
        //configuration.setNodeFilter(node -> !node.isClient());
        return configuration;
    }
}
