// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlowfishCBC.java

package BlowfishJ;


// Referenced classes of package BlowfishJ:
//            BlowfishECB, BinConverter

public class BlowfishCBC extends BlowfishECB
{

    public long getCBCIV()
    {
        return m_lCBCIV;
    }

    public void getCBCIV(byte dest[])
    {
        BinConverter.longToByteArray(m_lCBCIV, dest, 0);
    }

    public void setCBCIV(long lNewCBCIV)
    {
        m_lCBCIV = lNewCBCIV;
    }

    public void setCBCIV(byte newCBCIV[])
    {
        m_lCBCIV = BinConverter.byteArrayToLong(newCBCIV, 0);
    }

    public BlowfishCBC(byte bfkey[])
    {
        super(bfkey);
        setCBCIV(0L);
    }

    public BlowfishCBC(byte bfkey[], long lInitCBCIV)
    {
        super(bfkey);
        setCBCIV(lInitCBCIV);
    }

    public BlowfishCBC(byte bfkey[], byte initCBCIV[])
    {
        super(bfkey);
        setCBCIV(initCBCIV);
    }

    public void cleanUp()
    {
        m_lCBCIV = 0L;
        super.cleanUp();
    }

    private long encryptBlockCBC(long lPlainblock)
    {
        lPlainblock ^= m_lCBCIV;
        lPlainblock = super.encryptBlock(lPlainblock);
        return m_lCBCIV = lPlainblock;
    }

    private long decryptBlockCBC(long lCipherblock)
    {
        long lTemp = lCipherblock;
        lCipherblock = super.decryptBlock(lCipherblock);
        lCipherblock ^= m_lCBCIV;
        m_lCBCIV = lTemp;
        return lCipherblock;
    }

    public void encrypt(byte inbuffer[], byte outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI += 8)
        {
            long lTemp = BinConverter.byteArrayToLong(inbuffer, nI);
            lTemp = encryptBlockCBC(lTemp);
            BinConverter.longToByteArray(lTemp, outbuffer, nI);
        }

    }

    public void encrypt(byte buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI += 8)
        {
            long lTemp = BinConverter.byteArrayToLong(buffer, nI);
            lTemp = encryptBlockCBC(lTemp);
            BinConverter.longToByteArray(lTemp, buffer, nI);
        }

    }

    public void encrypt(int inbuffer[], int outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI += 2)
        {
            long lTemp = BinConverter.intArrayToLong(inbuffer, nI);
            lTemp = encryptBlockCBC(lTemp);
            BinConverter.longToIntArray(lTemp, outbuffer, nI);
        }

    }

    public void encrypt(int buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI += 2)
        {
            long lTemp = BinConverter.intArrayToLong(buffer, nI);
            lTemp = encryptBlockCBC(lTemp);
            BinConverter.longToIntArray(lTemp, buffer, nI);
        }

    }

    public void encrypt(long inbuffer[], long outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI++)
            outbuffer[nI] = encryptBlockCBC(inbuffer[nI]);

    }

    public void encrypt(long buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI++)
            buffer[nI] = encryptBlockCBC(buffer[nI]);

    }

    public void decrypt(byte inbuffer[], byte outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI += 8)
        {
            long lTemp = BinConverter.byteArrayToLong(inbuffer, nI);
            lTemp = decryptBlockCBC(lTemp);
            BinConverter.longToByteArray(lTemp, outbuffer, nI);
        }

    }

    public void decrypt(byte buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI += 8)
        {
            long lTemp = BinConverter.byteArrayToLong(buffer, nI);
            lTemp = decryptBlockCBC(lTemp);
            BinConverter.longToByteArray(lTemp, buffer, nI);
        }

    }

    public void decrypt(int inbuffer[], int outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI += 2)
        {
            long lTemp = BinConverter.intArrayToLong(inbuffer, nI);
            lTemp = decryptBlockCBC(lTemp);
            BinConverter.longToIntArray(lTemp, outbuffer, nI);
        }

    }

    public void decrypt(int buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI += 2)
        {
            long lTemp = BinConverter.intArrayToLong(buffer, nI);
            lTemp = decryptBlockCBC(lTemp);
            BinConverter.longToIntArray(lTemp, buffer, nI);
        }

    }

    public void decrypt(long inbuffer[], long outbuffer[])
    {
        int nLen = inbuffer.length;
        for(int nI = 0; nI < nLen; nI++)
            outbuffer[nI] = decryptBlockCBC(inbuffer[nI]);

    }

    public void decrypt(long buffer[])
    {
        int nLen = buffer.length;
        for(int nI = 0; nI < nLen; nI++)
            buffer[nI] = decryptBlockCBC(buffer[nI]);

    }

    long m_lCBCIV;
}
