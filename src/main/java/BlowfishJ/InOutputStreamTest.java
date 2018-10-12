// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InOutputStreamTest.java

package BlowfishJ;

import java.io.*;

// Referenced classes of package BlowfishJ:
//            BlowfishOutputStream, BlowfishInputStream

public class InOutputStreamTest
{

    public InOutputStreamTest(String name)
    {
    }

    public static void testStreams()
    {
        try
        {
            String test_string = "12345678";
            byte bytes[] = test_string.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BlowfishOutputStream bos = new BlowfishOutputStream("password", baos);
            int in;
            while((in = bais.read()) > -1) 
                bos.write(in);
            bos.flush();
            bos.close();
            bytes = baos.toByteArray();
            bais = new ByteArrayInputStream(bytes);
            BlowfishInputStream bis = new BlowfishInputStream("password", bais);
            StringBuffer sb = new StringBuffer();
            while((in = bis.read()) > -1) 
                sb.append((char)in);
            bis.close();
            if(!test_string.equals(sb.toString()))
                throw new Exception("TEXT TEST FAILED!");
            System.out.println("TEXT TEST PASSED!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void testStreams2(String test_file)
    {
        try
        {
            File original = new File(test_file);
            FileInputStream fis = new FileInputStream(original);
            File encrypted = File.createTempFile("enc", ".enc");
            encrypted.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(encrypted);
            BlowfishOutputStream bos = new BlowfishOutputStream("password", fos);
            int in;
            while((in = fis.read()) > -1) 
                bos.write(in);
            bos.close();
            fis = new FileInputStream(encrypted);
            BlowfishInputStream bis = new BlowfishInputStream("password", fis);
            File decrypted = File.createTempFile("dec", ".dec");
            decrypted.deleteOnExit();
            fos = new FileOutputStream(decrypted);
            while((in = bis.read()) > -1) 
                fos.write(in);
            fos.close();
            if(decrypted.length() != original.length())
                throw new Exception("FILE TEST FAILED: Files are different size.");
            fis = new FileInputStream(original);
            FileInputStream fis2 = new FileInputStream(decrypted);
            while((in = fis.read()) > -1) 
            {
                int in2 = fis2.read();
                if(in2 != in)
                {
                    fis.close();
                    fis2.close();
                    throw new Exception("FILE TEST FAILED: Files don't match.");
                }
            }
            fis.close();
            fis2.close();
            System.out.println("FILE TEST PASSED!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        testStreams();
        if(args.length > 0)
            testStreams2(args[0]);
        else
            System.out.println("FILE TEST NOT PERFORMED (need to pass a file name on the command line).");
    }
}
