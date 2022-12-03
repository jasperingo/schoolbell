package com.jasper.schoolbell.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

@Provider
public class HttpStatusFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        if (containerResponseContext.getStatus() == 200) {
            for (Annotation annotation : containerResponseContext.getEntityAnnotations()) {
                if(annotation instanceof HttpStatus){
                    containerResponseContext.setStatus(((HttpStatus) annotation).value().getStatusCode());
                    break;
                }
            }
        }
    }
}
