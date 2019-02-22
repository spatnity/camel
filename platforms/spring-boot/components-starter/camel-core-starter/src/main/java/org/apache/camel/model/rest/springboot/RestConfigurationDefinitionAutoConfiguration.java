/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.model.rest.springboot;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.camel.CamelContext;
import org.apache.camel.model.rest.RestConstants;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spring.boot.util.CamelPropertiesHelper;
import org.apache.camel.support.IntrospectionSupport;
import org.apache.camel.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo")
@Configuration
@ConditionalOnBean(type = "org.apache.camel.spring.boot.CamelAutoConfiguration")
@ConditionalOnProperty(name = "camel.rest.enabled", matchIfMissing = true)
@AutoConfigureAfter(name = "org.apache.camel.spring.boot.CamelAutoConfiguration")
@EnableConfigurationProperties(RestConfigurationDefinitionProperties.class)
public class RestConfigurationDefinitionAutoConfiguration {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private RestConfigurationDefinitionProperties config;

    @Lazy
    @Bean(name = RestConstants.DEFAULT_REST_CONFIGURATION_ID)
    @ConditionalOnClass(CamelContext.class)
    @ConditionalOnMissingBean
    public RestConfiguration configureRestConfigurationDefinition()
            throws Exception {
        Map<String, Object> properties = new HashMap<>();
        IntrospectionSupport.getProperties(config, properties, null, false);
        // These options is configured specially further below, so remove them first
        properties.remove("enableCors");
        properties.remove("apiProperty");
        properties.remove("componentProperty");
        properties.remove("consumerProperty");
        properties.remove("dataFormatProperty");
        properties.remove("endpointProperty");
        properties.remove("corsHeaders");
        
        RestConfiguration definition = new RestConfiguration();
        CamelPropertiesHelper.setCamelProperties(camelContext, definition, properties, true);
        
        // Workaround for spring-boot properties name as It would appear
        // as enable-c-o-r-s if left uppercase in Configuration
        definition.setEnableCORS(config.getEnableCors());
        
        if (config.getApiProperty() != null) {
            definition.setApiProperties(new HashMap<>(CollectionHelper.flattenKeysInMap(config.getApiProperty(), ".")));
        }
        if (config.getComponentProperty() != null) {
            definition.setComponentProperties(new HashMap<>(CollectionHelper.flattenKeysInMap(config.getComponentProperty(), ".")));
        }
        if (config.getConsumerProperty() != null) {
            definition.setConsumerProperties(new HashMap<>(CollectionHelper.flattenKeysInMap(config.getConsumerProperty(), ".")));
        }
        if (config.getDataFormatProperty() != null) {
            definition.setDataFormatProperties(new HashMap<>(CollectionHelper.flattenKeysInMap(config.getDataFormatProperty(), ".")));
        }
        if (config.getEndpointProperty() != null) {
            definition.setEndpointProperties(new HashMap<>(CollectionHelper.flattenKeysInMap(config.getEndpointProperty(), ".")));
        }
        if (config.getCorsHeaders() != null) {
            Map<String, Object> map = CollectionHelper.flattenKeysInMap(config.getCorsHeaders(), ".");
            Map<String, String> target = new HashMap<>();
            map.forEach((k, v) -> target.put(k, v.toString()));
            definition.setCorsHeaders(target);
        }
        return definition;
    }
}