/*
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
package org.apache.camel.processor.interceptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.StreamCache;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoStreamCachingTest extends ContextTestSupport {

    private static final String MESSAGE = "<hello>world!</hello>";
    private MockEndpoint a;
    private MockEndpoint b;

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        a = getMockEndpoint("mock:a");
        b = getMockEndpoint("mock:b");
    }

    @Test
    public void testNoStreamCache() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:a").noStreamCaching().to("mock:a");
            }
        });
        context.start();

        a.expectedMessageCount(1);

        InputStream message = new ByteArrayInputStream(MESSAGE.getBytes());
        template.sendBody("direct:a", message);

        assertMockEndpointsSatisfied();
        boolean b1 = a.assertExchangeReceived(0).getIn().getBody() instanceof ByteArrayInputStream;
        assertTrue(b1);
        assertEquals(MESSAGE, a.assertExchangeReceived(0).getIn().getBody(String.class));
    }

    @Test
    public void testStreamCacheIsDefault() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:a").to("mock:a");
            }
        });
        context.start();

        a.expectedMessageCount(1);

        InputStream message = new ByteArrayInputStream(MESSAGE.getBytes());
        template.sendBody("direct:a", message);

        assertMockEndpointsSatisfied();

        boolean a1 = a.assertExchangeReceived(0).getIn().getBody() instanceof StreamCache;
        assertTrue(a1);
        assertEquals(MESSAGE, a.assertExchangeReceived(0).getIn().getBody(String.class));
    }

    @Test
    public void testMixed() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:a").noStreamCaching().to("mock:a");

                from("direct:b").streamCaching().to("mock:b");
            }
        });
        context.start();

        a.expectedMessageCount(1);
        b.expectedMessageCount(1);

        InputStream message = new ByteArrayInputStream(MESSAGE.getBytes());
        template.sendBody("direct:a", message);

        InputStream message2 = new ByteArrayInputStream(MESSAGE.getBytes());
        template.sendBody("direct:b", message2);

        assertMockEndpointsSatisfied();

        boolean b2 = a.assertExchangeReceived(0).getIn().getBody() instanceof ByteArrayInputStream;
        assertTrue(b2);
        assertEquals(MESSAGE, a.assertExchangeReceived(0).getIn().getBody(String.class));

        boolean b1 = b.assertExchangeReceived(0).getIn().getBody() instanceof StreamCache;
        assertTrue(b1);
        assertEquals(MESSAGE, b.assertExchangeReceived(0).getIn().getBody(String.class));
    }

}
