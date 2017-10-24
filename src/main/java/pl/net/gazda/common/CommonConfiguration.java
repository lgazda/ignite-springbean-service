package pl.net.gazda.common;

import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
public class CommonConfiguration {
    @Bean
    TcpDiscoverySpi tcpDiscoverySpi() {
        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(singletonList("127.0.0.1"));
        discoSpi.setIpFinder(ipFinder);
        return discoSpi;
    }
}
