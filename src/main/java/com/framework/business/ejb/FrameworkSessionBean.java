/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//************************************
// Imports
//************************************
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.SessionContext;

import com.framework.business.pk.IFrameworkPrimaryKey;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.StoredProcedureException;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.db.IDatabaseQuerier;
import com.framework.integration.objectpool.db.ResultsetToStringUnloader;

/**
 * Base class of all Framework session beans.
 * <p> 
 * @author    realMethods, Inc.
 */
public abstract class FrameworkSessionBean extends FrameworkEJB
    implements javax.ejb.SessionBean 
{
//****************************************************
// Public Methods
//****************************************************

// FrameworkEJB implementations

   /**
	* Container notification that is instance is about to be put to use. 
	*/
   public void ejbActivate() 
   {           
   }

   /**
	* Container notification that this instance is done being used.
	*/
   	public void ejbPassivate() 
   	{
   	}
   
   /**
	* Container notification to remove this bean.
    */
    public void ejbRemove() 
   	{
	}
      
	/**
	 */
	public void ejbCreate () 
	{
	}
	      
    /**
     * Sets the session context.
     * @param 	sessionContext 
     */
    public void setSessionContext(SessionContext sessionContext) 
    {
        this.sessionContext = sessionContext;
    }


    /**
     * returns the home interface assigned session context.
     * 
     * @return		SessionContext
     */
    public SessionContext getSessionContext()
    {
        return( sessionContext ); 
    }


    /** 
     * Returns whether the EJBean has been modified or not.
     * 
     * @return Boolean
     */
    public Boolean isModified ()
    {
        return modified;
    }

//****************************************************
// Protected Methods
//****************************************************


//****************************************************
// Protected Methods
//****************************************************

    /** 
     * Sets the bean as modified or not.  No longer required.
     * <p>
     * @param	isDirty
     */
    protected void setModified( boolean isDirty )
    {
        modified = new Boolean( isDirty );
    }


    /**
     * Overloadable method which returns the default database connection.
     * This method is called to retrieve a connection in which to execute SQL
     * <p>
     * @return      IDatabaseQuerier
     * @exception   ConnectionAccessException		
     */
    protected IDatabaseQuerier getConnection()
    throws ConnectionAcquisitionException
    { 
        return( getMainApplicationConnection() ); 
    }

    /**
     * Returns the defaultdatabase connection
     * <p>
     * @return 		IDatabaseQuerier
     * @exception 	ConnectionAcquisitionException
     */
    protected IDatabaseQuerier getMainApplicationConnection()
    throws ConnectionAcquisitionException       
    { 
    	IDatabaseQuerier dbquerier  = null;
    	
        try
        {
        	dbquerier = (IDatabaseQuerier)ConnectionPoolManagerFactory.getObject().getConnection( EJBUtility.getMainApplicationConnectionID() ); 
        }
        catch( ObjectCreationException exc )        
        {
			throw new ConnectionAcquisitionException( "FrameworkSessionBean.getMainApplicationConnection() - failed to get the Connection Pool Manager from its factory - " + exc, exc );        	
        }
        catch( ConnectionAcquisitionException exc1 )
        {
        	throw new ConnectionAcquisitionException( "FrameworkSessionBean.getMainApplicationConnection() - failed to acquire the main database connection - " + exc1, exc1 );
        }
        
        return( dbquerier );
    }
    
    /**
    * Returns a Collection of two ArrayLists: First list is a list
    * containing the column names of the Resultset.  The second list
    * is an ArrayList of Hashmaps.  The key into the Hashmap is the
    * field name and the value is a String representation of the data.
    * It uses the getConnection() to execute the stored procedure with.
    * <p>
    * @param 		storedProcedureName 		name of the stored procedure to execute
    * @param 		key 						can be null
    * @return 		Collection					two ArrayLists,  1st is column names, 2nd is Hashmaps 
    * @exception 	IllegalArgumentException 	if the stored proc. name is null or empty
    * @exception 	StoredProcedureException
    * @see			com.framework.integration.objectpool.db.ResultsetToStringUnloader
    */
    protected Collection getResultsetAsStrings( String storedProcedureName, IFrameworkPrimaryKey key )
    throws IllegalArgumentException, StoredProcedureException
    {
    	Collection resultsetCollection = null;
    	
        // Validate Parameters
        if ( storedProcedureName == null || storedProcedureName.length() == 0 )
        {
            throw new IllegalArgumentException("FrameworkSessionBean::getResultsetAsStrings(..) - storedProcedureNameIn cannot be null or empty.");    
        }

        try
        {
   		    // Now create the ResultsetToStringUnloader
		    // This class will be the callback mechanism with the Resultset to create
		    // the collection of Collections that contain the field names along
		    // with a Hashmap of field names to values for each record
		    ResultsetToStringUnloader tempUnloader = new ResultsetToStringUnloader();
		    Collection tempParams = null;
		    
		    if (key != null)
		    {
				tempParams = key.valuesAsParameters();   
		    }
			// Execute Stored Procedure
			resultsetCollection = getConnection().executeStoredProcedure( storedProcedureName, tempParams, tempUnloader);
		}
		catch (Throwable exc)
		{
		    throw new StoredProcedureException("FrameworkSessionBean::getResultsetAsStrings(..) - Could not create the return collection due to : " + exc );  
		}

        //----------------------------------------
        // Now Create the List Proxy
        //----------------------------------------
		ArrayList returnList = new ArrayList();        
        
        if ( resultsetCollection != null )
        {        
        	Iterator tempIterator = resultsetCollection.iterator();
        
        	returnList.add( (Collection)tempIterator.next() ); 
        	returnList.add( (Collection)tempIterator.next() );
        }
                        
        return returnList;
    }
    
    
//****************************************************
// Attributes
//****************************************************
    
    /**
     * dirty flag
     */
    private Boolean		modified = new Boolean( false );
    
    /**
     * the context for the containers session
     */
    private SessionContext    		sessionContext = null;
        
}

/*
 * Change Log:
 * $Log: FrameworkSessionBean.java,v $
 */
