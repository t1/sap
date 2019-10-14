package com.github.t1.sap.test;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public enum TransactionStatus {
    /**
     * A transaction is associated with the target object and it is in the
     * active state. An implementation returns this status after a
     * transaction has been started and prior to a Coordinator issuing
     * any prepares, unless the transaction has been marked for rollback.
     */
    ACTIVE,

    /**
     * A transaction is associated with the target object and it has been
     * marked for rollback, perhaps as a result of a setRollbackOnly operation.
     */
    MARKED_ROLLBACK,

    /**
     * A transaction is associated with the target object and it has been
     * prepared. That is, all subordinates have agreed to commit. The
     * target object may be waiting for instructions from a superior as to how
     * to proceed.
     */
    PREPARED,

    /**
     * A transaction is associated with the target object and it has been
     * committed. It is likely that heuristics exist; otherwise, the
     * transaction would have been destroyed and NoTransaction returned.
     */
    COMMITTED,

    /**
     * A transaction is associated with the target object and the outcome
     * has been determined to be rollback. It is likely that heuristics exist;
     * otherwise, the transaction would have been destroyed and NoTransaction
     * returned.
     */
    ROLLEDBACK,

    /**
     * A transaction is associated with the target object but its
     * current status cannot be determined. This is a transient condition
     * and a subsequent invocation will ultimately return a different status.
     */
    UNKNOWN,

    /**
     * No transaction is currently associated with the target object. This
     * will occur after a transaction has completed.
     */
    NO_TRANSACTION,

    /**
     * A transaction is associated with the target object and it is in the
     * process of preparing. An implementation returns this status if it
     * has started preparing, but has not yet completed the process. The
     * likely reason for this is that the implementation is probably
     * waiting for responses to prepare from one or more
     * Resources.
     */
    PREPARING,

    /**
     * A transaction is associated with the target object and it is in the
     * process of committing. An implementation returns this status if it
     * has decided to commit but has not yet completed the committing process.
     * This occurs because the implementation is probably waiting for
     * responses from one or more Resources.
     */
    COMMITTING,

    /**
     * A transaction is associated with the target object and it is in the
     * process of rolling back. An implementation returns this status if
     * it has decided to rollback but has not yet completed the process.
     * The implementation is probably waiting for responses from one or more
     * Resources.
     */
    ROLLING_BACK;

    public static TransactionStatus of(TransactionManager manager) {
        try {
            return values()[manager.getStatus()];
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }
}
