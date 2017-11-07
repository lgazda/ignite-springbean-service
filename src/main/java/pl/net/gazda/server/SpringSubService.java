package pl.net.gazda.server;

import org.apache.ignite.Ignite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SpringSubService {
    private static final Logger LOG = LoggerFactory.getLogger(SpringSubService.class);
    private final Ignite ignite;

    @Autowired
    public SpringSubService(Ignite ignite) {
        this.ignite = ignite;
    }

    Object igniteBasedLogic() {
        return "Result from " + ignite.cluster().forLocal().node().id();
    }

    @PostConstruct
    public void postConstruct() {
        LOG.warn("You can't call/reference to Ignite bean from this place. Spring context must be refreshed and ready.");
        //ignite.cluster().localNode();
    }
}
