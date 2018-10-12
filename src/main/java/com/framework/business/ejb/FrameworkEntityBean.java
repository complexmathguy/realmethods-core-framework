/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//***********************************
// Imports
//***********************************
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.framework.business.bo.IFrameworkBusinessObject;
import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.exception.FrameworkEJBException;

/**
 * Base class for all Framework related entity beans
 * <p> 
 * @author    realMethods, Inc.
 */
abstract public class FrameworkEntityBean extends FrameworkEJB
    implements javax.ejb.EntityBean 
{
//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     *
     */
    public FrameworkEntityBean()
    {    	
        super();        
    }   

    /**
     * Retrieves the the identityHashCode for this object.
     * @return Hashcode for this object.
     */
    public String id() 
    {
        StringBuffer id = new StringBuffer( System.identityHashCode(this) );
        
        try
        {
        	id.append( ", PK = " );
        	
        	if ( entityContext != null )
        	{
        		if ( entityContext.getPrimaryKey() != null )
        			id.append( entityContext.getPrimaryKey().toString() );
				else
					id.append( "-unassigned-");
        	}
        	else					  			
        		id.append( "null entityContext");
        }	 
        catch( Exception exc )
        {
        	id.append( "-unassigned-");
        }                   
        
        return( id.toString() );

    }
    
    /**
     * Container notification that is instance is about to be put to use. 
     * @exception	FrameworkEJBException
     */
    public void ejbActivate() 
    throws FrameworkEJBException
    {
        setModified( true );            
    }

	/**
	 * Container notification that this instance is done being used.
	 * <p>
	 * Assigns null to the contained IFrameworkValueObject.
	 * <p>
	 * @exception	FrameworkEJBException
	 */
	public void ejbPassivate() 
	throws FrameworkEJBException
	{
		setValueObject( null );
	}

	/**
	 * Refreshes the EJBean from the persistent storage.
	 * @exception   FrameworkEJBException
	 */
	public void ejbLoad()
	throws FrameworkEJBException
	{
	}

	/**
	 * Stores the EJBean in the persistent storage.
	 * @exception               FrameworkEJBException
	 */
	public void ejbStore()
	throws FrameworkEJBException
	{
	}   

	/**
	 * Deletes the EJBean from the persistent storage. 
	 * @exception             RemoveException
	 */
	public void ejbRemove() 
	throws RemoveException
	{	  	
	}
	
    /**
     * Sets the EntityContext for the Entity Bean.
     * @param 		entityContext       
     * @exception	EJBException
     */
    public void setEntityContext( EntityContext entityContext )
    throws EJBException
    {
       this.entityContext = entityContext;
    }

    /**
     * Unsets the EntityContext for the Entity Bean.
     * @exception   	EJBException
     */
    public void unsetEntityContext() 
    throws EJBException
    {
        this.entityContext = null;
    }   


    /**
     * Returns the home interface assigned entity context.
     * @return EntityContext associated with the bean.
     */ 
    public EntityContext getEntityContext()
    {
        return( entityContext ); 
    }

    /** 
     * Returns whether the EJBean has been modified or not. 
     * @return  modified or not 
     */
    public Boolean isModified ()
    {
        return modified;
    }

    /**
     * Retrieves the a value object representation of the entity bean.
     * @return		IFrameworkValueObject
     */
    public IFrameworkValueObject getValueObject()
    {
		return( valueObject );
    }
    
    /**
     * Sets the a value object representation of the entity bean.
     * @param        vo				
     */
    public void setValueObject( IFrameworkValueObject vo )
    {
		valueObject = vo;
		setModified( true );
    }

	/**
	 * Retrieves the a business object representation of the entity bean.
	 * @return		IFrameworkBusinessObject
	 */
	public IFrameworkBusinessObject getBusinessObject()
	{
		return( (IFrameworkBusinessObject)getValueObject() );
	}
    
	/**
	 * Sets the a business object representation of the entity bean.
	 * @param        bo
	 */
	public void setBusinessObject( IFrameworkBusinessObject bo )
	{
		setValueObject( bo );
	}


//************************************************************************    
// Private / Protected Methods
//************************************************************************

    /** 
     * Sets the entity bean as modified.
     * @param	isDirty
     */
    protected void setModified( boolean isDirty )
    {
        modified = new Boolean( isDirty );
    }
   

//************************************************************************    
// Attributes
//************************************************************************
    
    /**
     * dirty flag.
     */
    private Boolean modified        = new Boolean( false );    
    
    /**
     * the Entity Context for the bean.
     */
    protected EntityContext entityContext;
    
    /**
     * value object of interest
     */
    protected IFrameworkValueObject valueObject = null;
    

}

/*
 * Change Log:
 * $Log: FrameworkEntityBean.java,v $
 */
