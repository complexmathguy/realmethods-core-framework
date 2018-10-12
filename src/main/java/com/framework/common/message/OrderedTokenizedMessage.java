/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import java.util.ArrayList;
import java.util.Collection;

import com.framework.common.exception.TokenizeException;

/**
 * Base class of all ordered tokenized messages.  An ordered tokenized entity implies 
 * that order is important in parsing and discovering values.  For instance:
 * <p>
 * <i>&quotJoe!*!Smith!*!34&quot</i>
 * <p>
 * implies a String with 3 fields <first_name, last_name, age> where the position of 
 * each field is well  known.
 * <p>
 * @author    realMethods, Inc.
 */
public class OrderedTokenizedMessage extends TokenizedMessage
    implements IOrderedTokenizedMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public OrderedTokenizedMessage()
    {
    }

    /**
     * constructor
     *
     * @param	msg		target to apply ordered tokenizing logic to
     */
    public OrderedTokenizedMessage( String msg )
    {
        // order here is important
        super( );

        setMessage( msg );
    }

    /**
     * @param       msg			target to apply ordered tokenizing logic to
     * @param       delims		token delimeter
     */
    public OrderedTokenizedMessage( String msg, String delims )
    {
        // order here is important
        super();

        setTokenDelims( delims );

        setMessage( msg );
    }

// IOrderedTokenizedMessage implementations

    /**
     * Returns the zero-based index within a Collection of Strings
     *
     * @param       zeroBasedindex		zero-based position into the Collection of tokenized values
     * @return      the result as a String
     */
    public String getValue( int zeroBasedindex )
    throws IndexOutOfBoundsException
    {
        if ( zeroBasedindex >= values.size() )
            throw new IndexOutOfBoundsException( "OrderedTokenizedMessage:getValue(...) - index out of range. " );

        return( (String)values.toArray()[( zeroBasedindex )] );
    }


    /**
     * Returns a Collection of the parsed values
     *
     * @return       Collection
     */
    final public Collection getValues()
    {
        return( values );
    }

    /**
     * Tokenizes the message using the assigned delimeter, and places each token
     * into a Collection of values
     */
    public void tokenize()
    throws TokenizeException
    {
        String sMsg = getMessageAsString();

        // lazy initialzation
        if ( values == null )
        {
            values = new ArrayList( 10 );
        }
        else
        {
            values.clear();
        }

        int iStartIndex;
        int iEndIndex;
        String sDelim = getTokenDelims();

        //trim whitespace
        sMsg = sMsg.trim();

        //strip any leading delimiter
        if ( sMsg.startsWith( sDelim ) )
            iStartIndex = sDelim.length();
        else
            iStartIndex = 0;

        iEndIndex = sMsg.indexOf( sDelim, iStartIndex);
        int i = 0;

        //iterate through message, adding each token
        while ( iEndIndex != -1 )
        {
            if ( iStartIndex == iEndIndex )
                values.add( "" );
            else
                values.add( sMsg.substring(iStartIndex, iEndIndex) );

            iStartIndex = iEndIndex + sDelim.length();
            iEndIndex = sMsg.indexOf( sDelim, iStartIndex);
            i++;
        }

        // if the last token was not followed by a delimiter, need to attach the last token
        if ( !sMsg.endsWith( sDelim ) )
        {
            values.add( sMsg.substring( iStartIndex ) );
            i++;
        }

    }


// attributes

	/**
	 * Holder for parsed tokens.
	 */
    protected Collection values           = new ArrayList();
}

/*
 * Change Log:
 * $Log: OrderedTokenizedMessage.java,v $
 */
