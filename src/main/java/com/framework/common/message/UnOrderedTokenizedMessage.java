/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.framework.common.exception.TokenizeException;

/**    
 * Base class of all unordered tokenized messages.  An unordered tokenized entity implies 
 * that order is not important in parsing and discovering values.  For instance:
 * <p>
 * "first_name=Joe!*!last_name=Smith!*!Age=34" 
 * <p>
 * implies a String with 3 fields <first_name, last_name, age> where the position of 
 * each field is not known, but rather represented by a delimited set of key/value pairings.
 * <p>
 * @author    realMethods, Inc.
 */
public class UnOrderedTokenizedMessage extends TokenizedMessage
    implements IUnOrderedTokenizedMessage
{
//****************************************************
// Public Methods
//****************************************************
// constructors

    /**
     * default constructor
     */
    public UnOrderedTokenizedMessage()    
    {
    }

    /**
     * constructor
     *
     * @param		msg		target to apply unordered tokenizing logic to
     */
    public UnOrderedTokenizedMessage( String msg )
    {
        // order here is important
        super();

        setMessage( msg );
    }

    /**
     * @param       msg			target to apply unordered tokenizing logic to
     * @param       delims		token delimeter
     */
    public UnOrderedTokenizedMessage( String msg, String delims )
    {
        // order here is important
        super();

        setTokenDelims( delims );

        setMessage( msg );
    }

    /**
     * @param       msg			target to apply unordered tokenizing logic to
     * @param       delims		token delimeter
     * @param		keyDelim	the delimeter of each key/value pair to be tokenized
     */
    public UnOrderedTokenizedMessage( String msg, String delims, String keyDelim )
    {
    	// can't call to base class since it will call tokenize() to be called
    	// but keyValueDelim will not yet been assigned

		setTokenDelims( delims );
        this.keyValueDelim = keyDelim;
        
        // now safe to assign the message
        setMessage( msg );
        
    }

// UnOrderedTokenizedMessage implementations

    /**
     * returns the value corresponding to the provided key
     * 
     * @param       key			
     * @return      String
     */
    public String getValue( String key )
    {
        return( (String)getValues().get( key ) );
    }

    /**
     * returns a Map of the parsed values as a Map.  Null means none applied.
     *
     * @return       Map
     */
    final public Map getValues()
    {
	    return( values );    	
    }

    /**
     * returns the token delimitter used for the key/value pairings
     *
     * @return      String
     */
    final public String getKeyValueDelim()
    {
        return( keyValueDelim );
    }

    /**
     * Set the message and associate the key_value_delim
     *
     * @param       msg				msg to apply tokenizing logic
     * @param       keyValueDelim	delimeter for each key/value pair to be tokenized
     */
    public void setMessage( String msg, String keyValueDelim )
    {
        this.keyValueDelim = keyValueDelim;

        super.setMessage( msg );
    }

    /**
     * tokenizes the message
     */
    public void tokenize()
    throws TokenizeException
    {
        String sToken                       = null;
        String sKey                         = null;
        StringTokenizer tokenizer           = new StringTokenizer( getMessageAsString(), getTokenDelims() );
        StringTokenizer keyValueTokenizer   = null;

        // clear out the Map
        values = new HashMap( tokenizer.countTokens() );

        // first, extract each token from the message
        while ( tokenizer.hasMoreTokens() )
        {
            sToken = tokenizer.nextToken();

            // next, tokenize the token itself to break up the key value pair
            // it should generate two tokens...the 1st is the key, the 2nd is the value
            keyValueTokenizer = new StringTokenizer( sToken, getKeyValueDelim() );
	
            if ( keyValueTokenizer.hasMoreTokens() )
            {
                // assign the key
                sKey = keyValueTokenizer.nextToken();

                if ( keyValueTokenizer.hasMoreTokens() )
                {
                    // assign the value using the current key
                    values.put( sKey, keyValueTokenizer.nextToken() );
                }
                else
                    throw new TokenizeException( "UnOrderedTokenizedMessage:tokenize() - ill-formed key/value pairing - " + sToken );
            }
            else
                throw new TokenizeException( "UnOrderedTokenizedMessage:tokenize() - ill-formed token - " + sToken );
        }

    }


// attributes

    /**
     * delim used to separate key value pairings
     */
    protected String keyValueDelim           = new String( "=" );  

    /**
     * key/value pairings
     */
    protected HashMap values                = null;
}

/*
 * Change Log:
 * $Log: UnOrderedTokenizedMessage.java,v $
 */
