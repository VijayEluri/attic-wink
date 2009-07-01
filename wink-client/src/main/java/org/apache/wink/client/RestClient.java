/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/
 

package org.apache.wink.client;

import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wink.client.internal.ResourceImpl;
import org.apache.wink.common.SymphonyApplication;
import org.apache.wink.common.internal.application.ApplicationValidator;
import org.apache.wink.common.internal.factory.OFFactoryRegistry;
import org.apache.wink.common.internal.factory.ScopeOFFactory;
import org.apache.wink.common.internal.registry.ProvidersRegistry;
import org.apache.wink.common.internal.registry.metadata.ProviderMetadataCollector;


/**
 * The RestClient is the entry point for all rest service operations. The RestClient is used to
 * create instances of {@link Resource} classes that are used to make the actual invocations to the
 * service. The client can be initialized with a user supplied configuration to specify custom
 * Provider classes, in addition to other configuration options.
 * 
 * <pre>
 *      // create the client
 *      RestClient client = new RestClient();
 *      
 *      // create the resource to make invocations on
 *      Resource resource = client.resource(&quot;http://myhost:80/my/service&quot;);
 *      
 *      // invoke GET on the resource and receive the response entity as a string
 *      String entity = resource.get(String.class);
 *      ...
 * </pre>
 */
public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private ProvidersRegistry providersRegistry;
    private ClientConfig config;

    /**
     * Construct a new RestClient using the default client configuration
     */
    public RestClient() {
        this(new ClientConfig());
    }

    /**
     * Construct a new RestClient using the supplied configuration
     * 
     * @param config
     *            the client configuration
     */
    public RestClient(ClientConfig config) {
        ClientConfig clone = config.clone();
        this.config = clone.build();
        initProvidersRegistry();
    }

    /**
     * Get the unmodifiable client configuration
     * 
     * @return the unmodifiable client configuration
     */
    public ClientConfig getConfig() {
        return config;
    }

    /**
     * Create a new {@link Resource} instance
     * 
     * @param uri
     *            uri of the resource to create
     * @return a new {@link Resource} instance attached to the specified uri
     */
    public Resource resource(URI uri) {
        return new ResourceImpl(uri, config, providersRegistry);
    }

    /**
     * Create a new {@link Resource} instance
     * 
     * @param uri
     *            uri of the resource to create
     * @return a new {@link Resource} instance attached to the specified uri
     */
    public Resource resource(String uri) {
        return resource(URI.create(uri));
    }

    private void initProvidersRegistry() {
        // setup OFFactoryRegistry to support default and scope
        OFFactoryRegistry ofFactoryRegistry = new OFFactoryRegistry();
        ofFactoryRegistry.addFactoryFactory(new ScopeOFFactory<Object>());
        providersRegistry = new ProvidersRegistry(ofFactoryRegistry, new ApplicationValidator());

        // process all applications
        for (Application app : config.getApplications()) {
            processApplication(app);
        }
    }

    private void processApplication(Application application) {
        if (application == null) {
            return;
        }

        // process singletons
        Set<Object> singletons = application.getSingletons();
        if (singletons != null && singletons.size() > 0) {
            processSingletons(singletons);
        }

        // process classes
        Set<Class<?>> classes = application.getClasses();
        if (classes != null && classes.size() > 0) {
            processClasses(classes);
        }

        if (application instanceof SymphonyApplication) {
            processSymphonyApplication((SymphonyApplication)application);
        }
    }

    private void processClasses(Set<Class<?>> classes) {
        for (Class<?> cls : classes) {
            if (ProviderMetadataCollector.isProvider(cls)) {
                providersRegistry.addProvider(cls);
            } else {
                logger.warn(String.format("%s is not a provider. Ignoring.", String.valueOf(cls)));
            }
        }
    }

    private void processSingletons(Set<Object> singletons) {
        for (Object obj : singletons) {
            Class<?> cls = obj.getClass();
            if (ProviderMetadataCollector.isProvider(cls)) {
                providersRegistry.addProvider(obj);
            } else {
                logger.warn(String.format("%s is not a provider. Ignoring.", String.valueOf(obj)));
            }
        }
    }

    private void processSymphonyApplication(SymphonyApplication sApplication) {
        Set<Object> instances = sApplication.getInstances();
        double priority = sApplication.getPriority();
        if (instances == null) {
            return;
        }

        for (Object obj : instances) {
            Class<?> cls = obj.getClass();
            if (ProviderMetadataCollector.isProvider(cls)) {
                providersRegistry.addProvider(obj, priority);
            } else {
                logger.warn(String.format("%s is not a provider. Ignoring.", String.valueOf(obj)));
            }
        }
    }

}