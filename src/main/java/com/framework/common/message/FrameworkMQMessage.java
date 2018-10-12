/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import java.util.StringTokenizer;

import com.ibm.mq.MQC;
import com.ibm.mq.MQMessage;

/**
 * Extends the notion of a Message by containing an MQMessage
 *
 * <p>
 * @author    realMethods, Inc.
 * @see       com.framework.integration.objectpool.mq.FrameworkMQAdapter
 * @see		  com.framework.integration.objectpool.mq.FrameworkMQQueue
 * @see		  com.framework.integration.objectpool.mq.MQConnectionImpl
 */
public class FrameworkMQMessage extends OrderedTokenizedMessage
    implements IFrameworkMQMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkMQMessage()
    {
    }

    /**
     * Assume the usage of a standard MQ delimeter
     * @param     msg
     */
    public FrameworkMQMessage( String msg )
    {
        super( msg, standardMQDelimiter );
    }    

    /**
     * @param 		msg   		
     * @param       delim     what separates each section of the message
     */
    public FrameworkMQMessage( String msg, String delim )
    {
        super( msg, delim );
    }    

// overloads from IMQMessage

    /**
     * Formats the underlying message upon request
     */
    public void formatOnSend()
    {
        try
        {
            // clear out any existing message
            mqMessage.clearMessage();

            // apply default settings to the MQMessage before sending
            applyDefaultsToMQMessage();

            // if the message has a header on it, first must do some stuff before sending it...

            if ( messageHasAHeader() )
            {
                reformatConsideringHeader();                
            }
            else
            {
                logDebugMessage( "FrameworkMQMessage.formatOSend() - No msg header, so writing the string..." + getMessageAsString());

                // assign to the parent's MQMessage
                mqMessage.writeString( getMessageAsString() ); 
            }                
        }
        catch ( Exception exc )
        {
            logErrorMessage( "MQMessage:formatOnSend() - " + exc );
        }
    }

    public void formatOnReceive()
    {
        // no-op by default         
    }

    /**
     * returns the standard token delimiter
     *
     * @return      String
     */
    public String getStandardDelimiter()
    {
        return( standardMQDelimiter );
    }

    /**
     * apply the correlation ID
     *
     * @param 	correlationID		unique identifer used in a send/receive handshake
     */    
    public void setCorrelationID( byte[] correlationID )
    {
        if ( correlationID != null )
        {
            mqMessage.correlationId = correlationID;
        }
        else
        {
            mqMessage.correlationId = null;
        }        
   }

// accessor methods

    /**
     * returns the contained MQMessage
     *
     * @return      MQMessage
     */
    public com.ibm.mq.MQMessage getMQMessage()
    {
        return( mqMessage );
    }

// helper methods

    /**
     * apply default settings to the message
     */
    protected void applyDefaultsToMQMessage()
    {
        mqMessage.encoding    = MQC.MQENC_INTEGER_REVERSED;
        mqMessage.persistence = MQC.MQPER_PERSISTENT;
        mqMessage.messageId   = MQC.MQMI_NONE;        
    }

    /**
     * answers whether or not he message has a header on it
     *
     * @return      boolean
     */
    protected boolean messageHasAHeader()
    {
        boolean bHas = false;

        String sMsg = getMessageAsString();

        if ( sMsg.startsWith( "RFH " ) )
        {
            bHas = true;
        }

        return( bHas );            

    }

    /**
     * handles manipulating the message in light of the existence of a header
     */
    protected void reformatConsideringHeader()
    {
        // tokenize the message
        StringTokenizer tokenizer   = new StringTokenizer( getMessageAsString(), "|**|**|" );
        StringBuffer sTheMessage    = new StringBuffer();

        mqMessage.format = "MQHRF   ";

        // assigning the header values
        try
        {
            mqMessage.writeString( tokenizer.nextToken() );                           // MQHeader_strucid
            mqMessage.writeInt( new Integer( tokenizer.nextToken() ).intValue() );    // MQHeader_version
            mqMessage.writeInt( new Integer( tokenizer.nextToken() ).intValue() );    // MQHeader_struclength
            mqMessage.writeInt( new Integer( tokenizer.nextToken() ).intValue() );    // MQHeader_encoding
            mqMessage.writeInt( new Integer( tokenizer.nextToken() ).intValue() );    // MQHeader_CCSID
            mqMessage.writeString( tokenizer.nextToken() );                           // MQHeader_format
            mqMessage.writeInt( new Integer( tokenizer.nextToken() ).intValue() );    // MQHeader_flags
            mqMessage.writeString( tokenizer.nextToken() );                           // MQHeader_appgrp
            mqMessage.writeString( tokenizer.nextToken() );                           // MQHeader_msgtyp

            // the remainder is the message itself		
            while ( tokenizer.hasMoreTokens() )
            {
                sTheMessage.append( tokenizer.nextToken() );
                sTheMessage.append( getStandardDelimiter() );
            }

            mqMessage.writeString( sTheMessage.toString() );
        }
        catch ( Exception exc )
        {
        }
    }
    
// attributes

	/**
	 * the wrapped IBM MQ message
	 */
    protected MQMessage mqMessage  					= new MQMessage();
    /**
     * a default delimeter
     */
    static protected String standardMQDelimiter  	= new String( "|**|**|" );
}

/*
 * Change Log:
 * $Log: FrameworkMQMessage.java,v $
 */
