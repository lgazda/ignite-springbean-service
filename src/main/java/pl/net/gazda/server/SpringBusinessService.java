package pl.net.gazda.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.net.gazda.common.BusinessService;

@Service("springBusinessService")
public class SpringBusinessService implements BusinessService {
    private final SpringSubService subService;

    @Autowired
    public SpringBusinessService(SpringSubService subService) {
        this.subService = subService;
    }

    @Override
    public Object someOperation() {
        //calls to other spring services
        return subService.igniteBasedLogic();
    }

}
