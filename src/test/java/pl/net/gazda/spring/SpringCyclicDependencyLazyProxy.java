package pl.net.gazda.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringCyclicDependencyLazyProxy.SpringBeansConfiguration.class)
public class SpringCyclicDependencyLazyProxy {
    @Autowired
    private AbstractApplicationContext context;

    @Test
    public void should_startContext() {
        context.getBean("serviceBProxy");
    }

    @Configuration
    @ComponentScan("pl.net.gazda.spring")
    public static class SpringBeansConfiguration implements ApplicationContextAware{
        private ApplicationContext applicationContext;

        @Primary
        @Bean(name="serviceBProxy")
        public ProxyFactoryBean serviceBProxy() {
            LazyInitTargetSource targetSource = new LazyInitTargetSource();
            targetSource.setTargetBeanName("serviceB");
            targetSource.setTargetClass(ServiceB.class);

            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTargetSource(targetSource);

            return proxyFactoryBean;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    public interface ServiceA {

    }

    @Component("serviceA")
    public static class ServiceAImpl implements ServiceA {
        private final ServiceB service;

        @Autowired
        public ServiceAImpl(ServiceB service) {
            this.service = service;
        }
    }

    public interface ServiceB {

    }

    @Component("serviceB")
    public static class ServiceBImpl implements ServiceB {
/*        private final ServiceA service;

       // @Autowired
        public ServiceBImpl(ServiceA service) {
            this.service = service;
        }*/
    }
}
