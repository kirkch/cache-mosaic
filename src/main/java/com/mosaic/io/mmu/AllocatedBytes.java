package com.mosaic.io.mmu;

import com.mosaic.io.Bytes;

/**
 *
 */
public class AllocatedBytes extends Bytes {

    // trade off
    // to avoid ptr look up overhead the translation needs to be done ahead of this objects creation
    // however compaction and removal would invalidate the object

    // candidate solution

    // problem: deletion of ptr and ptr gets reused so is 'valid'
    //      include a 'version' with the pointer which detects ptr reuse
    //
    // compaction moves object invalidating the buf
    //      mem region has a version, reuse buf until version changes then discard and refetch
    //
    // This reduces the overhead of translating ptr to buf every request to just checking the region version each request,
    // the ptr translation only occurs when the region version has changed



    private long ptr;

    public AllocatedBytes( long ptr, Bytes bytes ) {
        super( bytes );

        this.ptr = ptr;
    }

    public long getPointer() {
        return ptr;
    }

}
