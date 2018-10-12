// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BinConverter.java

package BlowfishJ;


public class BinConverter
{

    public BinConverter()
    {
    }

    public static long byteArrayToLong(byte buffer[], int nStartIndex)
    {
        return (long)buffer[nStartIndex] << 56 | ((long)buffer[nStartIndex + 1] & 255L) << 48 | ((long)buffer[nStartIndex + 2] & 255L) << 40 | ((long)buffer[nStartIndex + 3] & 255L) << 32 | ((long)buffer[nStartIndex + 4] & 255L) << 24 | ((long)buffer[nStartIndex + 5] & 255L) << 16 | ((long)buffer[nStartIndex + 6] & 255L) << 8 | (long)buffer[nStartIndex + 7] & 255L;
    }

    public static void longToByteArray(long lValue, byte buffer[], int nStartIndex)
    {
        buffer[nStartIndex] = (byte)(int)(lValue >>> 56);
        buffer[nStartIndex + 1] = (byte)(int)(lValue >>> 48 & 255L);
        buffer[nStartIndex + 2] = (byte)(int)(lValue >>> 40 & 255L);
        buffer[nStartIndex + 3] = (byte)(int)(lValue >>> 32 & 255L);
        buffer[nStartIndex + 4] = (byte)(int)(lValue >>> 24 & 255L);
        buffer[nStartIndex + 5] = (byte)(int)(lValue >>> 16 & 255L);
        buffer[nStartIndex + 6] = (byte)(int)(lValue >>> 8 & 255L);
        buffer[nStartIndex + 7] = (byte)(int)lValue;
    }

    public static long intArrayToLong(int buffer[], int nStartIndex)
    {
        return (long)buffer[nStartIndex] << 32 | (long)buffer[nStartIndex + 1] & 0xffffffffL;
    }

    public static void longToIntArray(long lValue, int buffer[], int nStartIndex)
    {
        buffer[nStartIndex] = (int)(lValue >>> 32);
        buffer[nStartIndex + 1] = (int)lValue;
    }

    public static long makeLong(int nLo, int nHi)
    {
        return (long)nHi << 32 | (long)nLo & 0xffffffffL;
    }

    public static int longLo32(long lVal)
    {
        return (int)lVal;
    }

    public static int longHi32(long lVal)
    {
        return (int)(lVal >>> 32);
    }

    public static String bytesToBinHex(byte data[])
    {
        return bytesToBinHex(data, 0, data.length);
    }

    public static String bytesToBinHex(byte data[], int nStartPos, int nNumOfBytes)
    {
        StringBuffer sbuf = new StringBuffer();
        sbuf.setLength(nNumOfBytes << 1);
        int nPos = 0;
        for(int nI = 0; nI < nNumOfBytes; nI++)
        {
            sbuf.setCharAt(nPos++, HEXTAB[data[nI + nStartPos] >> 4 & 0xf]);
            sbuf.setCharAt(nPos++, HEXTAB[data[nI + nStartPos] & 0xf]);
        }

        return sbuf.toString();
    }

    public static int binHexToBytes(String sBinHex, byte data[], int nSrcPos, int nDstPos, int nNumOfBytes)
    {
        int nStrLen = sBinHex.length();
        int nAvailBytes = nStrLen - nSrcPos >> 1;
        if(nAvailBytes < nNumOfBytes)
            nNumOfBytes = nAvailBytes;
        int nOutputCapacity = data.length - nDstPos;
        if(nNumOfBytes > nOutputCapacity)
            nNumOfBytes = nOutputCapacity;
        int nResult = 0;
        for(int nI = 0; nI < nNumOfBytes; nI++)
        {
            byte bActByte = 0;
            boolean blConvertOK = true;
            for(int nJ = 0; nJ < 2; nJ++)
            {
                bActByte <<= 4;
                char cActChar = sBinHex.charAt(nSrcPos++);
                if(cActChar >= 'a' && cActChar <= 'f')
                {
                    bActByte |= (byte)(cActChar - 97) + 10;
                    continue;
                }
                if(cActChar >= '0' && cActChar <= '9')
                    bActByte |= (byte)(cActChar - 48);
                else
                    blConvertOK = false;
            }

            if(blConvertOK)
            {
                data[nDstPos++] = bActByte;
                nResult++;
            }
        }

        return nResult;
    }

    public static String byteArrayToUNCString(byte data[], int nStartPos, int nNumOfBytes)
    {
        nNumOfBytes &= -2;
        int nAvailCapacity = data.length - nStartPos;
        if(nAvailCapacity < nNumOfBytes)
            nNumOfBytes = nAvailCapacity;
        StringBuffer sbuf = new StringBuffer();
        sbuf.setLength(nNumOfBytes >> 1);
        int nSBufPos = 0;
        for(; nNumOfBytes > 0; nNumOfBytes -= 2)
        {
            sbuf.setCharAt(nSBufPos++, (char)(data[nStartPos] << 8 | data[nStartPos + 1] & 0xff));
            nStartPos += 2;
        }

        return sbuf.toString();
    }

    static final char HEXTAB[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };

}
