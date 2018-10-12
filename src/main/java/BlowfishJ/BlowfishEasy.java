// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlowfishEasy.java

package BlowfishJ;

import java.security.SecureRandom;
import java.util.Random;

// Referenced classes of package BlowfishJ:
//            SHA1, BlowfishCBC, BinConverter

public class BlowfishEasy
{

    public BlowfishEasy(String sPassword)
    {
        SHA1 hasher = new SHA1();
        hasher.update(sPassword);
        hasher.finalize();
        m_bfish = new BlowfishCBC(hasher.getDigest(), 0L);
        hasher.clear();
    }

    public String encryptString(String sPlainText)
    {
        long lCBCIV;
        synchronized(m_secRnd)
        {
            lCBCIV = m_secRnd.nextLong();
        }
        return encStr(sPlainText, lCBCIV);
    }

    public String encryptString(String sPlainText, Random rndGen)
    {
        long lCBCIV = rndGen.nextLong();
        return encStr(sPlainText, lCBCIV);
    }

    private String encStr(String sPlainText, long lNewCBCIV)
    {
        int nStrLen = sPlainText.length();
        byte buf[] = new byte[(nStrLen << 1 & -8) + 8];
        int nPos = 0;
        for(int nI = 0; nI < nStrLen; nI++)
        {
            char cActChar = sPlainText.charAt(nI);
            buf[nPos++] = (byte)(cActChar >> 8 & 0xff);
            buf[nPos++] = (byte)(cActChar & 0xff);
        }

        byte bPadVal = (byte)(buf.length - (nStrLen << 1));
        while(nPos < buf.length) 
            buf[nPos++] = bPadVal;
        m_bfish.setCBCIV(lNewCBCIV);
        m_bfish.encrypt(buf);
        byte newCBCIV[] = new byte[8];
        BinConverter.longToByteArray(lNewCBCIV, newCBCIV, 0);
        return BinConverter.bytesToBinHex(newCBCIV, 0, 8) + BinConverter.bytesToBinHex(buf, 0, buf.length);
    }

    public String decryptString(String sCipherText)
    {
        int nLen = sCipherText.length() >> 1 & -8;
        if(nLen < 8)
            return null;
        byte cbciv[] = new byte[8];
        int nNumOfBytes = BinConverter.binHexToBytes(sCipherText, cbciv, 0, 0, 8);
        if(nNumOfBytes < 8)
            return null;
        m_bfish.setCBCIV(cbciv);
        if((nLen -= 8) == 0)
            return "";
        byte buf[] = new byte[nLen];
        nNumOfBytes = BinConverter.binHexToBytes(sCipherText, buf, 16, 0, nLen);
        if(nNumOfBytes < nLen)
            return null;
        m_bfish.decrypt(buf);
        int nPadByte = buf[buf.length - 1] & 0xff;
        if(nPadByte > 8 || nPadByte < 0)
            nPadByte = 0;
        nNumOfBytes -= nPadByte;
        if(nNumOfBytes < 0)
            return "";
        else
            return BinConverter.byteArrayToUNCString(buf, 0, nNumOfBytes);
    }

    public void destroy()
    {
        m_bfish.cleanUp();
    }

    BlowfishCBC m_bfish;
    static SecureRandom m_secRnd = new SecureRandom();

}
