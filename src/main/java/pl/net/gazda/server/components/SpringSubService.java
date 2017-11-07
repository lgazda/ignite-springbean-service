package pl.net.gazda.server.components;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringSubService {
    private final Ignite ignite;

    @Autowired
    public SpringSubService(Ignite ignite) {
        this.ignite = ignite;
    }
}
