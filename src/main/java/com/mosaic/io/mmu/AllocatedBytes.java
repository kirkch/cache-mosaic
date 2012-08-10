package com.mosaic.io.mmu;

import com.mosaic.io.Bytes;
import com.mosaic.io.Codec;

/**
 *
 */
public class AllocatedBytes implements Bytes {

    // trade off
    // to avoid ptr look up overhead the translation needs to be done ahead of this objects creation
    // however compaction and removal would invalidate the object

    // candidate solution

    // problem: deletion of ptr and ptr gets reused so is 'valid'
    //      include a 'version' with the pointer which detects ptr reuse
    //
    // compaction moves object invalidating the nio buf
    //      mem region has a version, reuse buf until version changes then discard and refetch
    //
    // This reduces the overhead of translating ptr to buf every request to just checking the region version each request,
    // the ptr translation only occurs when the region version has changed



    private final long         ptr;
    private final MemoryRegion memoryRegion;

    public AllocatedBytes( long ptr, MemoryRegion memoryRegion ) {
        this.ptr          = ptr;
        this.memoryRegion = memoryRegion;
    }

    public long getPointer() {
        return ptr;
    }

    @Override
    public Bytes setPosition( int i ) {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public Bytes narrowedView( int fromIndex, int numBytes ) {
        return null;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public byte readByte() {
        return 0;
    }

    @Override
    public byte readByte( int index ) {
        return 0;
    }

    @Override
    public boolean readBoolean() {
        return false;
    }

    @Override
    public boolean readBoolean( int index ) {
        return false;
    }

    @Override
    public Boolean readBooleanNbl() {
        return null;
    }

    @Override
    public Boolean readBooleanNbl( int index ) {
        return null;
    }

    @Override
    public char readCharacter() {
        return 0;
    }

    @Override
    public char readCharacter( int index ) {
        return 0;
    }

    @Override
    public Character readCharacterNbl() {
        return null;
    }

    @Override
    public Character readCharacterNbl( int index ) {
        return null;
    }

    @Override
    public short readShort() {
        return 0;
    }

    @Override
    public short readShort( int index ) {
        return 0;
    }

    @Override
    public Short readShortNbl() {
        return null;
    }

    @Override
    public Short readShortNbl( int index ) {
        return null;
    }

    @Override
    public int readInt() {
        return 0;
    }

    @Override
    public int readInt( int index ) {
        return 0;
    }

    @Override
    public Integer readIntNbl() {
        return null;
    }

    @Override
    public Integer readIntNbl( int index ) {
        return null;
    }

    @Override
    public long readLong() {
        return 0;
    }

    @Override
    public long readLong( int index ) {
        return 0;
    }

    @Override
    public Long readLongNbl() {
        return null;
    }

    @Override
    public Long readLongNbl( int index ) {
        return null;
    }

    @Override
    public float readFloat() {
        return 0;
    }

    @Override
    public float readFloat( int index ) {
        return 0;
    }

    @Override
    public Float readFloatNbl() {
        return null;
    }

    @Override
    public Float readFloatNbl( int index ) {
        return null;
    }

    @Override
    public double readDouble() {
        return 0;
    }

    @Override
    public double readDouble( int index ) {
        return 0;
    }

    @Override
    public Double readDoubleNbl() {
        return null;
    }

    @Override
    public Double readDoubleNbl( int index ) {
        return null;
    }

    @Override
    public <T> T readObject( Codec<T> codec ) {
        return null;
    }

    @Override
    public <T> T readObject( int index, Codec<T> codec ) {
        return null;
    }

    @Override
    public void writeByte( byte v ) {
    }

    @Override
    public void writeByte( int index, byte v ) {
    }

    @Override
    public void writeBoolean( boolean v ) {
    }

    @Override
    public void writeBoolean( int index, boolean v ) {
    }

    @Override
    public void writeBooleanNbl( Boolean v ) {
    }

    @Override
    public void writeBooleanNbl( int index, Boolean v ) {
    }

    @Override
    public void writeCharacter( char v ) {
    }

    @Override
    public void writeCharacter( int index, char v ) {
    }

    @Override
    public void writeCharacterNbl( Character v ) {
    }

    @Override
    public void writeCharacterNbl( int index, Character v ) {
    }

    @Override
    public void writeShort( short v ) {
    }

    @Override
    public void writeShort( int index, short v ) {
    }

    @Override
    public void writeShortNbl( Short v ) {
    }

    @Override
    public void writeShortNbl( int index, Short v ) {
    }

    @Override
    public void writeInt( int v ) {
    }

    @Override
    public void writeInt( int index, int v ) {
    }

    @Override
    public void writeIntNbl( Integer v ) {
    }

    @Override
    public void writeIntNbl( int index, Integer v ) {
    }

    @Override
    public void writeLong( long v ) {
    }

    @Override
    public void writeLong( int index, long v ) {
    }

    @Override
    public void writeLongNbl( Long v ) {
    }

    @Override
    public void writeLongNbl( int index, Long v ) {
    }

    @Override
    public void writeFloat( float v ) {
    }

    @Override
    public void writeFloat( int index, float v ) {
    }

    @Override
    public void writeFloatNbl( Float v ) {
    }

    @Override
    public void writeFloatNbl( int index, Float v ) {
    }

    @Override
    public void writeDouble( double v ) {
    }

    @Override
    public void writeDouble( int index, double v ) {
    }

    @Override
    public void writeDoubleNbl( Double v ) {
    }

    @Override
    public void writeDoubleNbl( int index, Double v ) {
    }

    @Override
    public void writeString( String v ) {
    }

    @Override
    public void writeString( int index, String v ) {
    }

    @Override
    public String readString() {
        return null;
    }

    @Override
    public String readString( int index ) {
        return null;
    }

    @Override
    public <T> void writeObject( Codec<T> codec, T obj ) {
    }

    @Override
    public <T> void writeObject( int index, Codec<T> codec, T obj ) {
    }
}
