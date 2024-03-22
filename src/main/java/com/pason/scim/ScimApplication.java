package com.pason.scim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScimApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScimApplication.class, args);
    }

    // @Bean
    // public ResourceConfig conf(Application app) {
    //     ResourceConfig config = ResourceConfig.forApplication(app);

    //     config.register(new AbstractBinder() {
    //         @Override
    //         protected void configure() {
    //             bind(UserResourceImpl.class).to(UserResource.class); // Used by SelfResource
    //         }
    //     });
    //     return config;
    // }

}
