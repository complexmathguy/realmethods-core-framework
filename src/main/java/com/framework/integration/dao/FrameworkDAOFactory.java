/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.dao;


/** 
 * Factory for all Framework related DAO objects.
 * <p> 
 * @author    realMethods, Inc.
 */

public class FrameworkDAOFactory 
    extends com.framework.common.FrameworkBaseObjectFactory
{

// constructors

    /**
     * default constructor - deter direct instantation.
     * Use the factory method defined below.
     *
     */     
    protected FrameworkDAOFactory()
    {
        super( false /* caching turned off */ );
    }
    
// helper methods

    /**
     * factory method
     * <p>
     * @return    	FrameworkDAOFactory
     */
    static public FrameworkDAOFactory getInstance()
    {
        if ( singleton == null )
        {
        	singleton = new FrameworkDAOFactory();
        }    
        return( singleton );            
    }
    
 
    /**
     * Returns the Class representation of the named FrameworkDatabaseDAO class
     * <p>
     * @param       frameworkDAOClassName
     * @return      Class
     */
    public Class getFrameworkDAOAsClass( String frameworkDAOClassName )
    {
        return( getClass( frameworkDAOClassName ) );
    }

    /**
     * Returns the IFrameworkDAO representation of the 
     * named FrameworkDatabaseDAO class.
     * <p>
     * @param       frameworkDAOClassName
     * @return      IFrameworkDAO
     */
    public IFrameworkDAO getFrameworkDAO( String frameworkDAOClassName )
    {
        return( (IFrameworkDAO)getObject( frameworkDAOClassName ) );        
    }


    /**
     * Release the DAO back to the Factory.
     * <p>
     * @param	dao
     */
    public void releaseFrameworkDAO( IFrameworkDAO dao )
    {
        if ( dao != null )
        {
            dao.release();
            
            // delegate to the base class
            release( dao );
        }
    }
    
    
// attributes

    /**
     * factory pattern using as singleton
     */
    static protected FrameworkDAOFactory singleton   = null;
        
}

/*
 * Change Log:
 * $Log: FrameworkDAOFactory.java,v $
 * Revision 1.3  2003/10/08 02:10:28  tylertravis
 * no message
 *
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:09  tylertravis
 * initial sourceforge cvs revision
 *
 */
