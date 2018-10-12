// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlowfishInputStream.java

package BlowfishJ;

import java.io.*;

// Referenced classes of package BlowfishJ:
//            SHA1, BlowfishCBC, BinConverter

public class BlowfishInputStream extends InputStream
{

    public BlowfishInputStream(String passphrase, InputStream is)
    {
        _bytes_read = 0;
        _buffer_index = 0;
        _started = false;
        _passphrase = passphrase;
        _in = new PushbackInputStream(new BufferedInputStream(is));
        SHA1 hasher = new SHA1();
        hasher.update(_passphrase);
        hasher.finalize();
        _cbc = new BlowfishCBC(hasher.getDigest(), 0L);
        hasher.clear();
        _in_buffer = new byte[8];
    }

    public int read()
        throws IOException
    {
        if(!_started)
        {
            decryptBuffer();
            if(_bytes_read < 8)
                return -1;
            decryptBuffer();
            if(_bytes_read == -1)
                return -1;
            _buffer_index = 0;
        }
        if(_bytes_read < _in_buffer.length && _buffer_index == _bytes_read)
            return -1;
        if(_buffer_index == _bytes_read)
        {
            decryptBuffer();
            if(_bytes_read == -1)
                return -1;
            _buffer_index = 0;
        }
        int rtn = _in_buffer[_buffer_index] & 0xff;
        _buffer_index++;
        return rtn;
    }

    private void decryptBuffer()
        throws IOException
    {
        _bytes_read = _in.read(_in_buffer, 0, _in_buffer.length);
        if(_bytes_read == -1)
            return;
        if(!_started)
            if(_bytes_read < _in_buffer.length)
            {
                return;
            } else
            {
                long iv = BinConverter.byteArrayToLong(_in_buffer, 0);
                _cbc.setCBCIV(iv);
                _started = true;
                return;
            }
        _cbc.decrypt(_in_buffer);
        int end = _in.read();
        if(end == -1)
        {
            int pad_count = _in_buffer[_in_buffer.length - 1];
            if(pad_count > _in_buffer.length || pad_count < 1)
                return;
            _bytes_read = _in_buffer.length - pad_count;
            if(_bytes_read == 0)
                _bytes_read = -1;
        } else
        {
            _in.unread(end);
        }
    }

    public boolean markSupported()
    {
        return _in.markSupported();
    }

    public void mark(int readlimit)
    {
        _in.mark(readlimit);
    }

    public int available()
        throws IOException
    {
        return _in.available();
    }

    public void close()
        throws IOException
    {
        _in.close();
        _cbc.cleanUp();
    }

    private PushbackInputStream _in;
    private String _passphrase;
    private BlowfishCBC _cbc;
    private long _iv;
    private byte _in_buffer[];
    private int _bytes_read;
    private int _buffer_index;
    private boolean _started;
}
