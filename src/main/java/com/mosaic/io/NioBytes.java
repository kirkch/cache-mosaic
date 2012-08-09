package com.mosaic.io;

import com.mosaic.caches.util.Validate;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 */
public class NioBytes implements Bytes {

    private static final byte BOOLEAN_NULL_BYTE = (byte) 0xFF;

    public static Bytes newBuffer( int bufferSizeBytes ) {
        return new NioBytes( bufferSizeBytes );
    }


    private ByteBuffer buf;

    public NioBytes( int bufferSizeBytes ) {
        Validate.gtZero( bufferSizeBytes, "bufferSizeBytes" );

        this.buf = ByteBuffer.allocateDirect( bufferSizeBytes );

    }

    public NioBytes( ByteBuffer buf ) {
        this.buf = buf.duplicate();
    }

    public NioBytes( NioBytes buf ) {
        this.buf = buf.buf.duplicate();
    }


    @Override
    public Bytes setPosition( int i ) {
        buf.position( i );

        return this;
    }

    @Override
    public int getPosition() {
        return buf.position();
    }

    @Override
    public NioBytes narrowedView( int fromIndex, int numBytes ) {
        buf.position( fromIndex );
        buf.limit( fromIndex+numBytes );

        return new NioBytes(buf.slice());
    }

    @Override
    public int capacity() {
        return buf.capacity();
    }

    @Override
    public byte readByte() {
        return buf.get();
    }

    @Override
    public byte readByte( int index ) {
        return buf.get( index );
    }

    @Override
    public boolean readBoolean() {
        return buf.get() == 1;
    }

    @Override
    public boolean readBoolean( int index ) {
        return buf.get(index) == 1;
    }

    @Override
    public Boolean readBooleanNbl() {
        byte b = buf.get();

        return b == BOOLEAN_NULL_BYTE ? null : b==1;
    }

    @Override
    public Boolean readBooleanNbl( int index ) {
        byte b = buf.get( index );

        return b == BOOLEAN_NULL_BYTE ? null : b==1;
    }

    @Override
    public char readCharacter() {
        return buf.getChar();
    }

    @Override
    public char readCharacter( int index ) {
        return buf.getChar( index );
    }

    @Override
    public Character readCharacterNbl() {
        char c = buf.getChar();

        return c == 0 ? null : c;
    }

    @Override
    public Character readCharacterNbl( int index ) {
        char c = buf.getChar( index );

        return c == 0 ? null : c;
    }

    @Override
    public short readShort() {
        return buf.getShort();
    }

    @Override
    public short readShort( int index ) {
        return buf.getShort(index);
    }

    @Override
    public Short readShortNbl() {
        boolean nullFlag = readBoolean();
        if ( nullFlag ) {
            return null;
        }

        return readShort();
    }

    @Override
    public Short readShortNbl( int index ) {
        boolean nullFlag = readBoolean(index);
        if ( nullFlag ) {
            return null;
        }

        return readShort(index+1);
    }

    @Override
    public int readInt() {
        return buf.getInt();
    }

    @Override
    public int readInt( int index ) {
        return buf.getInt( index );
    }

    @Override
    public Integer readIntNbl() {
        boolean nullFlag = readBoolean();
        if ( nullFlag ) {
            return null;
        }

        return readInt();
    }

    @Override
    public Integer readIntNbl( int index ) {
        boolean nullFlag = readBoolean( index );
        if ( nullFlag ) {
            return null;
        }

        return readInt( index+1 );
    }

    @Override
    public long readLong() {
        return buf.getLong();
    }

    @Override
    public long readLong( int index ) {
        return buf.getLong( index );
    }

    @Override
    public Long readLongNbl() {
        boolean nullFlag = readBoolean();
        if ( nullFlag ) {
            return null;
        }

        return readLong();
    }

    @Override
    public Long readLongNbl( int index ) {
        boolean nullFlag = readBoolean( index );
        if ( nullFlag ) {
            return null;
        }

        return readLong( index+1 );
    }

    @Override
    public float readFloat() {
        return buf.getFloat();
    }

    @Override
    public float readFloat( int index ) {
        return buf.getFloat( index );
    }

    @Override
    public Float readFloatNbl() {
        boolean nullFlag = readBoolean();
        if ( nullFlag ) {
            return null;
        }

        return readFloat();
    }

    @Override
    public Float readFloatNbl( int index ) {
        boolean nullFlag = readBoolean( index );
        if ( nullFlag ) {
            return null;
        }

        return readFloat( index+1 );
    }

    @Override
    public double readDouble() {
        return buf.getDouble();
    }

    @Override
    public double readDouble( int index ) {
        return buf.getDouble( index );
    }

    @Override
    public Double readDoubleNbl() {
        boolean nullFlag = readBoolean();
        if ( nullFlag ) {
            return null;
        }

        return readDouble();
    }

    @Override
    public Double readDoubleNbl( int index ) {
        boolean nullFlag = readBoolean( index );
        if ( nullFlag ) {
            return null;
        }

        return readDouble( index+1 );
    }

    @Override
    public <T> T readObject( Codec<T> codec ) {
        return codec.readFrom( this );
    }

    @Override
    public <T> T readObject( int index, Codec<T> codec ) {
        int prevIndex = getPosition();

        try {
            setPosition(index);

            return codec.readFrom( this );
        } finally {
            setPosition( prevIndex );
        }
    }

    @Override
    public void writeByte( byte v ) {
        buf.put( v );
    }

    @Override
    public void writeByte( int index, byte v ) {
        buf.put( index, v );
    }

    @Override
    public void writeBoolean( boolean v ) {
        buf.put( (byte) (v ? 1 : 0) );
    }

    @Override
    public void writeBoolean( int index, boolean v ) {
        buf.put( index, (byte) (v ? 1 : 0) );
    }

    @Override
    public void writeBooleanNbl( Boolean v ) {
        if ( v == null ) {
            buf.put( BOOLEAN_NULL_BYTE );
        } else {
            buf.put( (byte) (v ? 1 : 0) );
        }
    }

    @Override
    public void writeBooleanNbl( int index, Boolean v ) {
        if ( v == null ) {
            buf.put( index, BOOLEAN_NULL_BYTE );
        } else {
            buf.put( index, (byte) (v ? 1 : 0) );
        }
    }

    @Override
    public void writeCharacter( char v ) {
        buf.putChar( v );
    }

    @Override
    public void writeCharacter( int index, char v ) {
        buf.putChar( index, v );
    }

    @Override
    public void writeCharacterNbl( Character v ) {
        buf.putChar( v == null ? 0 : v );
    }

    @Override
    public void writeCharacterNbl( int index, Character v ) {
        buf.putChar( index, v == null ? 0 : v );
    }

    @Override
    public void writeShort( short v ) {
        buf.putShort( v );
    }

    @Override
    public void writeShort( int index, short v ) {
        buf.putShort( index, v );
    }

    @Override
    public void writeShortNbl( Short v ) {
        if ( v == null ) {
            writeBoolean( true );
            return;
        }

        writeBoolean( false );
        writeShort( v );
    }

    @Override
    public void writeShortNbl( int index, Short v ) {
        if ( v == null ) {
            writeBoolean( index, true );
            return;
        }

        writeBoolean( index, false );
        writeShort( index+1, v );
    }

    @Override
    public void writeInt( int v ) {
        buf.putInt( v );
    }

    @Override
    public void writeInt( int index, int v ) {
        buf.putInt( index, v );
    }

    @Override
    public void writeIntNbl( Integer v ) {
        if ( v == null ) {
            writeBoolean( true );
            return;
        }

        writeBoolean( false );
        writeInt( v );
    }

    @Override
    public void writeIntNbl( int index, Integer v ) {
        if ( v == null ) {
            writeBoolean( index, true );
            return;
        }

        writeBoolean( index, false );
        writeInt( index+1, v );
    }

    @Override
    public void writeLong( long v ) {
        buf.putLong( v );
    }

    @Override
    public void writeLong( int index, long v ) {
        buf.putLong( index, v );
    }

    @Override
    public void writeLongNbl( Long v ) {
        if ( v == null ) {
            writeBoolean( true );
            return;
        }

        writeBoolean( false );
        writeLong( v );
    }

    @Override
    public void writeLongNbl( int index, Long v ) {
        if ( v == null ) {
            writeBoolean( index, true );
            return;
        }

        writeBoolean( index, false );
        writeLong( index+1, v );
    }

    @Override
    public void writeFloat( float v ) {
        buf.putFloat( v );
    }

    @Override
    public void writeFloat( int index, float v ) {
        buf.putFloat( index, v );
    }

    @Override
    public void writeFloatNbl( Float v ) {
        if ( v == null ) {
            writeBoolean( true );
            return;
        }

        writeBoolean( false );
        writeFloat( v );
    }

    @Override
    public void writeFloatNbl( int index, Float v ) {
        if ( v == null ) {
            writeBoolean( index, true );
            return;
        }

        writeBoolean( index, false );
        writeFloat( index+1, v );
    }

    @Override
    public void writeDouble( double v ) {
        buf.putDouble( v );
    }

    @Override
    public void writeDouble( int index, double v ) {
        buf.putDouble( index, v );
    }

    @Override
    public void writeDoubleNbl( Double v ) {
        if ( v == null ) {
            writeBoolean( true );
            return;
        }

        writeBoolean( false );
        writeDouble( v );
    }

    @Override
    public void writeDoubleNbl( int index, Double v ) {
        if ( v == null ) {
            writeBoolean( index, true );
            return;
        }

        writeBoolean( index, false );
        writeDouble( index+1, v );
    }

    @Override
    public void writeString( String v ) {
        // Strings are stored:   string length first then the string bytes in UTF-8
        // the string length is variable length, between 1 and 4 bytes; the first two bits specify how many bytes
        // null and empty string are a special case, where the entire string can be encoded into the string length byte
        if ( v == null ) {              // 0011 1111
            writeByte( (byte) 0x3F );
            return;
        } else if ( v.isEmpty() ) {
            writeByte( (byte) 0x00 );
            return;
        }

        byte[] utf8Bytes = toBytes( v );
        if ( utf8Bytes.length <  0x3F ) {
            byte mask = (byte) utf8Bytes.length;
            writeByte( mask );
        } else if ( utf8Bytes.length <=  0x3FFF ) {
            int mask = utf8Bytes.length | 0x4000;
            writeShort( (short) mask );
        } else if ( utf8Bytes.length <=  0x3FFFFF ) {
            byte byte3 = (byte)  (utf8Bytes.length  & 0x0000FF);
            byte byte2 = (byte) ((utf8Bytes.length  & 0x00FF00) >>  8);
            byte byte1 = (byte) ((utf8Bytes.length  & 0x3F0000) >> 16);

            buf.put( (byte) (byte1 | 0x80) );
            buf.put( byte2 );
            buf.put( byte3 );
        } else {
            writeInt( utf8Bytes.length | 0xC0000000 );
        }

        buf.put( utf8Bytes );
    }

    @Override
    public void writeString( int index, String v ) {
        int prevIndex = buf.position();

        try {
            setPosition( index );
            writeString(v);
        } finally {
            buf.position( prevIndex );
        }
    }

    @Override
    public String readString() {
        final byte firstByte = readByte();
        if ( firstByte == 0x3F ) {
            return null;
        } else if ( firstByte == 0x00 ) {
            return "";
        }

        int strLenEncodingType = (firstByte & 0xC0) >> 6;
        int byteLen = 0;

        switch ( strLenEncodingType ) {
            case 0:
                byteLen  = firstByte & 0x3F;
                break;
            case 1:
                byteLen  = (firstByte & 0x3F) << 8;
                byteLen |= buf.get() & 0xFF;
                break;
            case 2:
                byteLen  = ((firstByte & 0x3F) << 16) & 0xFF0000;
                byteLen |= (buf.get() <<  8) & 0x00FF00;
                byteLen |= buf.get() & 0xFF;
                break;
            case 3:
                buf.position( buf.position()-1 );

                int v2 = buf.getInt();
                byteLen = v2 & 0x3FFFFFFF;
                break;
            default: throw new IllegalStateException();
        }

        byte[] utf8Bytes = new byte[byteLen];
        buf.get( utf8Bytes, 0, byteLen );

        return toString( utf8Bytes );
    }

    @Override
    public String readString( int index ) {
        int prevIndex = buf.position();

        try {
            setPosition( index );
            return readString();
        } finally {
            buf.position( prevIndex );
        }
    }

    @Override
    public <T> void writeObject( Codec<T> codec, T obj ) {
        codec.writeTo( this, obj );
    }

    @Override
    public <T> void writeObject( int index, Codec<T> codec, T obj ) {
        int prevIndex = buf.position();

        try {
            buf.position( index );
            codec.writeTo( this, obj );
        } finally {
            buf.position( prevIndex );
        }
    }

    private static byte[] toBytes( String v ) {
        try {
            return v.getBytes( "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException( e );
        }
    }

    private static String toString( byte[] utf8Bytes ) {
        try {
            return new String( utf8Bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException( e );
        }
    }
}
