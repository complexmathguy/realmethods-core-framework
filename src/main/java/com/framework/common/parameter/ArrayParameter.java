/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.Collection;

import com.framework.common.exception.ObjectCreationException;

/**
 * Used to map a Collection to a byte []
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class ArrayParameter extends Parameter
{
//****************************************************
// Public Methods
//****************************************************
   /**
    * @param   	value
    * @param   	isInParameter		in/out indicator
    * @exception	ObjectCreationException
    */
    public ArrayParameter( Collection value, boolean isInParameter )
    throws ObjectCreationException
    {
        super( value, java.sql.Types.VARBINARY, isInParameter );
        
        // manulate the under Collection value into a binary array
                    
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream (baos);
            oos.writeObject(value);
            byte[] newByteArray = baos.toByteArray();
            
            // re-assign the underlying parameter
            setParameter( newByteArray );
        }
        catch( IOException exc )
        {
            throw new ObjectCreationException( "ArrayParameter(Collection,boolean) - " + exc, exc );
        }
    }

}

/*
 * Change Log:
 * $Log: ArrayParameter.java,v $
 */
