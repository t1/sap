package com.github.t1.sap.test;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@DomainModel
public class Person {
    @Id @GeneratedValue String id;

    String firstName;
    String lastName;
}
