//package com.exist.HelpdeskApp.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.context.ApplicationContext;
//
//@Component
//public class BeanLister implements CommandLineRunner {
//
//    private final ApplicationContext context;
//
//    public BeanLister(ApplicationContext context) {
//        this.context = context;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        String[] beanNames = context.getBeanDefinitionNames();
//        for (String name : beanNames) {
//            System.out.println(name);
//        }
//    }
//}
