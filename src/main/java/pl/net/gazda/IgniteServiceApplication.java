package pl.net.gazda;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.net.gazda.client.ClientSpringConfiguration;
import pl.net.gazda.server.ServerSpringConfiguration;

public class IgniteServiceApplication {
    /**
     * Starts 2 spring context. One for Ignite client and one for Ignite server node.
     * Client context tries to access Ignite Service when this service is not fully deployed.
     * This code should demonstrate that IgniteServiceDeploymentApplicationContextListener is not enough workaround and we can still have race for accessing Ignite Service.
     */
    public static void main(String[] args) {
        new Thread(() -> startSpringContext(ClientSpringConfiguration.class)).start();
        new Thread(() -> startSpringContext(ServerSpringConfiguration.class)).start();
    }

    private static ApplicationContext startSpringContext(Class configurationClass) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(configurationClass);
        ctx.refresh();
        return ctx;
    }
}
