package pl.net.gazda.common.service;

import org.apache.ignite.services.Service;

public interface IgniteService extends Service {
    void performSomeAction();
}
