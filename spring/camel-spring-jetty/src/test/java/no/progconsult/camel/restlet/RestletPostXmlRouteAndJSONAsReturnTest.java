/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.progconsult.camel.restlet;

import no.progconsult.camel.jira.Fields;
import no.progconsult.camel.jira.Issue;
import no.progconsult.camel.jira.Issuetype;
import no.progconsult.camel.jira.Project;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.restlet.RestletComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.junit.Test;
import org.restlet.data.MediaType;

/**
 * @version
 */
public class RestletPostXmlRouteAndJSONAsReturnTest extends RestletTestSupport {

    private static final String REQUEST_MESSAGE =
            "<mail><body>HelloWorld!</body><subject>test</subject><to>x@y.net</to></mail>";

    private static final String REQUEST_MESSAGE_WITH_XML_TAG =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + REQUEST_MESSAGE;

    private String url = "restlet:http://localhost:" + portNum + "/users?restletMethod=POST";


    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                RestletComponent restlet = context.getComponent("restlet", RestletComponent.class);
                restlet.setSynchronous(true);

                // enable POST support
                from(url)
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String body = exchange.getIn().getBody(String.class);
//                            Issue body = exchange.getIn().getBody(Issue.class);
//                            assertNotNull(body);
//                            assertTrue("Get a wrong request message", body.indexOf(REQUEST_MESSAGE) >= 0);
                                exchange.getOut().setBody("{OK}");
                                exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
                            }
                        });

                // route to restlet
                from("direct:start").marshal().json(JsonLibrary.Jackson).to(url).process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println();
                    }
                });

                from("direct:get").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setHeader("CamelRestletLogin", "brynjar.norum@gmail.com");
                        exchange.getIn().setHeader("CamelRestletPassword", "BuildT1lAtlassian?");
                        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                    }
                })
//                                  .to(url);
                                  .to("restlet:http://localhost:8080/rest/api/2/issue/EM-24");
            }
        };
    }

    @Test
    public void testPostXml() throws Exception {
        postRequestMessage(REQUEST_MESSAGE);
    }

    @Test
    public void testPostXmlWithXmlTag() throws Exception {
        postRequestMessage(REQUEST_MESSAGE_WITH_XML_TAG);
    }

    @Test
    public void testGetJira() throws Exception {
        Exchange exchange = template.request("direct:get", null);
        System.out.println();
    }

    @Test
    public void testPostJira() throws Exception {
        Issue issue = new Issue();
        Fields fields = new Fields();
        issue.setFields(fields);
        fields.setDescription("test description");
        fields.setSummary("test summary");
        Project project = new Project();
        project.setId("10000");
        fields.setProject(project);
        Issuetype issuetype = new Issuetype();
        issuetype.setName("Task");
        fields.setIssuetype(issuetype);


        Exchange exchange = template.request("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(issue);
                exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
                exchange.getIn().setHeader(Exchange.ACCEPT_CONTENT_TYPE, "application/json");
                exchange.getIn().setHeader("CamelRestletLogin", "brynjar.norum@gmail.com");
                exchange.getIn().setHeader("CamelRestletPassword", "BuildT1lAtlassian?");
            }
        });
        System.out.println();
    }


    protected void postRequestMessage(final String message) throws Exception {
        Exchange exchange = template.request("direct:start", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(message);
                exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
            }
        });

        assertNotNull(exchange);
        assertTrue(exchange.hasOut());

        String s = exchange.getOut().getBody(String.class);
        assertEquals("{OK}", s);
    }
}
