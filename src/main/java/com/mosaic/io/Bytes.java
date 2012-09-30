package com.mosaic.io;

/**
 *
 */
public interface Bytes {
    Bytes setPosition( int i );

    int getPosition();

    Bytes narrowedView( int fromIndex, int numBytes );

    /**
     * Total size of the buffer in bytes.
     */
    int capacity();

    byte readByte();

    byte readByte( int index );

    boolean readBoolean();

    boolean readBoolean( int index );

    Boolean readBooleanNbl();

    Boolean readBooleanNbl( int index );

    char readCharacter();

    char readCharacter( int index );

    Character readCharacterNbl();

    Character readCharacterNbl( int index );

    short readShort();

    short readShort( int index );

    Short readShortNbl();

    Short readShortNbl( int index );

    int readInt();

    int readInt( int index );

    Integer readIntNbl();

    Integer readIntNbl( int index );

    long readLong();

    long readLong( int index );

    Long readLongNbl();

    Long readLongNbl( int index );

    float readFloat();

    float readFloat( int index );

    Float readFloatNbl();

    Float readFloatNbl( int index );

    double readDouble();

    double readDouble( int index );

    Double readDoubleNbl();

    Double readDoubleNbl( int index );

    <T> T readObject( Codec<T> codec );

    <T> T readObject( int index, Codec<T> codec );

    void writeByte( byte v );

    void writeByte( int index, byte v );

    void writeBoolean( boolean v );

    void writeBoolean( int index, boolean v );

    void writeBooleanNbl( Boolean v );

    void writeBooleanNbl( int index, Boolean v );

    void writeCharacter( char v );

    void writeCharacter( int index, char v );

    void writeCharacterNbl( Character v );

    void writeCharacterNbl( int index, Character v );

    void writeShort( short v );

    void writeShort( int index, short v );

    void writeShortNbl( Short v );

    void writeShortNbl( int index, Short v );

    void writeInt( int v );

    void writeInt( int index, int v );

    void writeIntNbl( Integer v );

    void writeIntNbl( int index, Integer v );

    void writeLong( long v );

    void writeLong( int index, long v );

    void writeLongNbl( Long v );

    void writeLongNbl( int index, Long v );

    void writeFloat( float v );

    void writeFloat( int index, float v );

    void writeFloatNbl( Float v );

    void writeFloatNbl( int index, Float v );

    void writeDouble( double v );

    void writeDouble( int index, double v );

    void writeDoubleNbl( Double v );

    void writeDoubleNbl( int index, Double v );

    void writeString( String v );

    void writeString( int index, String v );

    String readString();

    String readString( int index );

    <T> void writeObject( Codec<T> codec, T obj );

    <T> void writeObject( int index, Codec<T> codec, T obj );
}
