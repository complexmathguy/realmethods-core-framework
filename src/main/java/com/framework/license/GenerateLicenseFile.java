package com.framework.license;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.realmethods.license:
//            JCECypher

public class GenerateLicenseFile
{

    public GenerateLicenseFile()
    {
    }

    public void generate(String destinationIPAddress, String expDate, String hostID, String outputFileName)
        throws IllegalArgumentException, Exception
    {
        StringBuffer theLicense = new StringBuffer(JCECypher.getLK());
        theLicense.append("=");
        if(!validIPAddress(destinationIPAddress))
            throw new IllegalArgumentException("Invalid IP Address - should be of the form xxx.xxx.xxx.xxx");
        Calendar expirationDate = validExpirationDate(expDate);
        if(expirationDate == null)
            throw new IllegalArgumentException("Invalid expiration date - use 0 for indefinite or the form MM-DD-YYYY");
        if(hostID == null || hostID.length() == 0)
            hostID = "localhost";
        theLicense.append(JCECypher.encryptDateElement(expirationDate.get(2)));
        theLicense.append(JCECypher.encryptDateElement(expirationDate.get(5)));
        theLicense.append(JCECypher.encryptDateElement(expirationDate.get(1)));
        long randomSeed = Calendar.getInstance().getTime().getTime();
        theLicense.append(Long.toHexString((new Random(randomSeed)).nextLong()).substring(0, 4));
        StringTokenizer tokenizer = new StringTokenizer(destinationIPAddress, ".");
        String tkn1 = "000";
        String tkn2 = "000";
        String tkn3 = "000";
        String tkn4 = "000";
        if(tokenizer.hasMoreTokens())
            tkn1 = tokenizer.nextToken();
        if(tokenizer.hasMoreTokens())
            tkn2 = tokenizer.nextToken();
        if(tokenizer.hasMoreTokens())
            tkn3 = tokenizer.nextToken();
        if(tokenizer.hasMoreTokens())
            tkn4 = tokenizer.nextToken();
        theLicense.append(JCECypher.encryptIPAddressElement(tkn1));
        theLicense.append(JCECypher.encryptIPAddressElement(tkn2));
        theLicense.append(JCECypher.encryptIPAddressElement(tkn3));
        theLicense.append(JCECypher.encryptIPAddressElement(tkn4));
        randomSeed = Calendar.getInstance().getTime().getTime();
        theLicense.append(Long.toHexString((new Random(randomSeed)).nextLong()).substring(0, 4));
        theLicense.append(JCECypher.encryptHostID(hostID));
        try
        {
            if(outputFileName == null)
                outputFileName = "framework.license";
            PrintStream licenseFile = new PrintStream(new FileOutputStream(outputFileName), true);
            licenseFile.print(theLicense.toString());
            licenseFile.close();
        }
        catch(Exception ioExc)
        {
            throw new Exception("Unable to generate the license file.");
        }
    }

    public boolean validIPAddress(String ipAddress)
    {
        boolean valid = false;
        if(ipAddress != null && ipAddress.indexOf(".") != -1)
            valid = true;
        return valid;
    }

    public Calendar validExpirationDate(String expirationDate)
    {
        Calendar expDate = null;
        if(expirationDate != null && expirationDate.length() == 10)
            try
            {
                StringTokenizer tokenizer = new StringTokenizer(expirationDate, "-");
                if(tokenizer.hasMoreTokens())
                {
                    int month = (new Integer(tokenizer.nextToken())).intValue();
                    if(tokenizer.hasMoreTokens())
                    {
                        int day = (new Integer(tokenizer.nextToken())).intValue();
                        if(tokenizer.hasMoreTokens())
                        {
                            int year = (new Integer(tokenizer.nextToken())).intValue();
                            expDate = Calendar.getInstance();
                            expDate.set(year, month, day);
                        }
                    }
                }
            }
            catch(Exception exc) { }
        return expDate;
    }

    public static void main(String args[])
    {
        GenerateLicenseFile genFile = new GenerateLicenseFile();
        boolean problem = false;
        if(args.length < 3)
            problem = true;
        if(!problem)
        {
            try
            {
                String outputFile = null;
                if(args.length == 4)
                    outputFile = args[3];
                genFile.generate(args[0], args[1], args[2], outputFile);
                System.out.println(args[0] + " : " + args[1] + " : " + args[2] + " : " + args[3]);
            }
            catch(Exception exc)
            {
                System.out.println("Problem generating the license file: " + exc);
            }
        } else
        {
            System.out.println("Invalid # of arguments: ");
            System.out.println("    arg1 - ip address - xxx.xxx.xxx.xxx");
            System.out.println("    arg2 - expiration date {mm-dd-yyyy}");
            System.out.println("    arg3 - host id - type@name@tier@version@aibExpDate");
            System.out.println("    arg4(optional) - outputfile {default=framework.license} ");
        }
    }
}
