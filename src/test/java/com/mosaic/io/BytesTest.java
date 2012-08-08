package com.mosaic.io;

import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import static org.junit.Assert.*;

/**
 *
 */
public class BytesTest {

    @Test
    public void createNewBuffer_withNegativeSize_expectException() {
        try {
            Bytes.newBuffer(-1);
            fail( "expected exception" );
        } catch ( IllegalArgumentException ex ) {
            assertEquals( "bufferSizeBytes must be greater than zero", ex.getMessage() );
        }
    }

    @Test
    public void createNewBuffer_expectGetCapacityToMatchInitialSize() {
        assertEquals( 2, Bytes.newBuffer(2).capacity() );
    }

    @Test
    public void createNewBuffer_expectGetgetPositionToReturnZero() {
        assertEquals( 0, Bytes.newBuffer(2).getPosition() );
    }

    @Test
    public void createNewBuffer_readByte_expectErrorAsBufferIsEmpty() {
        assertEquals( 0, Bytes.newBuffer( 2 ).readByte() );
    }

    @Test
    public void createNewBuffer_readChar_expectErrorAsBufferIsEmpty() {
        assertEquals( 0, Bytes.newBuffer( 2 ).readCharacter() );
    }

    @Test
    public void createNewBuffer_readBoolean_expectErrorAsBufferIsEmpty() {
        assertEquals( false, Bytes.newBuffer( 2 ).readBoolean() );
    }

    @Test
    public void createNewBuffer_readShort_expectErrorAsBufferIsEmpty() {
        assertEquals( 0, Bytes.newBuffer( 2 ).readShort() );
    }

    @Test
    public void createNewBuffer_readInt_expectErrorAsBufferIsEmpty() {
        try {
            Bytes.newBuffer(2).readInt();
            fail( "expected exception" );
        } catch ( BufferUnderflowException ex ) {
        }
    }

    @Test
    public void createNewBuffer_readLong_expectErrorAsBufferIsEmpty() {
        try {
            Bytes.newBuffer(2).readLong();
            fail( "expected exception" );
        } catch ( BufferUnderflowException ex ) {
        }
    }

    @Test
    public void createNewBuffer_readFloat_expectErrorAsBufferIsEmpty() {
        try {
            Bytes.newBuffer(2).readFloat();
            fail( "expected exception" );
        } catch ( BufferUnderflowException ex ) {
        }
    }

    @Test
    public void createNewBuffer_readDouble_expectErrorAsBufferIsEmpty() {
        try {
            Bytes.newBuffer(2).readDouble();
            fail( "expected exception" );
        } catch ( BufferUnderflowException ex ) {
        }
    }

    @Test
    public void createNewBuffer_readString_expectNull() {
        assertEquals( "", Bytes.newBuffer(1).readString() );
    }

    @Test
    public void createNewBuffer_readObject_expectErrorAsBufferIsEmpty() {
        try {
            Bytes.newBuffer(2).readObject( AccountPojoCodec.INSTANCE );
            fail( "expected exception" );
        } catch ( BufferUnderflowException ex ) {
        }
    }


// Bytes
    @Test
    public void blankBuffer10_writeByte_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeByte( (byte) 'a' );

        assertEquals( 1, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeByte_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeByte( (byte) 'a' );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeByte_expectWritableBytes9() {
        Bytes buf = new Bytes( 10 );

        buf.writeByte( (byte) 'a' );
    }

    @Test
    public void blankBuffer10_writeByteReadByte_expectCorrectByteBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeByte( (byte) 'a' );

        buf.setPosition(0);
        assertEquals( 'a', buf.readByte() );
    }

    @Test
    public void blankBuffer10_writeByteReadByte_expectgetPosition0() {
        Bytes buf = new Bytes( 10 );

        buf.writeByte( (byte) 'a' );
        buf.readByte();

        assertEquals( 2, buf.getPosition() );
    }

    @Test
    public void blankBuffer1_writeByteWriteByte_expectOverflowException() {
        Bytes buf = new Bytes( 1 );

        buf.writeByte( (byte) 'a' );

        try {
            buf.writeByte( (byte) 'a' );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer2_writeByteWriteByte_expectWritableBytes0() {
        Bytes buf = new Bytes( 2 );

        buf.writeByte( (byte) 'a' );
        buf.writeByte( (byte) 'b' );
    }

    @Test
    public void blankBuffer2_writeByteWriteByte_expectgetPosition2() {
        Bytes buf = new Bytes( 2 );

        buf.writeByte( (byte) 'a' );
        buf.writeByte( (byte) 'b' );

        assertEquals( 2, buf.getPosition() );
    }

    @Test
    public void blankBuffer2_writeByteWriteByteReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 2 );

        buf.writeByte( (byte) 'a' );
        buf.writeByte( (byte) 'b' );

        buf.setPosition(0);

        assertEquals( 'a', buf.readByte() );
        assertEquals( 'b', buf.readByte() );
    }

// Booleans
    @Test
    public void blankBuffer10_writeBoolean_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeBoolean( true );

        assertEquals( 1, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeBoolean_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeBoolean( true );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeBoolean_expectWritableBytes9() {
        Bytes buf = new Bytes( 10 );

        buf.writeBoolean( true );

        assertEquals( 1, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeBooleanReadBoolean_expectCorrectBooleanBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeBoolean( true );
        buf.setPosition(0);

        assertEquals( true, buf.readBoolean() );
    }

    @Test
    public void blankBuffer1_writeBooleanWriteBoolean_expectOverflowException() {
        Bytes buf = new Bytes( 1 );

        buf.writeBoolean( true );

        try {
            buf.writeBoolean( true );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer2_writeBooleanWriteBoolean_expectgetPosition2() {
        Bytes buf = new Bytes( 2 );

        buf.writeBoolean( true );
        buf.writeBoolean( false );

        assertEquals( 2, buf.getPosition() );
    }

    @Test
    public void blankBuffer2_writeBooleanWriteBooleanReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 2 );

        buf.writeBoolean( true );
        buf.writeBoolean( false );

        buf.setPosition(0);

        assertEquals( true, buf.readBoolean() );
        assertEquals( false, buf.readBoolean() );
    }

    @Test
    public void blankBuffer1_writeNullBooleanNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeBooleanNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readBooleanNbl() );
    }

    @Test
    public void blankBuffer1_writeTrueBooleanNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeBooleanNbl( true );

        buf.setPosition(0);

        assertTrue( buf.readBooleanNbl() == Boolean.TRUE );
    }

    @Test
    public void blankBuffer1_writeFalseBooleanNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeBooleanNbl( false );

        buf.setPosition(0);

        assertTrue( buf.readBooleanNbl() == Boolean.FALSE );
    }

    

// Characters
    @Test
    public void blankBuffer10_writeCharacter_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeCharacter( 'a' );

        assertEquals( 2, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeCharacter_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeCharacter( 'a' );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeCharacterReadCharacter_expectCorrectCharacterBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeCharacter( 'a' );

        buf.setPosition(0);

        assertEquals( 'a', buf.readCharacter() );
    }

    @Test
    public void blankBuffer2_writeCharacterWriteCharacter_expectOverflowException() {
        Bytes buf = new Bytes( 2 );

        buf.writeCharacter( 'a' );

        try {
            buf.writeCharacter( 'b' );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer3_writeCharacterWriteCharacter_expectOverflowException() {
        Bytes buf = new Bytes( 3 );

        buf.writeCharacter( 'a' );

        try {
            buf.writeCharacter( 'b' );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer4_writeCharacterWriteCharacter_expectgetPosition2() {
        Bytes buf = new Bytes( 4 );

        buf.writeCharacter( 'a' );
        buf.writeCharacter( 'b' );

        assertEquals( 4, buf.getPosition() );
    }

    @Test
    public void blankBuffer4_writeCharacterWriteCharacterReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 4 );

        buf.writeCharacter( 'a' );
        buf.writeCharacter( 'b' );

        buf.setPosition(0);

        assertEquals( 'a', buf.readCharacter() );
        assertEquals( 'b', buf.readCharacter() );
    }

    @Test
    public void blankBuffer1_writeNullCharacterNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 2 );

        buf.writeCharacterNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readCharacterNbl() );
    }

    @Test
    public void blankBuffer1_writeTrueCharacterNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 2 );

        buf.writeCharacterNbl( 'a' );

        buf.setPosition(0);

        assertEquals( new Character( 'a' ), buf.readCharacterNbl() );
    }

// Shorts
    @Test
    public void blankBuffer10_writeShort_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeShort( (short) 43 );

        assertEquals( 2, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeShort_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeShort( (short) 42 );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeShortReadShort_expectCorrectShortBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeShort( (short) 42 );

        buf.setPosition(0);

        assertEquals( (short) 42, buf.readShort() );
    }

    @Test
    public void blankBuffer2_writeShortWriteShort_expectOverflowException() {
        Bytes buf = new Bytes( 2 );

        buf.writeShort( (short) 42 );

        try {
            buf.writeShort( (short) -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer3_writeShortWriteShort_expectOverflowException() {
        Bytes buf = new Bytes( 3 );

        buf.writeShort( (short) 42 );

        try {
            buf.writeShort( (short) -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer4_writeShortWriteShort_expectgetPosition2() {
        Bytes buf = new Bytes( 4 );

        buf.writeShort( (short) 42 );
        buf.writeShort( (short) -42 );

        assertEquals( 4, buf.getPosition() );
    }

    @Test
    public void blankBuffer4_writeShortWriteShortReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 4 );

        buf.writeShort( (short)  42 );
        buf.writeShort( (short) -42 );

        buf.setPosition(0);

        assertEquals( (short)  42, buf.readShort() );
        assertEquals( (short) -42, buf.readShort() );
    }

    @Test
    public void blankBuffer1_writeNullShortNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeShortNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readShortNbl() );
    }

    @Test
    public void blankBuffer3_writeTrueShortNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 3 );

        buf.writeShortNbl( (short) 42 );

        buf.setPosition(0);

        assertEquals( new Short((short) 42), buf.readShortNbl() );
    }

    @Test
    public void blankBuffer1_writeTrueShortNbl_expectException() {
        Bytes buf = new Bytes( 1 );

        try {
            buf.writeShortNbl( (short) 42 );
            fail("expected exception");
        } catch ( BufferOverflowException ex ) {

        }
    }
    
// Integers
    @Test
    public void blankBuffer10_writeInt_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeInt( 43 );

        assertEquals( 4, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeInt_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeInt( 42 );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeIntReadInt_expectCorrectIntBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeInt( 42 );

        buf.setPosition(0);

        assertEquals( 42, buf.readInt() );
    }

    @Test
    public void blankBuffer4_writeIntWriteInt_expectOverflowException() {
        Bytes buf = new Bytes( 4 );

        buf.writeInt( 42 );

        try {
            buf.writeInt( -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer7_writeIntWriteInt_expectOverflowException() {
        Bytes buf = new Bytes( 7 );

        buf.writeInt( 42 );

        try {
            buf.writeInt( -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer8_writeIntWriteInt_expectgetPosition2() {
        Bytes buf = new Bytes( 8 );

        buf.writeInt( 42 );
        buf.writeInt( -42 );

        assertEquals( 8, buf.getPosition() );
    }

    @Test
    public void blankBuffer8_writeIntWriteIntReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 8 );

        buf.writeInt(  42 );
        buf.writeInt( -42 );

        buf.setPosition(0);

        assertEquals(  42, buf.readInt() );
        assertEquals( -42, buf.readInt() );
    }

    @Test
    public void blankBuffer1_writeNullIntNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeIntNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readIntNbl() );
    }

    @Test
    public void blankBuffer5_writeTrueIntNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 5 );

        buf.writeIntNbl( 42 );

        buf.setPosition(0);

        assertEquals( new Integer(42), buf.readIntNbl() );
    }

    @Test
    public void blankBuffer4_writeTrueIntNbl_expectException() {
        Bytes buf = new Bytes( 4 );

        try {
            buf.writeIntNbl( 42 );
            fail("expected exception");
        } catch ( BufferOverflowException ex ) {

        }
    }
    
// Longs
    @Test
    public void blankBuffer10_writeLong_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeLong( 43 );

        assertEquals( 8, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeLong_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeLong( 42 );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeLongReadLong_expectCorrectLongBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeLong( 42 );

        buf.setPosition(0);

        assertEquals( 42, buf.readLong() );
    }

    @Test
    public void blankBuffer8_writeLongWriteLong_expectOverflowException() {
        Bytes buf = new Bytes( 8 );

        buf.writeLong( 42 );

        try {
            buf.writeLong( -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer15_writeLongWriteLong_expectOverflowException() {
        Bytes buf = new Bytes( 15 );

        buf.writeLong( 42 );

        try {
            buf.writeLong( -42 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer16_writeLongWriteLong_expectgetPosition2() {
        Bytes buf = new Bytes( 16 );

        buf.writeLong( 42 );
        buf.writeLong( -42 );

        assertEquals( 16, buf.getPosition() );
    }

    @Test
    public void blankBuffer16_writeLongWriteLongReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 16 );

        buf.writeLong(  42 );
        buf.writeLong( -42 );

        buf.setPosition(0);

        assertEquals(  42, buf.readLong() );
        assertEquals( -42, buf.readLong() );
    }

    @Test
    public void blankBuffer1_writeNullLongNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeLongNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readLongNbl() );
    }

    @Test
    public void blankBuffer9_writeTrueLongNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 9 );

        buf.writeLongNbl( 42L );

        buf.setPosition(0);

        assertEquals( new Long(42), buf.readLongNbl() );
    }

    @Test
    public void blankBuffer8_writeTrueLongNbl_expectException() {
        Bytes buf = new Bytes( 8 );

        try {
            buf.writeLongNbl( 42L );
            fail("expected exception");
        } catch ( BufferOverflowException ex ) {

        }
    }
    
// Floats
    @Test
    public void blankBuffer10_writeFloat_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeFloat( 43.2f );

        assertEquals( 4, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeFloat_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeFloat( 43.2f );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeFloatReadFloat_expectCorrectFloatBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeFloat( 43.2f );

        buf.setPosition(0);

        assertEquals( 43.2f, buf.readFloat(), 0.0001f );
    }

    @Test
    public void blankBuffer4_writeFloatWriteFloat_expectOverflowException() {
        Bytes buf = new Bytes( 4 );

        buf.writeFloat( 43.2f );

        try {
            buf.writeFloat( -43.2f );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer7_writeFloatWriteFloat_expectOverflowException() {
        Bytes buf = new Bytes( 7 );

        buf.writeFloat( 43.2f );

        try {
            buf.writeFloat( -43.2f );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer8_writeFloatWriteFloat_expectgetPosition2() {
        Bytes buf = new Bytes( 8 );

        buf.writeFloat( 43.2f );
        buf.writeFloat( -43.2f );

        assertEquals( 8, buf.getPosition() );
    }

    @Test
    public void blankBuffer8_writeFloatWriteFloatReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 8 );

        buf.writeFloat(  43.2f );
        buf.writeFloat( -43.2f );

        buf.setPosition(0);

        assertEquals(  43.2f, buf.readFloat(), 0.001f );
        assertEquals( -43.2f, buf.readFloat(), 0.001f );
    }

    @Test
    public void blankBuffer1_writeNullFloatNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeFloatNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readFloatNbl() );
    }

    @Test
    public void blankBuffer5_writeTrueFloatNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 5 );

        buf.writeFloatNbl( 43.2f );

        buf.setPosition(0);

        assertEquals( new Float(43.2f), buf.readFloatNbl() );
    }

    @Test
    public void blankBuffer4_writeTrueFloatNbl_expectException() {
        Bytes buf = new Bytes( 4 );

        try {
            buf.writeFloatNbl( 43.2f );
            fail("expected exception");
        } catch ( BufferOverflowException ex ) {

        }
    }
    
// Doubles
    @Test
    public void blankBuffer10_writeDouble_expectgetPosition1() {
        Bytes buf = new Bytes( 10 );

        buf.writeDouble( 42.3 );

        assertEquals( 8, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeDouble_expectCapacity10() {
        Bytes buf = new Bytes( 10 );

        buf.writeDouble( 42.3 );

        assertEquals( 10, buf.capacity() );
    }

    @Test
    public void blankBuffer10_writeDoubleReadDouble_expectCorrectDoubleBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeDouble( 42.3 );

        buf.setPosition(0);

        assertEquals( 42.3, buf.readDouble(), 0.001 );
    }

    @Test
    public void blankBuffer8_writeDoubleWriteDouble_expectOverflowException() {
        Bytes buf = new Bytes( 8 );

        buf.writeDouble( 42.3 );

        try {
            buf.writeDouble( -42.3 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer15_writeDoubleWriteDouble_expectOverflowException() {
        Bytes buf = new Bytes( 15 );

        buf.writeDouble( 42.3 );

        try {
            buf.writeDouble( -42.3 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

    @Test
    public void blankBuffer16_writeDoubleWriteDouble_expectgetPosition2() {
        Bytes buf = new Bytes( 16 );

        buf.writeDouble( 42.3 );
        buf.writeDouble( -42.3 );

        assertEquals( 16, buf.getPosition() );
    }

    @Test
    public void blankBuffer16_writeDoubleWriteDoubleReadRead_expectCorrectBytesBack() {
        Bytes buf = new Bytes( 16 );

        buf.writeDouble(  42.3 );
        buf.writeDouble( -42.3 );

        buf.setPosition(0);

        assertEquals(  42.3, buf.readDouble(), 0.001 );
        assertEquals( -42.3, buf.readDouble(), 0.001 );
    }

    @Test
    public void blankBuffer1_writeNullDoubleNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 1 );

        buf.writeDoubleNbl( null );

        buf.setPosition(0);

        assertEquals( null, buf.readDoubleNbl() );
    }

    @Test
    public void blankBuffer9_writeTrueDoubleNbl_expectToBeAbleToReadItBack() {
        Bytes buf = new Bytes( 9 );

        buf.writeDoubleNbl( 42.3 );

        buf.setPosition(0);

        assertEquals( 42.3, buf.readDoubleNbl(), 0.001 );
    }

    @Test
    public void blankBuffer8_writeTrueDoubleNbl_expectException() {
        Bytes buf = new Bytes( 8 );

        try {
            buf.writeDoubleNbl( 42.3 );
            fail("expected exception");
        } catch ( BufferOverflowException ex ) {

        }
    }

// Strings
    @Test
    public void blankBuffer1_writeNull_expectReadable1Byte() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( null );

        assertEquals( 1, buf.getPosition() );
    }

    @Test
    public void blankBuffer1_writeNull_expectToReadItBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( null );

        buf.setPosition(0);

        assertEquals( null, buf.readString() );
    }

    @Test
    public void blankBuffer10_writeEmptyString_expectSingleReadableByte() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( "" );

        assertEquals( 1, buf.getPosition() );
    }

    @Test
    public void blankBuffer10_writeEmptyString_expectToReadItBack() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( "" );

        buf.setPosition(0);

        assertEquals( "", buf.readString() );
    }

    @Test
    public void givenBufferHoldingEmptyString_readItBack_expectInternedResult() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( "" );
        buf.writeString( "" );

        buf.setPosition(0);

        assertTrue( buf.readString() == buf.readString() );
    }

    @Test
    public void writeStringWhoseLengthFitsWithin6Bits_expectSizePrefixToTakeUp1Byte() {
        Bytes buf = new Bytes( 10 );

        buf.writeString( "hello" );
        assertEquals( 6, buf.getPosition() );

        buf.setPosition(0);

        assertEquals( "hello", buf.readString() );
    }

    @Test
    public void threeStringsInARow() {
        Bytes buf = new Bytes( 16 );

        buf.writeString( "hello" );
        buf.writeString( "my" );
        buf.writeString( "friend" );

        assertEquals( 16, buf.getPosition() );

        buf.setPosition(0);
        assertEquals( "hello", buf.readString() );
        assertEquals( "my", buf.readString() );
        assertEquals( "friend", buf.readString() );
    }

    @Test
    public void writeStringWhoseLengthFitsWithin14Bits_expectSizePrefixToTakeUp2Bytes() {
        writeReadString( 2, 0xFFFF, 0x3FFF );
    }

    @Test
    public void writeReadStringBelow1ByteBoundary() {
        writeReadString( 1, 0xFF, 0x3F - 1 );
    }

    @Test
    public void writeReadStringAbove1ByteBoundary() {
        writeReadString( 2, 0xFF, 0x3F );
    }

    @Test
    public void writeReadStringBelow3ByteBoundary() {
        writeReadString( 2, 0xFFFF, 0x3FFF );
    }

    @Test
    public void writeReadStringBelow4ByteBoundary() {
        writeReadString( 3, 0xFFFFFF, 0x3FFFFF );
    }

    @Test
    public void writeReadStringAbove3ByteBoundary() {
        writeReadString( 4, 0xFFFFFF, 0x400000 );
    }

    private void writeReadString( int sizeBlockBytes, int bufSize, int stringLength ) {
        Bytes buf = new Bytes( bufSize );

        StringBuilder b = new StringBuilder(bufSize);
        for ( int i=0; i<stringLength; i++ ) {
            b.append('a');
        }

        String str = b.toString();
        buf.writeString( str );

        assertEquals( sizeBlockBytes+stringLength, buf.getPosition() );
        assertEquals( stringLength, str.length() );

        buf.setPosition(0);

        String readBackString = buf.readString();
        assertEquals( stringLength, readBackString.length() );
        assertEquals( str, readBackString );
    }

// Objects

    @Test
    public void writeAccountPojo() {
        Bytes buf = new Bytes( 160 );

        buf.writeObject( AccountPojoCodec.INSTANCE, new AccountPojo("id1", "name1", 105) );

        buf.setPosition(0);

        AccountPojo acc = buf.readObject( AccountPojoCodec.INSTANCE );
        assertEquals( "id1",   acc.getAccountId() );
        assertEquals( "name1", acc.getAccountName() );
        assertEquals( 105,     acc.getBalance() );
    }

// narrowedView

    @Test
    public void narrowedView_setThroughView_expectToBeAbleToReadBackViaView() {
        Bytes buf  = new Bytes( 160 );
        Bytes view = buf.narrowedView( 10, 8 );

        view.writeInt( 42 );
        view.writeInt( -2 );

        view.setPosition(0);

        assertEquals( 42, view.readInt() );
        assertEquals( -2, view.readInt() );
    }

    @Test
    public void narrowedView_setThroughView_expectToBeAbleToReadBackViaParentBuffer() {
        Bytes buf  = new Bytes( 160 );
        Bytes view = buf.narrowedView( 10, 8 );

        view.writeInt( 42 );
        view.writeInt( -2 );

        buf.setPosition( 10 );
        assertEquals( 42, buf.readInt() );
        assertEquals( -2, buf.readInt() );
    }

    @Test
    public void narrowedView_overFlow_expectException() {
        Bytes buf  = new Bytes( 160 );
        Bytes view = buf.narrowedView( 10, 8 );

        view.writeInt( 42 );
        view.writeInt( -2 );

        try {
            view.writeInt( 4 );
            fail( "expected BufferOverflowException" );
        } catch ( BufferOverflowException ex ) {

        }
    }

//    @Test
    public void narrowedView_changeFromParentBuffer_expectViewToChange() {
        Bytes buf  = new Bytes( 160 );
        Bytes view = buf.narrowedView( 10, 8 );

//        buf.setWriteIndex(10);
        buf.writeInt(2);
        buf.writeInt(3);
    }



// setReadIndex

    // setReadIndex_negativeIndex_expectException
    // setReadIndex_indexEqualToWriteIndex_expectException
    // setReadIndex_indexAfterWriteIndex_expectException
    // setReadIndex_indexOneByteBeforeWriteIndex_expectSuccess


 // revamp for arrays
    // blankBuffer10_writeObject_expectgetPosition1
    // blankBuffer10_writeObject_expectCapacity10
    // blankBuffer10_writeObject_expectWritableBytes9
    // blankBuffer10_writeObjectReadObject_expectCorrectObjectBack
    // blankBuffer10_writeObjectReadObject_expectgetPosition0
    // blankBuffer1_writeObjectWriteObject_expectOverflowException
    // blankBuffer2_writeObjectWriteObject_expectWritableBytes0
    // blankBuffer2_writeObjectWriteObjectReadRead_expectgetPosition0
    // blankBuffer2_writeObjectWriteObjectReadRead_expectCorrectObjectsBack
    


// clear/seek/peek


    // boolean byte char short int long float double string Object
    // Array( boolean byte char short int long float double string Object )
}
