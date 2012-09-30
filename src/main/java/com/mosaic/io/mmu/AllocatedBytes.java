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

    // problem: after ptr is deleted, it will get reused at some point; thus the pointer stored in this object would appear valid
    //      however it wouuld be pointing at a different object.
    //      solution: include a 'version' with the pointer which detects ptr reuse
    //
    // compaction moves objects; invalidating the nio buf stored within the AllocatedBytes object
    //      mem region has a version, reuse buf until version changes then discard and refetch
    //
    // This reduces the overhead of translating ptr to buf every request to just checking the region version each request,
    // the ptr translation only occurs when the region version has changed



    private final int          ptr;
    private final MemoryRegion memoryRegion;
    private final int          fromIndex;
    private final int          numBytes;

    private       int          memoryRegionVersion;
    private       Bytes        bytesView;

    public AllocatedBytes( int ptr, MemoryRegion memoryRegion ) {
        this( ptr, memoryRegion, -1, -1 );
    }

    private AllocatedBytes( int ptr, MemoryRegion memoryRegion, int fromIndex, int numBytes ) {
        this.ptr                 = ptr;
        this.memoryRegion        = memoryRegion;
        this.memoryRegionVersion = memoryRegion.getVersion();
        this.bytesView           = memoryRegion.fetchUnderlyingView( ptr );
        this.fromIndex           = fromIndex;
        this.numBytes            = numBytes;

        if ( numBytes > 0 ) {
            this.bytesView = bytesView.narrowedView( fromIndex, numBytes );
        }
    }

    public long getPointer() {
        return ptr;
    }

    private void validateBytesView() {
        memoryRegion.throwIfInvalidPointer( ptr );

        if ( memoryRegion.getVersion() != memoryRegionVersion ) {
            this.memoryRegionVersion = memoryRegion.getVersion();
            this.bytesView           = memoryRegion.fetchUnderlyingView( ptr );

            if ( numBytes > 0 ) {
                this.bytesView = bytesView.narrowedView( fromIndex, numBytes );
            }
        }
    }

    @Override
    public Bytes setPosition( int i ) {
        validateBytesView();

        return bytesView.setPosition(i);
    }

    @Override
    public int getPosition() {
        validateBytesView();

        return bytesView.getPosition();
    }

    @Override
    public Bytes narrowedView( int fromIndex, int numBytes ) {
        validateBytesView();

        return new AllocatedBytes( ptr, memoryRegion, fromIndex, numBytes );
    }

    @Override
    public int capacity() {
        validateBytesView();

        return bytesView.capacity();
    }

    @Override
    public byte readByte() {
        validateBytesView();

        return bytesView.readByte();
    }

    @Override
    public byte readByte( int index ) {
        validateBytesView();

        return bytesView.readByte(index);
    }

    @Override
    public boolean readBoolean() {
        validateBytesView();

        return bytesView.readBoolean();
    }

    @Override
    public boolean readBoolean( int index ) {
        validateBytesView();

        return bytesView.readBoolean(index);
    }

    @Override
    public Boolean readBooleanNbl() {
        validateBytesView();

        return bytesView.readBooleanNbl();
    }

    @Override
    public Boolean readBooleanNbl( int index ) {
        validateBytesView();

        return bytesView.readBooleanNbl(index);
    }

    @Override
    public char readCharacter() {
        validateBytesView();

        return bytesView.readCharacter();
    }

    @Override
    public char readCharacter( int index ) {
        validateBytesView();

        return bytesView.readCharacter(index);
    }

    @Override
    public Character readCharacterNbl() {
        validateBytesView();

        return bytesView.readCharacterNbl();
    }

    @Override
    public Character readCharacterNbl( int index ) {
        validateBytesView();

        return bytesView.readCharacterNbl(index);
    }

    @Override
    public short readShort() {
        validateBytesView();

        return bytesView.readShort();
    }

    @Override
    public short readShort( int index ) {
        validateBytesView();

        return bytesView.readShort(index);
    }

    @Override
    public Short readShortNbl() {
        validateBytesView();

        return bytesView.readShortNbl();
    }

    @Override
    public Short readShortNbl( int index ) {
        validateBytesView();

        return bytesView.readShortNbl(index);
    }

    @Override
    public int readInt() {
        validateBytesView();

        return bytesView.readInt();
    }

    @Override
    public int readInt( int index ) {
        validateBytesView();

        return bytesView.readInt(index);
    }

    @Override
    public Integer readIntNbl() {
        validateBytesView();

        return bytesView.readIntNbl();
    }

    @Override
    public Integer readIntNbl( int index ) {
        validateBytesView();

        return bytesView.readIntNbl(index);
    }

    @Override
    public long readLong() {
        validateBytesView();

        return bytesView.readLong();
    }

    @Override
    public long readLong( int index ) {
        validateBytesView();

        return bytesView.readLong(index);
    }

    @Override
    public Long readLongNbl() {
        validateBytesView();

        return bytesView.readLongNbl();
    }

    @Override
    public Long readLongNbl( int index ) {
        validateBytesView();

        return bytesView.readLongNbl(index);
    }

    @Override
    public float readFloat() {
        validateBytesView();

        return bytesView.readFloat();
    }

    @Override
    public float readFloat( int index ) {
        validateBytesView();

        return bytesView.readFloat(index);
    }

    @Override
    public Float readFloatNbl() {
        validateBytesView();

        return bytesView.readFloatNbl();
    }

    @Override
    public Float readFloatNbl( int index ) {
        validateBytesView();

        return bytesView.readFloatNbl(index);
    }

    @Override
    public double readDouble() {
        validateBytesView();

        return bytesView.readDouble();
    }

    @Override
    public double readDouble( int index ) {
        validateBytesView();

        return bytesView.readDouble(index);
    }

    @Override
    public Double readDoubleNbl() {
        validateBytesView();

        return bytesView.readDoubleNbl();
    }

    @Override
    public Double readDoubleNbl( int index ) {
        validateBytesView();

        return bytesView.readDoubleNbl(index);
    }

    @Override
    public <T> T readObject( Codec<T> codec ) {
        validateBytesView();

        return bytesView.readObject(codec);
    }

    @Override
    public <T> T readObject( int index, Codec<T> codec ) {
        validateBytesView();

        return bytesView.readObject(index,codec);
    }

    @Override
    public void writeByte( byte v ) {
        validateBytesView();

        bytesView.writeByte(v);
    }

    @Override
    public void writeByte( int index, byte v ) {
        validateBytesView();

        bytesView.writeByte(index, v);
    }

    @Override
    public void writeBoolean( boolean v ) {
        validateBytesView();

        bytesView.writeBoolean(v);
    }

    @Override
    public void writeBoolean( int index, boolean v ) {
        validateBytesView();

        bytesView.writeBoolean(index,v);
    }

    @Override
    public void writeBooleanNbl( Boolean v ) {
        validateBytesView();

        bytesView.writeBooleanNbl(v);
    }

    @Override
    public void writeBooleanNbl( int index, Boolean v ) {
        validateBytesView();

        bytesView.writeBooleanNbl(index, v);
    }

    @Override
    public void writeCharacter( char v ) {
        validateBytesView();

        bytesView.writeCharacter(v);
    }

    @Override
    public void writeCharacter( int index, char v ) {
        validateBytesView();

        bytesView.writeCharacter(index, v);
    }

    @Override
    public void writeCharacterNbl( Character v ) {
        validateBytesView();

        bytesView.writeCharacterNbl(v);
    }

    @Override
    public void writeCharacterNbl( int index, Character v ) {
        validateBytesView();

        bytesView.writeCharacterNbl(index, v);
    }

    @Override
    public void writeShort( short v ) {
        validateBytesView();

        bytesView.writeShort(v);
    }

    @Override
    public void writeShort( int index, short v ) {
        validateBytesView();

        bytesView.writeShort(index, v);
    }

    @Override
    public void writeShortNbl( Short v ) {
        validateBytesView();

        bytesView.writeShortNbl(v);
    }

    @Override
    public void writeShortNbl( int index, Short v ) {
        validateBytesView();

        bytesView.writeShortNbl(index, v);
    }

    @Override
    public void writeInt( int v ) {
        validateBytesView();

        bytesView.writeInt(v);
    }

    @Override
    public void writeInt( int index, int v ) {
        validateBytesView();

        bytesView.writeInt(index, v);
    }

    @Override
    public void writeIntNbl( Integer v ) {
        validateBytesView();

        bytesView.writeIntNbl(v);
    }

    @Override
    public void writeIntNbl( int index, Integer v ) {
        validateBytesView();

        bytesView.writeIntNbl(index, v);
    }

    @Override
    public void writeLong( long v ) {
        validateBytesView();

        bytesView.writeLong(v);
    }

    @Override
    public void writeLong( int index, long v ) {
        validateBytesView();

        bytesView.writeLong(index, v);
    }

    @Override
    public void writeLongNbl( Long v ) {
        validateBytesView();

        bytesView.writeLongNbl(v);
    }

    @Override
    public void writeLongNbl( int index, Long v ) {
        validateBytesView();

        bytesView.writeLongNbl(index, v);
    }

    @Override
    public void writeFloat( float v ) {
        validateBytesView();

        bytesView.writeFloat(v);
    }

    @Override
    public void writeFloat( int index, float v ) {
        validateBytesView();

        bytesView.writeFloat(index, v);
    }

    @Override
    public void writeFloatNbl( Float v ) {
        validateBytesView();

        bytesView.writeFloatNbl(v);
    }

    @Override
    public void writeFloatNbl( int index, Float v ) {
        validateBytesView();

        bytesView.writeFloatNbl(index, v);
    }

    @Override
    public void writeDouble( double v ) {
        validateBytesView();

        bytesView.writeDouble(v);
    }

    @Override
    public void writeDouble( int index, double v ) {
        validateBytesView();

        bytesView.writeDouble(index, v);
    }

    @Override
    public void writeDoubleNbl( Double v ) {
        validateBytesView();

        bytesView.writeDoubleNbl(v);
    }

    @Override
    public void writeDoubleNbl( int index, Double v ) {
        validateBytesView();

        bytesView.writeDoubleNbl(index, v);
    }

    @Override
    public void writeString( String v ) {
        validateBytesView();

        bytesView.writeString(v);
    }

    @Override
    public void writeString( int index, String v ) {
        validateBytesView();

        bytesView.writeString(index, v);
    }

    @Override
    public String readString() {
        validateBytesView();

        return bytesView.readString();
    }

    @Override
    public String readString( int index ) {
        validateBytesView();

        return bytesView.readString(index);
    }

    @Override
    public <T> void writeObject( Codec<T> codec, T obj ) {
        validateBytesView();

        bytesView.writeObject(codec, obj);
    }

    @Override
    public <T> void writeObject( int index, Codec<T> codec, T obj ) {
        validateBytesView();

        bytesView.writeObject(index, codec, obj);
    }
}
