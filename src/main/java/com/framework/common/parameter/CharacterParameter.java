/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.parameter;

//************************************
// Imports
//************************************

/**
 * Maps a Character (character) to a java.sql.Types.CHAR
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class CharacterParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * constructor used to create a StringParamter
     *
     * @param   c
     * @param   isInParameter    in or out param indicator
     */
    public CharacterParameter( Character c, boolean isInParameter )
    throws IllegalArgumentException
    {    	
    	if ( c == null )
    		throw new IllegalArgumentException( "" );    		
            		
        setParameter( c.toString() );
        this.isInParameter  = isInParameter;
        type = java.sql.Types.CHAR;
    }

    /**
     * constructor used to create a StringParamter
     *
     * @param    c
     * @param   isInParameter    in or out param indicator
     */
    public CharacterParameter( char c, boolean isInParameter )
    {
        this( new Character(c), isInParameter );
    }
}

/*
 * Change Log:
 * $Log: CharacterParameter.java,v $
 */




