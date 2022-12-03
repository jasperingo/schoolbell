package com.jasper.schoolbell.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class HttpStatusFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        if (containerResponseContext.getStatus() == 200) {
            Arrays.stream(containerResponseContext.getEntityAnnotations())
                .filter(annotation1 -> annotation1 instanceof HttpStatus)
                .findFirst()
                .ifPresent(annotation -> containerResponseContext.setStatus(((HttpStatus) annotation).value().getStatusCode()));
        }
    }
}
