/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import com.framework.common.exception.TokenizeException;

/**    
 * Base class of all tokenized type messages. The token defines the delimeter to apply during parsing of the
 * message into its components.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class TokenizedMessage extends FrameworkMessage
    implements ITokenizedMessage
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * default constructor
     */
    public TokenizedMessage()    
    {
    }

    /**
     * Tokenizes the provided message, using the default delimeter value appliied to member
     * tokenDelims.
     * <p>
     * @param 	msg   	target to apply sub-classes tokenizing logic to
     */
    public TokenizedMessage( String msg )
    {
        super( msg );
    }

    /**
     * Tokenizes the provided message, using the provided delimeters.
     * <p>
     * @param 	msg			target to apply sub-classes tokenizing logic to
     * @param 	tkndelims	what separates each value of the msg
     */
    public TokenizedMessage( String msg, String tkndelims )
    {
        super();

        // have to assign the token delim first then apply the message....
        tokenDelims = tkndelims;

        setMessage( msg );
    }

// ITokenizedMessage implementations

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getTokenDelims()
    {
        return( tokenDelims );
    }

    /**
     * Set the delimitters used to tokenize the string
     *
     * @param	delims		what separates each value of the message
     */
    public void setTokenDelims( String delims )
    throws IllegalArgumentException
    {
        if ( delims == null || delims.length() == 0 )
            throw new IllegalArgumentException( "TokenizedMessage:setTokenDelims(...) - invalid token delimetters." ) ;

        tokenDelims = delims;
    }

// overloads from Message

    /**
     * Setthe msg
     *
     * @param		msg		of interest to tokenize
     */
    public void setMessage( Object msg )
    {
        // hold current message
        Object sMsg = getMessage();

        // assign new message
        super.setMessage( msg );

        // break it up
        try
        {
            tokenize();
        }
        catch ( TokenizeException e )
        {
            logErrorMessage( "TokenizedMessage:setMessage() - unable to assign message " + msg + " - " + e );

            // revert back to the previous msg to 
            // ensure this object is in a usable state state
            super.setMessage( sMsg );
        }
    }


// attributes    

    /**
     * token delimitters
     */
    protected String tokenDelims         = ",";
}

/*
 * Change Log:
 * $Log: TokenizedMessage.java,v $
 */
