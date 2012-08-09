package com.mosaic.io;

/**
 *
 */
public class NioBytesTest extends SharedBytesTestCases {

    protected Bytes createBuffer( int size ) {
        return NioBytes.newBuffer( size );
    }

}
