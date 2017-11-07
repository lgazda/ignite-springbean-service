package pl.net.gazda.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.net.gazda.common.BusinessService;

@Service("springService")
public class SpringService implements BusinessService {
    private final SpringSubService subService;

    @Autowired
    public SpringService(SpringSubService subService) {
        this.subService = subService;
    }

    @Override
    public Object someOperation() {
        //calls to other spring services
        return subService.igniteBasedLogic();
    }

}
