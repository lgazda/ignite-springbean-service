package pl.net.gazda.server.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("springService")
public class SpringService {
    private final SpringSubService subService;

    @Autowired
    public SpringService(SpringSubService subService) {
        this.subService = subService;
    }
}
