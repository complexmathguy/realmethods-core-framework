/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//************************************
// Imports
//************************************
import com.framework.common.exception.ObjectCreationException;

import com.framework.integration.dao.FrameworkDAOFactory;
import com.framework.integration.dao.IFrameworkDAO;

/**
 * Extend this abstract class when defining a session bean class that makes use
 * of a corresponding FrameworkDatabaseDAO.
 * <p> 
 * @author    realMethods, Inc.
 */
public abstract class FrameworkDAOSessionBean extends FrameworkSessionBean
    implements javax.ejb.SessionBean 
{
//****************************************************
// Public Methods
//****************************************************

    /**
     * EJB container indicator that this instance is about to be put to use.
     * <p>
     * Release the DAO(s) used during this session.
     */
    public void ejbPassivate() 
    {
        releaseFrameworkDAO();
            
        super.ejbPassivate();        
    }
    
    /**
     * Releases the previously assigned IFrameworkDAO back to the FrameworkDAOFactory.
     * <p>
     * Overload this method is the sub-class utilizes a different IFrameworkDAO instance, or
     * perhaps more than one.
     */
    protected void releaseFrameworkDAO()
    {        
        // release the DAO back to the Factory
        if ( internalFrameworkDAO != null )
        {
        	try
        	{
            	FrameworkDAOFactory.getInstance().releaseFrameworkDAO( internalFrameworkDAO );
        	}
        	catch( Throwable exc )
        	{
        		logErrorMessage( "FrameworkDAOSessionBean.releaseFrameworkDAO() - failed to get instance of FrameworkDAOFActory in order to release the contained FrameworkDAO - " + exc );
        	}
        	finally
        	{
            	internalFrameworkDAO = null;
        	}
        }
    }

        
    /**
     * helper method for subclasses to obtain the FrameworkDatabaseDAOFactory
     *
     * @return   	FrameworkDAOFactory 
     * @exception	ObjectCreationException
     */
    protected FrameworkDAOFactory getDAOFactory()
    throws ObjectCreationException
    {
        return( FrameworkDAOFactory.getInstance() );
    }
    
    /**
     * Returns the previously assigned IFrameworkDAO.
     *
     * @return      IFrameworkDAO
     */
    public IFrameworkDAO getInternalFrameworkDAO()
    {
        return( internalFrameworkDAO );
    }

    /**
     * Assigns a IFrameworkDAO
     *
     * @param       dao
     */
    public void setInternalFrameworkDAO( IFrameworkDAO dao )
    {
        internalFrameworkDAO = dao;
    }
        
//****************************************************
// Attributes
//****************************************************

    /**
     * internal DAO in use
     */
    protected transient IFrameworkDAO internalFrameworkDAO      = null;
            
}

/*
 * Change Log:
 * $Log: FrameworkDAOSessionBean.java,v $
 */
