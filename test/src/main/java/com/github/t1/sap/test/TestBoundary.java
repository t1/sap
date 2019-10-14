package com.github.t1.sap.test;

import com.github.t1.sap.test.annotations.Boundary;

import javax.annotation.Resource;
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
