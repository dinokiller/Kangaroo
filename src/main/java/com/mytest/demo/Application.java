package com.mytest.demo;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("some.properties")
public class Application {

    private final Log log = LogFactory.getLog(getClass());

    public static void main(String[] args) throws Throwable {
        new AnnotationConfigApplicationContext(Application.class);
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer pspc() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${configuration.projectName}")
    private String fieldValue;

    @Autowired
    Application(@Value("configuration.projectName") String pn) {
        log.info("Application Constructor: " + pn);
    }

    @Value("${configuration.projectName}")
    void setProjectName(String projectName) {
        log.info("setProjectName: " + projectName);
    }

    @Autowired
    void setEnvironment(Environment env) {
        log.info("setEnvironment: "+ env.getProperty("configuration.projectName"));
    }

    @Bean
    InitializingBean both(Environment env, @Value("${configuration.projectName}") String projectName) {
        return() -> {
            log.info("@Bean with both dependencies (projectName): " + projectName);
            log.info("@Bean with both dependencies (env): " + env.getProperty("configuration.projectName"));
        };
    }

    @PostConstruct
    void afterPropertySet() throws Throwable {
        log.info("fieldValue: " + this.fieldValue);
    }

}