package com.mosaic.io;

/**
 *
 */
public class AccountPojo {
    private String accountId;
    private String accountName;
    private long   balance;

    public AccountPojo( String accountId, String accountName, long balance ) {
        this.accountId   = accountId;
        this.accountName = accountName;
        this.balance     = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public long getBalance() {
        return balance;
    }
}
