package com.alesaudate.samples.test.client;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;

import org.springframework.security.core.codec.Base64;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;




public abstract class BaseTestClient extends JerseyTest {
	
	
	
	public static final String CONTEXT_PATH = "/springhibernate";
	
	public BaseTestClient() {
		super(new WebAppDescriptor.Builder("com.alesaudate.samples")
        .contextPath(CONTEXT_PATH)
        .contextParam("contextConfigLocation", "/applicationContext.xml")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
	}
	
	
	
	protected String getPath() {
		return super.getBaseURI().toASCIIString() + CONTEXT_PATH;
	}
	
	@Override
	protected TestContainerFactory getTestContainerFactory()
			throws TestContainerException {
		return new GrizzlyWebTestContainerFactory();
	}
	
	@Override
	protected URI getBaseURI() {
		
		return UriBuilder.fromUri("http://localhost/")
                .port(getPort(8080)).build();
	}
	
	@Override
	public WebResource resource() {
		
			Client client = Client.create();
			client.addFilter(new ClientFilter() {
				
				@Override
				public ClientResponse handle(ClientRequest cr)
						throws ClientHandlerException {
					
					cr.getHeaders().add(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.encode("admin:admin".getBytes())));
					return getNext().handle(cr);
				}
			});
			return client.resource("http://localhost:8080" + CONTEXT_PATH + "/person");
		
	}
	
	protected abstract String getResourcePath();

}
