package com.github.t1.sap.test.annotations;

import javax.ejb.Stateless;
import javax.enterprise.inject.Stereotype;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@Retention(RUNTIME)
@Stateless
public @interface Boundary {}
