package com.github.t1.sap.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor(access = PRIVATE)
public class StatusResponse {
    TransactionStatus transactionStatus;
}
