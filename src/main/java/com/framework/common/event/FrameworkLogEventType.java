/************************************************************************ 
*   realMethods: Professionally Supported Open Source Framework
*
*   The contained free software can be redistributed and/or modified
*   under the terms of the GNU Lesser cGeneral Public License as published by
*   the Free Software Foundation.  See terms of license at gnu.org.
*
*   This program is distributed WITHOUT ANY WARRANTY.  See the
*   GNU General Public License for more details.
*************************************************************************/

package com.framework.common.event;

import com.framework.common.FrameworkBaseObject;

/**
 * Enumerated wrapper class for Framework Log event types.  The types supported are
 * <i><il>
 * INFO
 * DEBUG
 * WARNING
 * ERROR
 * </il></i>
 * <p> 
 * All support instances are contained as static final attributes.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkLogEventType extends FrameworkBaseObject
{
    
// constructors    

    /**
     * default constructor - deter external and internal instantiation
     */
    private FrameworkLogEventType()
    {        
    }
    
    /**
     * constructor - allow only internal instantiation
     *
     * @param       int     eventType
     */
    private FrameworkLogEventType( int eventType )
    {
        logEventType = eventType;
    }
    
    /**
     * Factory method use to handle returning the correct type
     * of FrameworkLogEventType.
     *
     * @param		eventType		
     * @return		log event type
     */
    static public FrameworkLogEventType getFrameworkLogEventType( int eventType )
    throws IllegalArgumentException
    {
        FrameworkLogEventType type = null;
        
        if ( eventType == INFO )
        {
            type = INFO_LOG_EVENT_TYPE;
        }
        else if ( eventType == WARNING )
        {
            type = WARNING_LOG_EVENT_TYPE;            
        }
        else if ( eventType == ERROR )
        {
            type = ERROR_LOG_EVENT_TYPE;
        }
        else if ( eventType == DEBUG )
        {
            type = DEBUG_LOG_EVENT_TYPE;
        }        
        else
            throw new IllegalArgumentException( "FrameworkLogEventType:getFrameworkLogEventType() - invalid argument" );
            
        return( type );            
    }
    
    /**
     * returns true/false for being an INFO
     *
     * @return      true/false indicator
     */
    public boolean isInfoLogEventType()
    {
        boolean bIs = false;
        
        if ( logEventType == INFO )
        {
            bIs = true;
        }
    
        return( bIs );
    }    

    /**
     * returns true/false for being a WARNING
     *
     * @return      true/false indicator
     */    
    public boolean isWarningLogEventType()
    {
        boolean bIs = false;
        
        if ( logEventType == WARNING )
        {
            bIs = true;
        }
    
        return( bIs );        
    }
    
    /**
     * returns true/false for being an ERROR
     *
     * @return      true/false indicator
     */    
    public boolean isErrorLogEventType()
    {
        boolean bIs = false;
        
        if ( logEventType == ERROR )
        {
            bIs = true;
        }
    
        return( bIs );        
    }

    /**
     * returns true/false for being a DEBUG
     *
     * @return      true/false indicator
     */    
    public boolean isDebugLogEventType()
    {
        boolean bIs = false;
        
        if ( logEventType == DEBUG )
        {
            bIs = true;
        }
    
        return( bIs );        
    }
    
// helper methods

    /**
     * toString() overload from java.lang.Object
     *
     * @return      String representation of this instance
     */
    public String toString()
    {
        StringBuffer sBuf = new StringBuffer( "" );
        
        if ( isInfoLogEventType() )
        {
            sBuf.append( "INFO" );
        }
        else if ( isWarningLogEventType() )
        {
            sBuf.append( "WARNING" );
        }
        else if ( isErrorLogEventType() )
        {
            sBuf.append( "ERROR" );
        }
        else if ( isDebugLogEventType() )
        {
            sBuf.append( "DEBUG" );
        }
        
        return( sBuf.toString() );
    }
    
// attributes

    /**
     * the purpose of this wrapper class
     */
    protected int logEventType        =   INFO;
    
    /**
     * debug log event type
     */
    static protected final int DEBUG    = 0;    
	/**
	 * info log event type
	 */     
    static protected final int INFO     = 1;
	/**
	 * waring log event type
	 */    
    static protected final int WARNING  = 2;
	/**
	 * error log event type
	 */    
    static protected final int ERROR    = 3;
    
    /**
     * debug type instance of this class
     */
    static public final FrameworkLogEventType DEBUG_LOG_EVENT_TYPE    = new FrameworkLogEventType( DEBUG );
	/**
	 * info type instance of this class
	 */         
    static public final FrameworkLogEventType INFO_LOG_EVENT_TYPE     = new FrameworkLogEventType( INFO );    
	/**
	 * warning type instance of this class
	 */    
    static public final FrameworkLogEventType WARNING_LOG_EVENT_TYPE  = new FrameworkLogEventType( WARNING );
	/**
	 * error type instance of this class
	 */    
    static public final FrameworkLogEventType ERROR_LOG_EVENT_TYPE    = new FrameworkLogEventType( ERROR );
    
}

/*
 * Change Log:
 * $Log: FrameworkLogEventType.java,v $
 */

