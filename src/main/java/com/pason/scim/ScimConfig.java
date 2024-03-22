/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.pason.scim;

import java.util.List;
import java.util.Set;

import org.apache.directory.scim.core.repository.DefaultPatchHandler;
import org.apache.directory.scim.core.repository.PatchHandler;
import org.apache.directory.scim.core.repository.Repository;
import org.apache.directory.scim.core.repository.RepositoryRegistry;
import org.apache.directory.scim.core.schema.SchemaRegistry;
import org.apache.directory.scim.protocol.UserResource;
import org.apache.directory.scim.server.configuration.ServerConfiguration;
import org.apache.directory.scim.server.rest.EtagGenerator;
import org.apache.directory.scim.server.rest.ScimResourceHelper;
import org.apache.directory.scim.server.rest.UserResourceImpl;
import org.apache.directory.scim.spec.resources.ScimResource;
import org.apache.directory.scim.spring.ScimpleSpringConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
// import jakarta.enterprise.inject.Instance;
// import jakarta.enterprise.util.TypeLiteral;
import jakarta.ws.rs.core.Application;

/**
 * Autoconfigures default beans needed for Apache SCIMple.
 */
@Configuration
@AutoConfigureBefore(ScimpleSpringConfiguration.class) // before the scim boot starter configuration
public class ScimConfig {

    @Bean
    @ConditionalOnMissingBean
    ServerConfiguration serverConfiguration() {
        return new ServerConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean
    EtagGenerator etagGenerator() {
        return new EtagGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    SchemaRegistry schemaRegistry() {
        return new SchemaRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    PatchHandler patchHandler(SchemaRegistry schemaRegistry) {
        return new DefaultPatchHandler(schemaRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    RepositoryRegistry repositoryRegistry(SchemaRegistry schemaRegistry,
            List<Repository<? extends ScimResource>> scimResources) {
        RepositoryRegistry registry = new RepositoryRegistry(schemaRegistry);
        registry.registerRepositories(scimResources);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    Application jaxrsApplication() {
        return new ScimpleJaxRsApplication();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceConfig conf(Application app) {
        ResourceConfig config = ResourceConfig.forApplication(app);

        // register swagger
        config.register(OpenApiResource.class);

        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(UserResourceImpl.class).to(UserResource.class); // Used by SelfResource
            }
        });
        return config;
    }

    /**
     * Basic JAX-RS application that includes the required SCIMple classes.
     */
    static class ScimpleJaxRsApplication extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            return ScimResourceHelper.scimpleFeatureAndResourceClasses();
        }
    }

}
