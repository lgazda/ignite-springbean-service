package pl.net.gazda.client.components;

import javax.annotation.PostConstruct;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.net.gazda.common.service.IgniteService;
import pl.net.gazda.common.service.SimpleIgniteService;

@Component
public class ClientService {
    private final Ignite ignite;

    @Autowired
    ClientService(Ignite ignite) {
        this.ignite = ignite;
    }

    @PostConstruct
    private void init() throws InterruptedException {
        IgniteService service = ignite.services()
            .serviceProxy(SimpleIgniteService.NAME, IgniteService.class, false);
        service.performSomeAction();
    }
}
