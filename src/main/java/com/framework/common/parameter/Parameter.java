/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.parameter;

import com.framework.common.FrameworkBaseObject;

/**
 * Maps a certain Java types to its corresponding java.sql Type. Can be used by itself when
 * the Java type is unknown, but the correct sub-class should be used instead.
 * <p>
 * @author    realMethods, Inc.
 */
public class Parameter extends FrameworkBaseObject
{
//****************************************************
// Public Methods
//****************************************************

	/**
	 * Constructor - default
	 */
	public Parameter()
	{}
	
    /**
     * Constructor
     * @param object			represents pararmeter data
     * @param isInParameter		in/out indicator
     */
    public Parameter( Object object, boolean isInParameter )
    {
        parameter     = object;
        this.isInParameter  = isInParameter;
        type = getSQLType( object );
    }

    /**
     * Constructor
     * <p>
     * Note: the type parameter must be a value from java.sql.Types
     * <p>
     * @param object			represents pararmeter data
     * @param type				java.sql.Types value for the input object
     * @param isInParameter		in/out indicator
     */
    public Parameter( Object object, int type, boolean isInParameter )
    {
        this.parameter			= object;
        this.type 				= type;
        this.isInParameter  	= isInParameter;        
    }

// accessor methods

    /**
     * Retrieves the underlying parameter
     * @return The underlying parameter
     */
    final public Object getParameter()
    {
        return( parameter ); 
    }

    /**
     * Retrieves the underlying SQL type for the parameter.
     * @return The underlying SQL type
     */
    final public int getType()
    {
        return( type ); 
    }   

// helper methods
    /**
     * Retrieves the the SQL type of the type of object passed in
     * @param o The object to determine what type.
     * @return The SQL type.
     */
    final protected int getSQLType( Object o )
    {
        if ( o instanceof java.lang.String )
            return( java.sql.Types.LONGVARCHAR );
        else if ( o instanceof java.lang.Long )
            return( java.sql.Types.BIGINT );
        else if ( o instanceof java.math.BigDecimal )
            return( java.sql.Types.NUMERIC );
        else if ( o instanceof java.lang.Boolean )
            return( java.sql.Types.BIT );
        else if ( o instanceof java.lang.Integer )
            return( java.sql.Types.INTEGER );
        else if ( o instanceof java.lang.Float )
            return( java.sql.Types.REAL );
        else if ( o instanceof java.lang.Double )
            return( java.sql.Types.DOUBLE );
        else if ( o instanceof byte[] )
            return( java.sql.Types.LONGVARBINARY );
        else if ( o instanceof java.sql.Date )
            return( java.sql.Types.DATE );
        else if ( o instanceof java.sql.Time )
            return( java.sql.Types.TIME );
        else if ( o instanceof java.sql.Timestamp )
            return( java.sql.Types.TIMESTAMP );
        else if ( o instanceof java.util.Date )
            return( java.sql.Types.DATE );
        else if ( o instanceof java.lang.Character )
            return( java.sql.Types.CHAR );
        else if ( o instanceof java.util.Collection )
            return( java.sql.Types.VARCHAR ); 
        else if ( o == null )
            return( java.sql.Types.NULL );
        else if ( o instanceof java.util.Calendar )
            return( java.sql.Types.DATE );            
        else
            return( java.sql.Types.OTHER );
    }

    /**
     * Is the parameter an IN paramater
     * <p>
     * @return	boolean
     */
    final public boolean isInParameter()
    {
        return( isInParameter ? true : false ); 
    }

    /**
     * Is the parameter an out parameter.
     * <p>
     * @return 	boolean
     */
    final public boolean isOutParameter()
    {
        return( !isInParameter() ); 
    }

    /**
     * Assigns the underlying Object
     * <p>
     * @param	object
     */
    protected void setParameter( Object object )
    {
        parameter = object;
    }
    
    /**
     * String representation.
     * <p>
     * @return	formatted String
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer( "\nIn DB Parameter: ");
        
        buffer.append( String.valueOf( isInParameter ) );
        buffer.append( ", Parameter: " );
        buffer.append( parameter );
        buffer.append( ", java.sql.Type: " );
        buffer.append( String.valueOf( type ) );
		      
        return( buffer.toString() );                                    
    }
    
    // attributes
    /**
     * Flag that indicates whether the parameter is an IN parameter.
     */
    protected boolean isInParameter  = true;
    /** 
     * The underlying object of the parameter.
     */
    protected Object parameter     	= null;
    /**
     * The underlying SQL type associated with the parameter.
     */
    protected int type 				= java.sql.Types.OTHER;
    /**
     * Public static constant variable for an IN parameter.
     */
    final static public boolean IN  	= true;
    /**
     * Public static constant variable for an OUT parameter.
     */
    final static public boolean OUT	= false;
}

/*
 * Change Log:
 * $Log: Parameter.java,v $
 */
