package pl.net.gazda.server;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
    "pl.net.gazda.common",
    "pl.net.gazda.server.components"
})
public class ServerSpringConfiguration {
    @Bean(name = "igniteServerSpringBean")
    public IgniteSpringBean igniteSpringBean(IgniteConfiguration configuration) {
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(configuration);
        return igniteSpringBean;
    }

    @Bean(name = "igniteServerConfiguration")
    public IgniteConfiguration igniteConfiguration(TcpDiscoverySpi discoverySpi) {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setIgniteInstanceName("Server Node");
        configuration.setDiscoverySpi(discoverySpi);

        return configuration;
    }
}
