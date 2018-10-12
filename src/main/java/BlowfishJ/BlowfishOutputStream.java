// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlowfishOutputStream.java

package BlowfishJ;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

// Referenced classes of package BlowfishJ:
//            SHA1, BlowfishCBC, BinConverter

public class BlowfishOutputStream extends OutputStream
{

    public BlowfishOutputStream(String passphrase, OutputStream os)
    {
        _bytes_in_buffer = 0;
        _started = false;
        _passphrase = passphrase;
        _out = os;
        SHA1 hasher = new SHA1();
        hasher.update(_passphrase);
        hasher.finalize();
        _cbc = new BlowfishCBC(hasher.getDigest(), 0L);
        hasher.clear();
        _iv = (new Random()).nextLong();
        _in_buffer = new byte[8];
        _out_buffer = new byte[8];
    }

    public void write(int b)
        throws IOException
    {
        if(!_started)
        {
            byte iv_bytes[] = new byte[8];
            BinConverter.longToByteArray(_iv, iv_bytes, 0);
            _out.write(iv_bytes, 0, iv_bytes.length);
            _cbc.setCBCIV(_iv);
            _started = true;
        }
        _bytes_in_buffer++;
        if(_bytes_in_buffer < _in_buffer.length)
        {
            _in_buffer[_bytes_in_buffer - 1] = (byte)b;
            return;
        }
        _in_buffer[_bytes_in_buffer - 1] = (byte)b;
        _bytes_in_buffer = 0;
        _cbc.encrypt(_in_buffer, _out_buffer);
        for(int i = 0; i < _out_buffer.length; i++)
            _out.write(_out_buffer[i]);

    }

    public void close()
        throws IOException
    {
        byte pad_val = (byte)(_in_buffer.length - _bytes_in_buffer);
        if(pad_val > 0)
        {
            for(; _bytes_in_buffer < _in_buffer.length; _bytes_in_buffer++)
                _in_buffer[_bytes_in_buffer] = pad_val;

            _cbc.encrypt(_in_buffer, _out_buffer);
            for(int i = 0; i < _out_buffer.length; i++)
                _out.write(_out_buffer[i]);

        }
        flush();
        _out.close();
        _cbc.cleanUp();
    }

    public void flush()
        throws IOException
    {
        _out.flush();
    }

    private OutputStream _out;
    private String _passphrase;
    private BlowfishCBC _cbc;
    private long _iv;
    byte _in_buffer[];
    byte _out_buffer[];
    int _bytes_in_buffer;
    boolean _started;
}
