package pl.net.gazda.common;

import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IgniteCommonConfiguration {
    @Bean
    public TcpDiscoverySpi localhostTcpDiscoverySpi() {
        return new TcpDiscoverySpi();
    }

    @Bean
    public TcpCommunicationSpi communicationSpi() {
        return new TcpCommunicationSpi();
    }
}
