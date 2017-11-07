package pl.net.gazda.common;

import org.apache.ignite.services.Service;

public interface IgniteBusinessService extends Service, BusinessService {
    String NAME = "IgniteService";
}
