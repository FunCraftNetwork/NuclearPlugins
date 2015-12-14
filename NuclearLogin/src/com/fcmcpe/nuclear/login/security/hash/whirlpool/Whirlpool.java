package com.fcmcpe.nuclear.login.security.hash.whirlpool;

/**
 * Created on 2015/11/30 by xtypr.
 * Package com.fcmcpe.codefuncore.security.hash.whirlpool in project CodeFunCore .
 * Code from https://github.com/rachelsilva/Whirlpool/
 */
public class Whirlpool extends WhirlpoolAbstract{

    public Whirlpool(){
        super();
    }

    /**
     * The digest function. Runs the Whirlpool Hash and fills the byte[] passed
     * in with the digest.
     */
    @Override
    public byte[] digest(byte[] source) {
        byte[][] currentState = new byte[8][8];

        for (byte aSource : source) {
            byte c = (byte) (aSource & 0xFF);
            if (!messageLimitReach()) {
                message.add(c);
                addMessageCounter();
            }
        }

        //Step 1: Do padding
        addPadding();

        //Step 2: Append Message Length
        appendMessageLength();

        //Step 3: Initialize Hash Matrix
        initialize2DByteArray(currentState);

        //Step 4: Do the blocks
        while(!message.isEmpty()){
            //Get the current message block
            byte[][] currentMessage = new byte[8][8];

            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    currentMessage[i][j] = message.remove();
                }
            }

            //Run the whirlpool block cipher
            byte[][] output = WBlockCipher(currentMessage, currentState);

            //Update the state with the Merkle-Damgard Construct
            currentState = WhirlpoolOps.matrixXOR(output, WhirlpoolOps.matrixXOR(currentMessage, currentState));
        }

        return byte2Dto1DArray(currentState);
    }

    /**
     * Method to run the W BlockCipher on the message block and key passed in.
     * Outputs the key for the next step.
     */
    protected byte[][] WBlockCipher(byte[][] message, byte[][] key){
        byte[][] roundMessage = message;
        byte[][] roundKey = key;

        //Pre-Round key XOR
        roundMessage = WhirlpoolOps.matrixXOR(roundMessage, roundKey);

        for(int i = 1; i < 11; i++){
            roundMessage = WhirlpoolOps.substituteBytes(roundMessage);
            roundMessage = WhirlpoolOps.shiftColumns(roundMessage);
            roundMessage = WhirlpoolOps.mixRows(roundMessage);
            roundKey = WhirlpoolOps.getRoundKey(roundKey, i);
            roundMessage = WhirlpoolOps.addRoundKey(roundMessage, roundKey);
        }

        return roundMessage;
    }

}