package pl.net.gazda;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.net.gazda.client.IgniteClientConfiguration;
import pl.net.gazda.server.IgniteServerConfiguration;

public class SpringRunner {
    /**
     * Starts 3 spring context. Two for ignite server nodes and one for Ignite client node.
     * Client node executes an Ignite service deployed on both server nodes.
     */
    public static void main(String[] args) {
        startIgniteServerContext();
        startIgniteServerContext();
        startIgniteClientContext();
    }

    private static void startIgniteClientContext() {
        startContextInThread(IgniteClientConfiguration.class);
    }

    private static void startIgniteServerContext() {
        startContextInThread(IgniteServerConfiguration.class);
    }

    private static void startContextInThread(Class<?> configurationClass) {
        new Thread(() -> startSpringContext(configurationClass)).start();
    }

    private static ApplicationContext startSpringContext(Class<?> configurationClass) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(configurationClass);
        ctx.refresh();
        return ctx;
    }
}
