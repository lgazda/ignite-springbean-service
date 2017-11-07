package pl.net.gazda.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.net.gazda.common.BusinessService;

@Service("springService")
public class SpringService implements BusinessService {
    private static final Logger LOG = LoggerFactory.getLogger(SpringService.class);
    private final SpringSubService subService;

    @Autowired
    public SpringService(SpringSubService subService) {
        this.subService = subService;
    }

    @Override
    public Object someOperation() {
        LOG.info("Doing some stuff and executing other Spring components.");
        return subService.igniteBasedLogic();
    }
}
