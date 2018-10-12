package com.framework.common.email;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;


/**
 * Send email class that uses authentication
 * @author realMethods
 *
 */
public class SendMailUsingAuthentication
{
    private String SMTP_HOST_NAME = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SMTP_HOST_NAME, "" );
    private String SMTP_AUTH_USER = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SMTP_AUTH_USER, "" );
    private String SMTP_AUTH_PWD = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SMTP_AUTH_PWD, "" );
    private String SMTP_PORT = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SMTP_PORT, "25" );
    private boolean SMTP_DEBUG = new Boolean( Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SMTP_DEBUG, "true" ) ).booleanValue();
    
    public SendMailUsingAuthentication()
    {
    }
    
    public SendMailUsingAuthentication( String hostName, String user, String password )
    {
    	SMTP_HOST_NAME = hostName;
    	SMTP_AUTH_USER = user;
    	SMTP_AUTH_PWD = password;
    }
    
    public void sendEmail(String [] recipients, String subject, String message) throws MessagingException
    {
    	System.out.println( "SendMailUsingAutentication:sendEmail - host:" + SMTP_HOST_NAME + ", user:" + SMTP_AUTH_USER + ", pw: " + SMTP_AUTH_PWD);

    	boolean debug = true;

        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", SMTP_PORT);

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);

        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address...the from has to be the authuser address
        InternetAddress addressFrom = new InternetAddress(SMTP_AUTH_USER);
        msg.setFrom(addressFrom);

        InternetAddress [] addressTo = new InternetAddress[ recipients.length ];

        for (int i = 0; i < recipients.length; i++)
        {
            addressTo[ i ] = new InternetAddress(recipients[ i ]);
        }

        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator
        extends javax.mail.Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;

            return new PasswordAuthentication(username, password);
        }
    }
}
