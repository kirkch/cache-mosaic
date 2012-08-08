package com.mosaic.io;

/**
 *
 */
public class AccountPojoCodec implements Codec<AccountPojo> {
    public static final Codec<AccountPojo> INSTANCE = new AccountPojoCodec();


    @Override
    public void writeTo( Bytes buf, AccountPojo v ) {
        buf.writeString( v.getAccountId() );
        buf.writeString( v.getAccountName() );
        buf.writeLong( v.getBalance() );
    }

    @Override
    public AccountPojo readFrom( Bytes buf ) {
        String accountId   = buf.readString();
        String accountName = buf.readString();
        long   balance     = buf.readLong();

        return new AccountPojo( accountId, accountName, balance );
    }

}
