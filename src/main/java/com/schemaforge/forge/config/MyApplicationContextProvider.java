package com.schemaforge.forge.config;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationContextProvider implements ApplicationContextAware {


    private ApplicationContext context;

    public MyApplicationContextProvider(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    public ApplicationContext getApplicationContext() {
        return context;
    }

}
