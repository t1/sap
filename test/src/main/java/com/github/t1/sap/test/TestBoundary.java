package com.github.t1.sap.test;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@Boundary
public class TestBoundary {

    @Resource(lookup = "java:/TransactionManager")
    TransactionManager transactionManager;

    @GET public StatusResponse get() {
        return StatusResponse.builder()
            .transactionStatus(TransactionStatus.of(transactionManager))
            .build();
    }
}
