package pl.net.gazda.common;

import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
public class IgniteCommonConfiguration {
    @Bean
    public TcpDiscoverySpi localhostTcpDiscoverySpi() {
        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
/*        ipFinder.setAddresses(singletonList("127.0.0.1:47500..47510"));
        discoSpi.setIpFinder(ipFinder);*/
        return discoSpi;
    }

    @Bean
    public TcpCommunicationSpi communicationSpi() {
        TcpCommunicationSpi tcpCommunicationSpi = new TcpCommunicationSpi();
/*        tcpCommunicationSpi.setLocalPort(47500);
        tcpCommunicationSpi.setLocalPortRange(10);*/
        return tcpCommunicationSpi;
    }
}
