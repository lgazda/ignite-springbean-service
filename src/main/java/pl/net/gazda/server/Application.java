package pl.net.gazda.server;

import org.apache.ignite.Ignite;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(IgniteServerConfiguration.class);
        ctx.refresh();

        ctx.getBean(Ignite.class).getOrCreateCache("testCache");
    }
}
