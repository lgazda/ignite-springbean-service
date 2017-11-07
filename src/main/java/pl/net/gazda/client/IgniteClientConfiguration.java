package pl.net.gazda.client;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.net.gazda.common.IgniteCommonConfiguration;

@Configuration
@Import(IgniteCommonConfiguration.class)
@ComponentScan("pl.net.gazda.client")
@EnableScheduling
public class IgniteClientConfiguration {
    @Bean(name="igniteClientSpringBean")
    public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(configuration);
        return igniteSpringBean;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration(DiscoverySpi discoverySpi, TcpCommunicationSpi tcpCommunicationSpi) {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setIgniteInstanceName("Client node");
        configuration.setClientMode(true);
        configuration.setDiscoverySpi(discoverySpi);
        configuration.setCommunicationSpi(tcpCommunicationSpi);
        return configuration;
    }
}
