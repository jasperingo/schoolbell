package com.jasper.schoolbell.filters;

import javax.ws.rs.NameBinding;
import javax.ws.rs.core.Response;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpStatus {
    Response.Status value();
}
