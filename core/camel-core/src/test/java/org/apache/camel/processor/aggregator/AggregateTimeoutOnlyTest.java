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
package org.apache.camel.processor.aggregator;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.junit.jupiter.api.Test;

/**
 * Unit test to verify that aggregate by timeout only also works.
 */
public class AggregateTimeoutOnlyTest extends ContextTestSupport {

    @Test
    public void testAggregateTimeoutOnly() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        // by default the use latest aggregation strategy is used so we get
        // message 9
        result.expectedBodiesReceived("Message 9");
        // should take 0.1 seconds to complete this one
        result.setResultMinimumWaitTime(90);

        for (int i = 0; i < 10; i++) {
            template.sendBodyAndHeader("direct:start", "Message " + i, "id", "1");
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // START SNIPPET: e1
                from("direct:start")
                        // aggregate timeout after 0.1 second
                        .aggregate(header("id"), new UseLatestAggregationStrategy()).completionTimeout(100)
                        .completionTimeoutCheckerInterval(10).to("mock:result");
                // END SNIPPET: e1
            }
        };
    }
}
