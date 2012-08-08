package com.mosaic.io;

/**
 *
 */
public interface Codec<T> {

//    public Bytes toBuf( T v ) {
//        Bytes buf = Bytes.newDynamiclySizedBuffer();
//
//        writeTo( buf, v );
//
//        buf.fixSize();
//
//        return buff;
//    }

    public void writeTo( Bytes buf, T v );
    public T readFrom( Bytes buf );

}
