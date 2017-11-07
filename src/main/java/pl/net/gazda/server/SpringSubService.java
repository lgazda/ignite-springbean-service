package pl.net.gazda.server;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SpringSubService {
    private final Ignite ignite;

    @Autowired
    public SpringSubService(Ignite ignite) {
        this.ignite = ignite;
    }

    public Object igniteBasedLogic() {
        return ignite.cluster().forLocal().node();
    }
}
