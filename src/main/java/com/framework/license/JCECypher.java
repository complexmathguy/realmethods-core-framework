package com.framework.license;

import BlowfishJ.BlowfishEasy;
import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class JCECypher
{

    public JCECypher()
    {
    }

    public static boolean usingGoogle()
    {
    	String licensee = getLicensee();
    	boolean usingGoogle = true;
    	
    	if ( licensee != null )
    	{
    		usingGoogle = licensee.startsWith("Google");
    	}
    	
    	return( usingGoogle );
    }
    
    public static final String getLK()
    {
        return "licenseKey";
    }

    public static final long getIPK()
    {
        return 17664L;
    }

    public static final long getDK()
    {
        return 11258L;
    }

    public static final boolean evalVersionInUse()
    {
    	boolean inUse = (HID != null && HID.lastIndexOf( "eval" ) >= 0) ? true : false;
    	
    	if ( !inUse )
    	{
    		String eval = System.getProperty( "eval" ); 
    		inUse = eval != null && eval.lastIndexOf( "true" ) >= 0;
    	}
    	
    	return inUse;
    }
        
    public static final String getHID()
    {
        return HID;
    }
    
    public static final int getMaxNumberEntities()
    {   
    	return( MaxNumEntities );
    }
    
    public static final boolean hasMaxNumberEntities()
    { return getMaxNumberEntities() > 0; }
    
    public static final boolean hasUnlimitedNumberEntities()
    { return !hasMaxNumberEntities(); }
    
    public static final String getLicensee()
    {
        return Licensee;
    }

    public static final String getVersion()
    {
        return Version;
    }

    public static final String getLicensedIPAddress()
    {
        return licensedIPAddress.toString();
    }

    public static final boolean isStandardVersion()
    {
        return HID.equalsIgnoreCase("standard");
    }

    public static final boolean isGroupVersion()
    {
        return HID.equalsIgnoreCase("group");
    }

    public static final boolean isEnterpriseVersion()
    {
        return HID.equalsIgnoreCase("enterprise");
    }

    public static final String getAIBExpDate()
    {
        return AIBExpDate;
    }

    public static boolean twist( String licenseFile, boolean createIfFailed )
    {
    	boolean retVal = twist( licenseFile );
    	
    	if ( retVal == false && createIfFailed == true )
    	{
    		if ( new java.io.File( licenseFile ).exists() == false )
    		{
    			// adjust license exp date by adding to today's date
    			int numEvalDays = 14;
    			GregorianCalendar cal = new GregorianCalendar();
    			cal.setTime(new Date());
    			cal.add(Calendar.DATE, numEvalDays);

    			// format the date
    			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			    String expDate = sdf.format(cal.getTime());
    			
				// create a community.license
			    String destinationIPAddress = "000.000.000.000";
			    String hostID = "standard@realMethods@10@1.0@" + expDate;
			    GenerateLicenseFile genFile = new GenerateLicenseFile();
			    try
			    {
			    	genFile.generate(destinationIPAddress, expDate, hostID, licenseFile );
			    	twist( licenseFile );			    	
			    	return true;
			    }
			    catch( Throwable exc )
			    {
			    	exc.printStackTrace();
			    	return false;
			    }
			    
    		}
    		else // license validation failed and the file exists
    			return false;
    	}
    	return true;
    }
    
    public static boolean twist( String licenseFile )
    {
        Properties licenseProperties = null;
        
        licenseProperties = loadLicenseFile( licenseFile );
        
        if ( licenseProperties == null )
        	return false;
        
        return( validateLicenseFile( licenseProperties ) );        
    	
    }
    

    public static String decryptHostID(String hostID)
        throws Exception
    {
        return (new BlowfishEasy("letmein2")).decryptString(hostID);
    }

    public static int decryptDateElement(String sDateElement)
    {
        int decryptedElement = 0;
        if(sDateElement != null && sDateElement.length() > 0)
            decryptedElement = (new Long(Long.parseLong(sDateElement, 16) - 11258L)).intValue();
        return decryptedElement;
    }

    public static String decryptIPAddressElement(String ipAddressElement)
    {
        String sDecryptedElement = null;
        if(ipAddressElement != null && ipAddressElement.length() > 0)
            sDecryptedElement = String.valueOf(Long.parseLong(ipAddressElement, 16) - 17664L);
        return sDecryptedElement;
    }

    public static String decryptVersion(String versionElement)
    {
        return (new BlowfishEasy("14249")).decryptString(versionElement);
    }

    public static String encryptIPAddressElement(String ipAddressElement)
    {
        String sEncryptedElement = null;
        if(ipAddressElement != null && ipAddressElement.length() > 0)
            sEncryptedElement = Long.toHexString((new Long(ipAddressElement)).longValue() + getIPK());
        return sEncryptedElement;
    }

    public static String encryptVersion(String version)
    {
        String s = (new BlowfishEasy("14249")).encryptString(version);
        return s;
    }

    public static String encryptHostID(String sHostID)
        throws Exception
    {
        String s = (new BlowfishEasy("letmein2")).encryptString(sHostID);
        return s;
    }

    public static String encryptDateElement(int dateElement)
    {
        String sEncryptedElement = null;
        sEncryptedElement = Long.toHexString((long)dateElement + getDK());
        return sEncryptedElement;
    }

    public static void terminatePlatform(String sTerminateReason)
    {
        System.out.println("\n\n*** The Platform is shutting down due to the following reason: " + sTerminateReason);
        //System.exit(-1);
    }

    public static boolean hasLicenseNotExpired()
    {
    	return( isDateGreaterThan( Calendar.getInstance(), licensedDate ) );
    }
    
    public static boolean isDateGreaterThan(Calendar calDateComparedTo, Calendar calComparingDate)
    {
        boolean bGreaterThan = false;
        int intDateComparedToMonth = calDateComparedTo.get(2);
        int intDateComparedToDay = calDateComparedTo.get(5);
        int intDateComparedToYear = calDateComparedTo.get(1);
        int intComparingDateMonth = calComparingDate.get(2);
        int intComparingDateDay = calComparingDate.get(5);
        int intComparingDateYear = calComparingDate.get(1);
        if(intComparingDateYear < intDateComparedToYear)
            bGreaterThan = false;
        else
        if(intComparingDateYear > intDateComparedToYear)
            bGreaterThan = true;
        else
        if(intComparingDateMonth < intDateComparedToMonth)
            bGreaterThan = false;
        else
        if(intComparingDateMonth > intDateComparedToMonth)
            bGreaterThan = true;
        else
        if(intComparingDateDay < intDateComparedToDay)
            bGreaterThan = false;
        else
        if(intComparingDateDay > intDateComparedToDay)
            bGreaterThan = true;
        else
            bGreaterThan = false;
        return bGreaterThan;
    }

    public static void main(String args[])
    {
    	String home = java.lang.System.getProperty( "REALMETHODS_HOME" ) != null ? java.lang.System.getProperty( "REALMETHODS_HOME" ).replace( '\\', java.io.File.separatorChar ) : ".";
    	
    	if ( home == null || home.length() == 0 )
    	{
    		System.out.println( "REALMETHODS_HOME is not defined" );
    		return;
    	}
    	
    	System.out.println( "args is " + args[0] );
    	
    	String licenseFile = "framework.license";
    	
    	if ( args.length > 0 )
    		licenseFile = args[0];
    	
    	if ( JCECypher.twist( licenseFile) )
    	{
    		outputSuccess();
    	}    	
    }
    
    public static void outputSuccess() {
		System.out.println( "Exp Date: " + new java.text.SimpleDateFormat( "MM/dd/yyyy" ).format( JCECypher.licensedDate.getTime() ) );
		System.out.println( "HID: " + JCECypher.getHID() );
		System.out.println( "Licensed IP Address: " + JCECypher.getLicensedIPAddress() );
		System.out.println( "AIB Exp Date: " + JCECypher.AIBExpDate );
		System.out.println( "Licensee: " + JCECypher.Licensee );
		System.out.println( "MaxNumEntities: " + new Integer( JCECypher.MaxNumEntities ).toString() );
		System.out.println( "Version: " + JCECypher.Version );
    }
    
    private static Properties loadLicenseFile( String licenseFile )
    {
    	Properties licenseProperties = null;
    	
        try
        {
            System.out.println("--- Attempting to locate license file " + licenseFile + " in the classpath.");
            licenseProperties = new Properties();
            
            licenseProperties.load(new BufferedReader( new FileReader(licenseFile) ) );
            
        }
        catch(Throwable licFileExc)
        {            
        	try
			{
                licenseProperties.load(new DataInputStream( Thread.currentThread().getContextClassLoader().getResourceAsStream(licenseFile)) ); 
			}
            catch( Throwable exc )
			{
            	try
            	{
            		licenseProperties.load(new DataInputStream( JCECypher.class.getClassLoader().getResourceAsStream(licenseFile) ) ); 
            	}
            	catch( Throwable exc1 )
            	{
            		System.out.println("*** ERROR: Unable to locate license file " + licenseFile);
            		return null;
            	}
			}
        }
   
    	return licenseProperties;
    }

    private static boolean validateLicenseFile( Properties licenseProperties )
	{
    	
    	boolean bIsValid = false;
    	InetAddress host = null;
    	
        try
        {
            String licenseValue = (String)licenseProperties.get("licenseKey");
            String thisIPAddress = "000.000.000.000";
            
            if(licenseValue != null)
            {
            	try
				{
            		if ( GOOGLE_LICENSE_ENV == false )
            		{
            			host = InetAddress.getLocalHost();
            			thisIPAddress = host.getHostAddress();
            		}
            		else
            			System.out.println( "Google License Env Discovered...." );
				}
            	catch( Throwable exc )
				{
            		// force it to this...
            		System.out.println( "*** Warning : License validation failed to acquire an IP address for this machine due to the following reason: " + exc.getMessage() + "\nWill force to 000.000.000.000" );
            		thisIPAddress = "000.000.000.000";
				}
                
                String monthValue = licenseValue.substring(0, 4);
                String dayValue = licenseValue.substring(4, 8);
                String yearValue = licenseValue.substring(8, 12);
                String filler = licenseValue.substring(12, 16);
                String ipAddressElement1Value = licenseValue.substring(16, 20);
                String ipAddressElement2Value = licenseValue.substring(20, 24);
                String ipAddressElement3Value = licenseValue.substring(24, 28);
                String ipAddressElement4Value = licenseValue.substring(28, 32);
                String versionElement = licenseValue.substring(32, 36);
                String blankIP = "000";
                String licenseHostID = null;
                try
                {
                    licenseHostID = licenseValue.substring(36, licenseValue.length());
                }
                catch(Exception exc) { }
                licensedIPAddress = new StringBuffer();
                if(!ipAddressElement1Value.equals(blankIP))
                    licensedIPAddress.append(decryptIPAddressElement(ipAddressElement1Value));
                if(!ipAddressElement2Value.equals(blankIP))
                {
                    licensedIPAddress.append(".");
                    licensedIPAddress.append(decryptIPAddressElement(ipAddressElement2Value));
                }
                if(!ipAddressElement3Value.equals(blankIP))
                {
                    licensedIPAddress.append(".");
                    licensedIPAddress.append(decryptIPAddressElement(ipAddressElement3Value));
                }
                if(!ipAddressElement4Value.equals(blankIP))
                {
                    licensedIPAddress.append(".");
                    licensedIPAddress.append(decryptIPAddressElement(ipAddressElement4Value));
                }
                if(!licensedIPAddress.toString().equalsIgnoreCase("0.0.0.0") && !licensedIPAddress.toString().equalsIgnoreCase("000.000.000.000") && !thisIPAddress.equalsIgnoreCase(licensedIPAddress.toString()) && !thisIPAddress.startsWith(licensedIPAddress.toString()))
                    terminatePlatform("\n\n*** You are using realMethods with an unauthorized or invalid license.\nIP Address Mismatch: Licensed IP Address - " + licensedIPAddress + ", Discovered IP Address - " + thisIPAddress + ".\n*** Contact support@realmethods.com for a valid license.");
                if(licenseHostID != null)
                {
                    String hostID = new String(decryptHostID(licenseHostID));
                    StringTokenizer tokenizer = new StringTokenizer(hostID, "@");
                    if(tokenizer.hasMoreTokens())
                    {
                        HID = tokenizer.nextToken();
                        if(tokenizer.hasMoreTokens())
                            Licensee = tokenizer.nextToken();
                        if(tokenizer.hasMoreTokens()) {                        	
                        	MaxNumEntities = new Integer( tokenizer.nextToken() ).intValue();
                        }
                        else
                        	MaxNumEntities = 0;
                        
                        if(tokenizer.hasMoreTokens())
                            Version = tokenizer.nextToken();
                        
                        if(Version == null)
                            terminatePlatform("\n\n*** You are using realMethods with an unauthorized or invalid license.\nNo version applied.\n*** Contact support@realmethods.com for a valid license.");
                        else
                        if(Version.equals("4.3.5"));
                        if(tokenizer.hasMoreTokens())
                            AIBExpDate = tokenizer.nextToken();
                    } else
                    {
                        terminatePlatform("\n\n*** Unauthorized, invalid, or corrupt license discovered.\n*** Contact support@realmethods.com for a valid license.");
                    }
                    if(!HID.equals("eval") && !HID.equals("licensed") && !HID.equals("standard") && !HID.equals("group") && !HID.equals("enterprise"))
                        terminatePlatform("\n\n*** You are using realMethods with an unauthorized or invalid license.\nIncorrect version type.\n*** Contact support@realmethods.com for a valid license.");
                } else
                {
                    terminatePlatform("\n\n*** You are using realMethods with an older or invalid license.\n*** Contact support@realmethods.com for a valid license.");
                }
                String special = "0.0.0.0";
                if(thisIPAddress.equalsIgnoreCase(licensedIPAddress.toString()) || licensedIPAddress.toString().equalsIgnoreCase(special) || thisIPAddress.startsWith(licensedIPAddress.toString()))
                {
                    Calendar today = Calendar.getInstance();
                    int licensedMonth = decryptDateElement(monthValue);
                    int licensedDay = decryptDateElement(dayValue);
                    int licensedYear = decryptDateElement(yearValue);
                    licensedDate = Calendar.getInstance();
                    licensedDate.set(licensedYear, licensedMonth - 1, licensedDay);
                    if(isDateGreaterThan(today, licensedDate))
                        bIsValid = true;
                    else
                        terminatePlatform("License file has expired - " + (new SimpleDateFormat("EEE, MMM d, ''yyyy")).format(licensedDate.getTime()) + ".  Contact support@realmethods.com.");
                } else
                {
                    terminatePlatform("License file IP Address discrepency - License IP: " + licensedIPAddress.toString() + ", Discovered IP: " + thisIPAddress + ".  Contact support@realmethods.com.");
                }
            }
        }
        catch(Exception exc)
        {
            terminatePlatform("\n** Unable to validate Platform license.  Shutting down the Platform due to the following reason - " + exc.getMessage() );
        }
    	
        if ( bIsValid )
        	outputSuccess();
        
        return( bIsValid );
	}
    
    protected static String HID = null;
    protected static String Licensee = null;
    protected static int MaxNumEntities = 0;
    protected static String Version = "100.100.100";
    protected static String AIBExpDate = "01-01-3000";
    protected static StringBuffer licensedIPAddress = new StringBuffer();
    protected static Calendar licensedDate = null;
    public static final String LICENSE_KEY = "licenseKey";
    public static final String VERSION_KEY = "14249";
    public static final long DATE_KEY = 11258L;
    public static final long IPADDRESS_KEY = 17664L;
    public static final int KEY_RADIX = 16;
    protected static final String TOKEN = "g";
    
    public static String REALMETHODS_HOME = java.lang.System.getProperty( "REALMETHODS_HOME" ) != null ? java.lang.System.getProperty( "REALMETHODS_HOME" ).replace( '\\', java.io.File.separatorChar ) : ".";
    
    public static boolean GOOGLE_LICENSE_ENV = java.lang.System.getProperty( "google.license.env" ) != null ? true : false;    


}
