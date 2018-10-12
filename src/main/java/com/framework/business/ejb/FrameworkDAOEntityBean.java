/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//************************************
// Imports
//************************************

import javax.ejb.RemoveException;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.exception.FrameworkEJBCreateException;
import com.framework.common.exception.FrameworkEJBException;
import com.framework.common.exception.FrameworkEJBFinderException;
import com.framework.common.exception.FrameworkEJBRemoveException;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.VersionUpdateException;

import com.framework.integration.dao.FrameworkDAOFactory;
import com.framework.integration.dao.IFrameworkDAO;

/**
 * Extend this class when defining a bean-managed entity bean requiring data access.
 * <p> 
 * @author	  realMethods, Inc.
 * @see		  com.framework.integration.dao.FrameworkDAO
 */
abstract public class FrameworkDAOEntityBean extends FrameworkEntityBean
{
//****************************************************
// Public Methods
//****************************************************

//*************************************
// ejb related helper methods
//*************************************

    /**
     * Creates the underlying bean entity.
     * <p>
     * This method will create/insert the corresponding 
     * bean in the database based on the provided value object
     * <p>
     * @param       vo						target to persist
     * @return      FrameworkPrimaryKey		new identity of the target
     * @exception   FrameworkEJBCreateException
     */
    protected FrameworkPrimaryKey create( IFrameworkValueObject vo )
    throws FrameworkEJBCreateException
    {        
        try
        {
            valueObject = getDAO().create( vo );
            
            // just in case it's needed later
            setValueObject( valueObject );
        }
        catch( Exception exc )
        {
            throw new FrameworkEJBCreateException( "FrameworkDAOEntityBean:ejbCreate( IFrameworkValueObject ) - " + exc, exc );
        }
        finally
        {
            releaseFrameworkDAO();
        }
        
        return( valueObject.getFrameworkPrimaryKey() );
    }

    /**
     * Retrieves the value object corresponding to the primary key by using the
     * DAO provided by the sub-class.
     * <p>
     * @param       pk
     * @exception   FrameworkEJBFinderException
     */
    protected void findByPrimaryKeyDelegate( FrameworkPrimaryKey pk ) 
    throws FrameworkEJBFinderException
    {
        try
        {
            // keep the returned IFrameworkValueObject just in case it's needed later
            setValueObject( getDAO().find( pk ) );
        }
        catch( Exception exc )
        {
            throw new FrameworkEJBFinderException( "FrameworkDAOEntityBean:ejbFindByPrimaryKeyDelegate(pk) - " + exc, exc );    
        }
        finally
        {
            releaseFrameworkDAO();
        }
        
    }         

    /**
     * Using the primary key from this entity context, locates the corresponding value object.
     * <p>
     * Uses the DAO provided by the sub-class implementation of getDAO() to accomplish the loading.
	 * <p>
     * @exception FrameworkEJBException
     */
    public void ejbLoad() 
    throws FrameworkEJBException
    {
        try
        {            
            // get the primary key from the EntityContext
            setValueObject( getDAO().find( (FrameworkPrimaryKey)this.getEntityContext().getPrimaryKey() ) );
        }
        catch ( Exception e )
        {
            throw new FrameworkEJBException( "FrameworkDAOEntityBean:ejbLoad() - " + e, e);
        }
        finally
        {
            releaseFrameworkDAO();
        }
        
    }

    /**
     * Uses the DAO provided by the sub-class implementation of getDAO() to accomplish the storing
     * of the contained value object.
     * <p>
     * @exception FrameworkEJBException
     */
    public void ejbStore()
    throws FrameworkEJBException
    {
        try
        {
            setValueObject( getDAO().save( getValueObject() ) );
        }
        catch( VersionUpdateException exc )
        {
            throw new FrameworkEJBException( "FrameworkDAOEntityBean:ejbStore() - " + exc, exc );            
        }
        catch( Exception exc )
        {
            throw new FrameworkEJBException( "FrameworkDAOEntityBean:ejbStore() - " + exc, exc );
        }
        finally
        {
            releaseFrameworkDAO();
        }
        
    }

    /**
    * Uses the DAO provided by the sub-class implementation of getDAO() to delete the data that
    * corresponds to the primarykey obtained from this entity's context.
    * <p>
    * @exception RemoveException
    */
    public void ejbRemove() 
    throws RemoveException
    {
        try
        {
            // Retrieve the primary key
            FrameworkPrimaryKey pk    = (FrameworkPrimaryKey)this.getEntityContext().getPrimaryKey();
            getDAO().delete( pk );
        }
        catch( Throwable exc )
        {
            throw new FrameworkEJBRemoveException( "FrameworkDAOEntityBean:ejbRemove() - " + exc, exc );
        }
        finally
        {
            releaseFrameworkDAO();
        }
        
    }

    /**
     * Container notification that this instance is done being used.
     * <p>
     * Releases the DAO back to its factory.
     * <p>
     * @exception	FrameworkEJBException
     */
    public void ejbPassivate() 
    throws FrameworkEJBException
    {        
        // release the DAO(s) back to the Factory
        releaseFrameworkDAO();
        
        super.ejbPassivate();
    };
    
//****************************************************
// protetected / private methods
//****************************************************

    /**
     * Helper method for subclasses to obtain the FrameworkDatabaseDAOFactory
     *
     * @return    	FrameworkDAOFactory 
     * @exception	ObjectCreationException
     */
    protected FrameworkDAOFactory getDAOFactory()
    throws ObjectCreationException
    {
        return( FrameworkDAOFactory.getInstance() );
    }
    
    /**
     * releases the FrameworkDAO
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
        		logErrorMessage( "FrameworkDAOEntityBean.releaseFrameworkDAO() - failed to get instance of FrameworkDAOFactory - " + exc );
        	}
        	finally
        	{
        		internalFrameworkDAO = null;
        	}
        }
    }
        
    /**
     * Returns the IFrameworkDAO.
     *
     * @return      IFrameworkDAO
     */
    public IFrameworkDAO getInternalFrameworkDAO()
    {
        return( internalFrameworkDAO );
    }

    /**
     * Assigns a IFrameworkDAO
     * @param     dao
     */
    public void setInternalFrameworkDAO( IFrameworkDAO dao )
    {
        internalFrameworkDAO = dao;
    }
        
// must implements

    /**
     * Subclass must implement this in order to get the correct
     * DAO object to redirect persistence operations to.
     * <p>
     * @return      IFrameworkDAO
     */
    abstract protected IFrameworkDAO getDAO();
    
//****************************************************
// Attributes
//****************************************************\
    
    /**
     * DAO in use
     */
    protected transient IFrameworkDAO internalFrameworkDAO      = null;
}

/*
 * Change Log:
 * $Log: FrameworkDAOEntityBean.java,v $
 */
