package com.fcmcpe.nuclear.login.security.hash.whirlpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created on 2015/11/30 by xtypr.
 * Package com.fcmcpe.codefuncore.security.hash.whirlpool in project CodeFunCore .
 * Code from https://github.com/rachelsilva/Whirlpool/
 */
public abstract class WhirlpoolAbstract {

    int necessaryPaddingInv = 32;

    byte[] messageCounter = new byte[32];

    Queue<Byte> message = new LinkedList<>();

    /**
     * Constructor for the Whirlpool abstract class
     * Initializes the messageCounter
     */
    public WhirlpoolAbstract(){
        initializeByteArray(messageCounter);
    }

    /**
     * Method to return the digest size
     */
    public int digestSize() {
        return 64;
    }

    /**
     * Method to digest the message, implemented by inheritors
     */
    public abstract byte[] digest(byte[] d);

    /**
     * Method to run the W block cipher, implemented by inheritors
     *
     * @param message The message block to be encrypted
     * @param key The output from the previous run of the cipher or the initialization vector.
     * @return The output of the block cipher
     */
    protected abstract byte[][] WBlockCipher(byte[][] message, byte[][] key);

    /**
     * Method to add the necessary padding to the message
     */
    protected void addPadding(){
        int necPad;
        if(necessaryPaddingInv == 0){
            necPad = 64;
        }
        else{
            necPad = 64 - necessaryPaddingInv;
        }
        byte l = (byte) 0x80; // If things are messed up later, look here.
        message.add(l);
        necPad--;
        while( necPad != 0){
            message.add((byte)0);
            necPad--;
        }
    }

    /**
     * Method to append the length of the message to the end of the message
     */
    protected void appendMessageLength(){
        for(int i = 0; i < messageCounter.length; i++){
            message.add(messageCounter[i]);
        }

        //Clear message counter
        initializeByteArray(messageCounter);
    }

    /**
     * Add a single byte to the message counter
     * Also update the necessary padding
     */
    void addMessageCounter(){
        //Update the message count
        long add = 8;
        int carry = 0;
        for (int i = messageCounter.length-1; i >= 0; i--) {
            carry += ((int)add & 0xFF) + (messageCounter[i] & 0xFF);
            messageCounter[i] = (byte)carry;
            carry = carry >>> 8;
            add = add >>> 8;
        }

        //Keep track of padding necessary (inverse)
        necessaryPaddingInv = (necessaryPaddingInv + 1) % 64;
    }

    /**
     * Method to check if we've run out of room for the message
     * @return Whether we've run out or not
     */
    boolean messageLimitReach(){
        for(int i = 0; i < messageCounter.length-2; i++){
            if(messageCounter[i] != 0xFF){
                return false;
            }
        }
        if(messageCounter[30] != 0xFE && messageCounter[31] != 0x00){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Method to convert a 2D Byte array to a 1D
     * @param array2D The 2D array to convert
     * @return The 1D array spawned from the 2D array
     */
    protected byte[] byte2Dto1DArray(byte[][] array2D){
        int rowLen = array2D.length;
        int colLen = array2D[0].length;
        byte[] array1D = new byte[rowLen * colLen];

        //loop through and populate the array
        int pos = 0;

        for(int i = 0; i < rowLen; i++){
            for(int j = 0; j < colLen; j++){
                array1D[pos] = array2D[i][j];
                pos++;
            }
        }

        return array1D;
    }

    /**
     * Method to copy values from a 1D array to another
     * @param from The array to copy from
     * @param to The array to copy to
     */
    protected void byte1DarrayCopy(byte[] from, byte[] to){
        for(int i = 0; i < from.length; i++){
            to[i] = from[i];
        }
    }

    /**
     * Method to print out a 1D byte array in a readable manner
     * Used to print out the digest once done.
     *
     * @param array The array you want to print
     * @return The array as a nice looking String
     */
    protected static String niceDisplay(byte[] array){
        String nice = "";
        for(int i = 0; i < array.length; i++){
            String temp = Integer.toHexString(array[i]);
            temp = temp.toUpperCase();
            if(temp.length() == 8){
                temp = temp.substring(6, 8);
            }
            else if(temp.length() == 1){
                temp = "0" + temp;
            }
            nice += temp;
        }
        return nice;
    }

    /**
     * Method to print out a 2D byte array in a readable manner.
     * Used for hand-testing intermediate state values
     *
     * @param array2D The 2D byte array you want to print
     * @return The 2D array as a nice looking String
     */
    protected static String niceDisplay(byte[][] array2D){
        int rowLen = array2D.length;
        int colLen = array2D[0].length;

        String nice = "";
        for(int r = 0; r < rowLen; r++){
            for(int c = 0; c < colLen; c++){
                String temp = Integer.toHexString(array2D[r][c]);
                temp = temp.toUpperCase();
                if(temp.length() == 8){
                    temp = temp.substring(6, 8);
                }
                else if(temp.length() == 1){
                    temp = "0" + temp;
                }
                nice += temp + " ";
            }
            nice += "\n";
        }
        return nice;
    }

    /**
     * Initialize a byte array with all 0's
     * @param array The array to initialize
     */
    protected void initializeByteArray(byte[] array){
        for(int i = 0; i < array.length; i++){
            array[i] = (byte)0;
        }
    }

    /**
     * Initialize a 2D byte array with all 0's
     * @param array The array to initialize
     */
    protected void initialize2DByteArray(byte[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                array[i][j] = (byte)0;
            }
        }
    }
}