/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.dao;

import java.sql.ResultSet;

import java.util.*;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.DatabaseConnectionFailureException;
import com.framework.common.exception.ExecuteStatementException;
import com.framework.common.exception.FrameworkDAOException;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.ResultSetCallbackException;
import com.framework.common.exception.SelectStatementException;
import com.framework.common.exception.StoredProcedureException;
import com.framework.common.exception.VersionUpdateException;

import com.framework.common.misc.Utility;

import com.framework.common.parameter.LongParameter;
import com.framework.common.parameter.Parameter;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.db.DatabaseQuerier;
import com.framework.integration.objectpool.db.IDatabaseQuerier;
import com.framework.integration.objectpool.db.IResultSetCallback;

/**
 * class FrameworkDatabaseDAO
 * <p>
 * Extend this abstract class for direct database related 
 * business DAO implementations.  
 * <p>
 * This class makes use of stored procedures for creating, finding, 
 * storing and deleting.  Overload these methods if you wish to supply
 * query and query data in a different manner.
 * <p>
 * This class assumes a primary key of IFrameworkPrimaryKey type.
 * Review and overload method getFrameworkPrimaryKeyShell() if your class 
 * requires a primary key other than type Long.
 * <p>
 * Please observer the following stored procedure assumptions:
 * 1. <b>create</b> (insert) - input parameters: all model related data in order as discovered
 * using reflection.  output parameters: primary key related attribute(s)
 * <p> 
 * 2. <b>save</b> (update )- input parameters: all model related data
 * output parameters: none
 * <p>
 * 3. <b>find</b> (load) - input paramters: primary key related attribute(s)
 * output parameters: all business model related data in order.
 * <p> 
 * 4. <b>delete</b> (delete)- input parameters: primary key related attribute(s)
 * output parameters: none
 * <p> 
 * @author		realMethods, Inc.
 */
abstract public class FrameworkDatabaseDAO extends FrameworkDAO
    implements IResultSetCallback, IFrameworkDatabaseDAO
{
    
// constructors

	public FrameworkDatabaseDAO()
	{
		super();
	}
	
//  IFrameworkDatabaseDAO implementation

    /**
     * Returns the default application database connection
     * @return    	DatabaseQuerier      
     * @exception	DatabaseConnectionFailureException
     */
    public IDatabaseQuerier getMainApplicationConnection()
    throws DatabaseConnectionFailureException       
    { 
    	IDatabaseQuerier db = null;
    	
    	try
    	{
    		db = (IDatabaseQuerier)ConnectionPoolManagerFactory.getObject().getConnection( Utility.getMainApplicationConnectionID() );
    	}
    	catch( Throwable exc )
    	{
    		throw new DatabaseConnectionFailureException( "FrameworkDatabaseDAO:getMainApplicationConnection() - " + exc, exc );
    	}
    	
        return db;
    }

    /**
     * Creates an associated value object.
     * <p>
     * This method will create/insert the corresponding 
     * bean in the database based on the provided model
     *
     * @param       vo			the source to create within the persistent store
     * @return      			the result of the creation	
     * @exception   FrameworkDAOException
     */
    public IFrameworkValueObject create( IFrameworkValueObject vo )
    throws FrameworkDAOException
    {
        if ( vo == null )
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:create(IFrameworkValueObject) - null valueObject not allowed." );

        setValueObject( vo );
        
        // acquire the database args...
        Collection v = null;
        
        try
        {
            // get the database arguments from the Value Object
            v = getDatabaseArgs( false /* storing */ ); 
            
            // delegate internally
            FrameworkPrimaryKey key = this.create( v );
            
            // assign the primary key to the provided model
            vo.setFrameworkPrimaryKey( key );
            
            // let the base class do its thing
            super.create( vo );
            
        }
        catch( Exception exc )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:create(IFrameworkValueObject) - " + exc, exc );
        }   
    	     		    	     
        // return the value object
        return( valueObject );
    }
    
    /**
     * Attempts to retrieve from the local cache, if enabled.  If not there, then
     * retrieves the value object from the database, using the provided primary key. 
     * If no match is found, a null IFrameworkValueObject is returned.
     * <p>
     * @param       pk  	identifier of entity to locate from the persistent store   
     * @return      IFrameworkValueObject
     * @exception   FrameworkDAOException
     */
    public IFrameworkValueObject find( FrameworkPrimaryKey pk ) 
    throws FrameworkDAOException
    {    	
        // If the primary key is null or it's contents are
        if ( (pk == null) || (pk.values() == null) )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:find(ISimplePrimaryKey) - null primary key not allowed.");
        }

        IFrameworkValueObject tmpValueObject = super.find( pk );
        
        if ( tmpValueObject == null )	// not in the local cache
        {        
	        try
	        {
	            Collection valueObjects   = executeSQL( getLoadSQL(), 
	                                                    pk.valuesAsParameters(), 
	                                                    true /* select related SQL statement */ );
	
	            if ( valueObjects != null && valueObjects.size() > 0 )
	            {
	                tmpValueObject = (IFrameworkValueObject)valueObjects.iterator().next();                
	            }            
	            
	            setValueObject( tmpValueObject );
	        }
	        catch ( Exception exc )
	        {
	            throw new FrameworkDAOException( "FrameworkDatabaseDAO:find(IFrameworkPrimaryKey) - " + exc.getMessage(), exc );
	        }
	        finally
	        {
	            if ( autoReleaseConnection() )
	                release();
	        }
	        
	        // apply to the local cache
	        cache( tmpValueObject );
        }
        return( tmpValueObject );                
    }


    /**
     * Stores the provided value object to the database.
     *
     * @param       vo		source of data to store
     * @return		the results of saving to the persistent store
     * @exception   FrameworkDAOException
     * @exception   VersionUpdateException	thrown if vo suppoorts versioning and what is persisted isn't this version
     */
    public IFrameworkValueObject save( IFrameworkValueObject vo )
    throws FrameworkDAOException, VersionUpdateException
    {

        if ( vo == null )
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:save(IFrameworkValueObject) - null valueObject not allowed." );

        // If versioning is turned, check to see that versionIDs are the
        // same before updating
        if ( vo.isVersioned() )
        {
            IFrameworkValueObject tempVO = find( vo.getFrameworkPrimaryKey() );

            if ( tempVO == null )
            {
                throw new FrameworkDAOException( "FrameworkDatabaseDAO:save( IFrameworkValueObject ) - unable to update since it doesn't exist in storage : " + valueObject );
            }

            // if no match on the versionIDs, throw a VersionUpdateException
            if ( vo.getVersionID().longValue() != tempVO.getVersionID().longValue() )
            {
                throw new VersionUpdateException( "FrameworkDatabaseDAO:save( IFrameworkValueObject ) - version ID mismatch : Storing version ID: " + valueObject.getVersionID().toString() + " in place of version ID " + tempVO.getVersionID().toString() );
            }
        }
        
        setValueObject( vo );
        
//        IDatabaseQuerier dbQuerier = null;
        String s = null;
        
        try
        {
            s = getUpdateSQL();
            
            ArrayList args =  new ArrayList(getDatabaseArgs( false /*storing to take place, not loading*/ ));
            
            // apply the keys and the version id at the end
            if ( isUsingStoredProcedures() == false )
            {
                Object [] keys = vo.getFrameworkPrimaryKey().values();            
                    
                for ( int i = 0; i < keys.length; i++ )
                {
                    args.add( new Parameter( keys[i], true ));
                }                
                
                if ( vo.isVersioned() )
                    args.add( new LongParameter( vo.getVersionID(), true ) );
            }
            
            executeSQL( s, 
                        args, 
                        false /* not a select statement */ );
            
        }
        catch ( Exception e )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:save() - " + s + e, e);
        }
        finally
        {
            if ( autoReleaseConnection() )
                release();
        }
        
		// Persistence rules for versioning dicate that in order to successfully overwrite
		// existing data, the version id of this object must equal the version id of
		// that already stored.  Since this method is called by the sub-class which
		// should be increasing either within the stored proc or embedded SQL, this
		// method will do it manually after the fact.
		if ( vo.isVersioned() )
		{
			vo.setVersionID( new Long( vo.getVersionID().longValue() + 1 ) );
		}        

        // let the base class do its thing
        return( super.save( vo ) );
    }

   /**
    * Removes the associated model from the database.
    *
    * @param        key		identifier of object to remove from the persistent store
    * @return       success or fail
    * @exception    FrameworkDAOException
    */
    public boolean delete( FrameworkPrimaryKey key ) 
    throws FrameworkDAOException 
    {
        if ( key == null )
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:delete(IFrameworkPrimaryKey) - null key not allowed" );            
            
        boolean bDeleted    = false;            
        Collection args         = key.valuesAsParameters();

        try
        {                        
            executeSQL( getDeleteSQL(), args, false /* not a select statement */ );

            // delete is successful
            bDeleted = true;
                    
            // let the base class do its thing
            super.delete( key );                       
        }
        catch ( Exception e )
        {
            throw new FrameworkDAOException("FrameworkDatabaseDAO:delete(IFrameworkPrimaryKey) - " + e, e );
        }
        finally
        {
            if ( autoReleaseConnection() )
                release();
        }

        
        return( bDeleted );
    }

// access methods
    
    /**
     * returns a Collection of input and/or output parameters for the execution
     * of the store stored procedure.
     * <p>
     * This method is deprecated in favor of getTheDatabaseArgs which
     * throws a more informative exception, FrameworkDAOException
     * 
     * @param	    loadIndicator     load or store
     * @exception	Exception
     * @deprecated
     */
    protected Collection getDatabaseArgs( boolean loadIndicator )
    throws Exception
	{
		return getTheDatabaseArgs( loadIndicator );
	}
	    
    /**
     * returns a Collection of input and/or output parameters for the execution
     * of the store stored procedure
     * 
     * @param	    loadIndicator     load or store
     * @return		Collection
     * @exception	FrameworkDAOException
     */
    protected Collection getTheDatabaseArgs( boolean loadIndicator )
    throws FrameworkDAOException
    {
        Collection args = null;
        
        boolean bIn = true;
                
        // if loading, make the in parameters into out parameters
        if ( loadIndicator == true )
        {
            // output parameters
            bIn = false;
        }        
        
        // add the primary key fields first
        if ( valueObject != null )
        {
        	FrameworkPrimaryKey pk = valueObject.getFrameworkPrimaryKey();
        	
            if ( pk != null && pk.isEmpty() == false )
            {                
               args = pk.valuesAsParameters( bIn );                
            }
            else // no primary key, so apply from a call to the primary key shell
            {
                Object [] keys = getFrameworkPrimaryKeyShell().values();            
                
                for ( int i = 0; i < keys.length; i++ )
                {
                    args.add( new Parameter( keys[i], bIn ));
                }                
            }
        }
        else
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:getTheDatabaseArgs(boolean) - attempting to persist without a value object" );
        
        return( args );
    }
    
    /**
     * Commit the assocate connection
     * @exception       FrameworkDAOException
     */
    public void commit()
    throws FrameworkDAOException
    {
    	try
    	{
	        if ( databaseQuerier != null )
    	        databaseQuerier.commit();
    	}
    	catch( Throwable exc )
    	{
    		throw new FrameworkDAOException( "FrameworkDatabaseDAO:commit() - failed to commit contained IDatabaseQuerier - " + exc, exc );
    	}
    	        
    }
    
    /**
     * Rollback the associated connection
     *
     * @exception       FrameworkDAOException
     */
    public void rollback()
    throws FrameworkDAOException
    {
    	try
    	{
	        if ( databaseQuerier != null )        
    	        databaseQuerier.rollback();
    	}
    	catch( Throwable exc )
    	{
    		throw new FrameworkDAOException( "FrameworkDatabaseDAO.rollback() - failed to rollback contained IDatabaseQuerier - " + exc, exc );
    	}
    }
    
	/**
	 * Returns a previously provided Connection pool name.  If none provided,
	 * will return the framework.xml value for DefaultDatabaseConnectionName
	 * <p>
	 * @return      String
	 */
	public String getConnectionPoolName()
	{
		if ( connectionPoolName == null )
		{
			connectionPoolName = Utility.getMainApplicationConnectionID();
		}
        
		return( connectionPoolName );
	}

	/**
	 * Assigns a Connection pool name
	 * @param	poolName
	 */
	public void setConnectionPoolName( String poolName )
	{
		connectionPoolName = poolName;
	}
    
    
//*****************************************	
// IResultSetCallback implementation
//*****************************************

    /**
     * callback method to notify of result set for the provided sql statement.
     * <p>
     * @param      	rs
     * @param       sql		SQL statemet executed
     * @return      resulting value objects
     * @exception   ResultSetCallbackException
     */
    public Collection notifyResultSet( ResultSet rs, String sql )
        throws ResultSetCallbackException
    {
        Collection returnCollection = new ArrayList();
        
        if ( rs == null )
        {
            throw new ResultSetCallbackException( "FrameworkDatabaseDAO:notifyResultSet - null ResultSet parameter." );
        }

        try
        {
            if ( rs.next() == false )
            {
                if ( getLoadSQL().equalsIgnoreCase( sql ) == true )
                {
                    // throw exception - this will be caught by refresh();
                    throw new ResultSetCallbackException( "FrameworkDatabaseDAO:notifyResultSet - rs.next() is false - resultSet is empty for sql statement" + sql );
                }
                // otherwise it must be a find related method
                else
                {
                    // Since we do not have any records return an empty resultset
                    logInfoMessage( "FrameworkDatabaseDAO:notifyResultSet - rs.next() is false - resultSet is empty for sql statement" + sql );
                    return returnCollection;
                }
            }
        }
        catch ( Exception e )
        {
            throw new ResultSetCallbackException( "FrameworkDatabaseDAO:notifyResultSet - could not access the resultset." + sql + " : ResultSet is: " + rs + ":" + e, e );
        }

        try
        {
//            if ( isUsingStoredProcedures() == true )
            {
                // Create the value objects
                returnCollection = createValueObjects( rs );                
            }
/*            
            else
            {
                ArrayList primaryKeys  = new ArrayList();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                    
                for (int nCounter = 1; nCounter <= columnCount; nCounter++)
                {                    
                    primaryKeys.add( rs.getObject( nCounter) );
                }
                    
                returnCollection = primaryKeys;
            }
            */
        }
        catch ( Exception e )
        {
            throw new ResultSetCallbackException( "FrameworkDatabaseDAO:notifyResultSet - " + e.getMessage(), e );         
        }

        return( returnCollection );
    }
    
    /**
     * Factory notification method of being released, so return
     * the IDatabaseQuerier back to the database
     */    
    public void release()
    {
        if ( databaseQuerier != null )
        {
        	try
        	{
            	ConnectionPoolManagerFactory.getObject().releaseConnection( databaseQuerier );
        	}
        	catch( ObjectCreationException exc )
        	{
        		logErrorMessage( "FrameworkDatabaseDAO.release() - Unable to release the contained IDatabaseQuerier due to failure to get the ConnectionoPoolManager from its factory  - " + exc );
        	}
        	catch( ConnectionActionException exc1 )
        	{
        		logErrorMessage( "FrameworkDatabaseDAO.release() - Failed to release the contained IDatabaseQuerier - " + exc1 );        		
        	}
        	finally
        	{
            	databaseQuerier = null;
        	}
        }
    }
    
// protected methods

    /**
     * Overloadable method which returns the default application DB connection.
     * This method is called to retrieve a connection in which to execute the stored
     * procedures.
     * <p>
     * @return      represents the default application DB connection.
     * @exception   DatabaseConnectionFailureException		
     */
    protected IDatabaseQuerier getConnection()
    throws DatabaseConnectionFailureException       
    { 
        if ( databaseQuerier == null )
        {
            // if assigned externally, attempt to use it... 
            String sConnectionName = getConnectionPoolName();
            
            if ( sConnectionName != null )
            {
                try
                {
                    databaseQuerier = (DatabaseQuerier)ConnectionPoolManagerFactory.getObject().getConnection( sConnectionName );
                }
                catch( Throwable exc )
                {
                    String sExcMsg = "FrameworkDatabaseDAO:getConnection() - failed to get connection " + sConnectionName + " from the ConnectionPoolManager : " + exc;
                    throw new DatabaseConnectionFailureException( sExcMsg, exc );
                }
            }
            
            //*****************************************************************************
            // if the dbQuerier is not yet assigned, use the main application connection
            // as specified in the framework.xml
            //*****************************************************************************
            
            if ( databaseQuerier == null )
            {
                try
                {
                    databaseQuerier = (DatabaseQuerier)getMainApplicationConnection();
                }               
                catch( Throwable exc )
                {                        
                    String sExcMsg = "FrameworkDatabaseDAO:getConnection() - failed to get connection through the main connection : " + exc;
                    throw new DatabaseConnectionFailureException( sExcMsg, exc );
                }
            }
            
            // if still not accessible, call it a day...
            if ( databaseQuerier == null )
                throw new DatabaseConnectionFailureException( "FrameworkDatabaseDAO:getConnection() - null IDatabaseQuerier returned." );
        }
        
        return( databaseQuerier ); 
    }
    
    /**
     * Overridable method that creates a Collection of associated IFrameworkValueObjects based
     * on the content of the ResultSet.
     * <p>
     * @param     	rs
     * @return    	resulting value objects      
     * @exception	FrameworkDAOException
     */
    protected Collection createValueObjects( ResultSet rs )
    throws FrameworkDAOException
    {
        ArrayList v 				= new ArrayList();
        IFrameworkValueObject vo 	= null;
        
        try
        {            
            do
            {
                // reset the value object each time in case it is
                // being used for temp storage
                setValueObject( null );
                
                // ask the subclass to create a correctly typed value object using the ResultSet                
                vo = createValueObject( rs );
                        
                // apply to the local cache
                cache( vo );
                
                // add to the return Collection
                v.add( vo );
                                
            }while ( rs.next() );
        }
        catch ( Throwable exc )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:createValueObjects() - " + exc, exc ); 
        }        
        
        return( v );
    }


    /**
     * Helper method used to safely acquire a Long. Returns null if the value cannot be translated.
     * <p>
     * @param       value
     * @return      Long
     */
    protected Long assignSafeLong( String value )
    {
        Long l = null;
        
        if ( value != null )
        {
            try
            {
                l = new Long( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( l );
    }

    /**
     * Helper method used to safely acquire a Double.  Returns null if the value cannot be translated.
     * <p>
     * @param       value
     * @return      Double
     */
    protected Double assignSafeDouble( String value )
    {
        Double d = null;
        
        if ( value != null )
        {
            try
            {
                d = new Double( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( d );
    }

    /**
     * Helper method used to safely acquire a Float.  Returns null if the value cannot be translated.
     * <p>
     * @param       value
     * @return      Float
     */
    protected Float assignSafeFloat( String value )
    {
        Float f = null;
        
        if ( value != null )
        {
            try
            {
                f = new Float( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( f );
    }

    /**
     * Helper method used to safely acquire a Short. Returns null if the value cannot be translated.
     * @param       value
     * @return      Short
     */
    protected Short assignSafeShort( String value )
    {
        Short s = null;
        
        if ( value != null )
        {
            try
            {
                s = new Short( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( s );
    }

    /**
     * Helper method used to safely acquire a Integer.  Returns null if the value cannot be translated.
     * @param       value
     * @return      Integer
     */
    protected Integer assignSafeInteger( String value )
    {
        Integer i = null;
        
        if ( value != null )
        {
            try
            {
                i = new Integer( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( i );
    }

    /**
     * Helper method used to safely acquire a Byte.  Returns null if the value cannot be translated.
     * @param       value
     * @return      Byte
     */
    protected Byte assignSafeByte( String value )
    {
        Byte b = null;
        
        if ( value != null )
        {
            try
            {
                b = new Byte( value );
            }
            catch( NumberFormatException exc )
            {
                // do nothing...
            }
        }
        
        return( b );
    }

    /**
     * Helper method used to safely acquire a Boolean from a String
     * by allowing, 1, true, or yes as indicating true..all other values indicate false
     * @param       value
     * @return      Byte
     */
    protected Boolean assignSafeBoolean( String value )
    {
        Boolean b = new Boolean( false );
        
        if ( value != null )
        {
            try
            {
                b = new Boolean( value.equals( "1" ) || value.equalsIgnoreCase( "true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase( "Y") );
            }
            catch( Exception exc )
            {
                // do nothing...
            }
        }
        
        return( b );
    }
    
    /**
     * Helper method for the safe acquisition of a Calendar
     * <p>
     * @param       date
     * @param       dateTimeFormat
     * @return      java.util.Calendar
     */     
    protected java.util.Calendar assignSafeDate( String date, String dateTimeFormat )
    {
		return( Utility.formatToCalendar( date, dateTimeFormat ) );
    }
    
    /**
     * Sub-class must implement this method since it is child dependent
     * <p>
     * @param       rs
     * @return      IFrameworkValueObject
     */
    protected IFrameworkValueObject createValueObject( ResultSet rs )
    {
        // nothing can be done here, so return what is assigned
        return( getValueObject() );
    }

    /**
     * returns the name of the create related stored procedure
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return      String 
     * @deprecated  
     */
    protected String getCreateStoredProcedureName()
    {
        throw new RuntimeException( "FrameworkDatabaseDAO:getCreateStoredProcedureName() must be overloaded" );
    }

    /**
     * Returns the name of the load related stored procedure
     * Must be overriden if using this base class' data access
     * implementation.
     * 
     * @return      String 
     * @deprecated
     */
    protected String getLoadStoredProcedureName()
    {
        throw new RuntimeException( "FrameworkDatabaseDAO:getLoadStoredProcedureName() must be overloaded" );
    }

    /**
     * Returns the name of the update related stored procedure
     * Must be overriden if using this base class' data access
     * implementation.
     * 
     * @return 		String
     * @deprecated
     */
    protected String getUpdateStoredProcedureName()
    {
        throw new RuntimeException( "FrameworkDatabaseDAO:getUpdateStoredProcedureName() must be overloaded" );
    }

    /**
     * Returns the name of the delete related stored procedure
     * Must be overriden if using this base class' data access
     * implementation.
     *<p>
     * @return 		String
     * @deprecated
     */
    protected String getDeleteStoredProcedureName()
    {
        throw new RuntimeException( "FrameworkDatabaseDAO:getDeleteStoredProcedureName() must be overloaded" );
    }


    /**
     * Returns the Create SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return      	String 
     * @exception		Exception 
     * @deprecated	In favor of getTheCreateSQL() which throws no type checked exception
     */
    protected String getCreateSQL()
    	throws Exception
    {
        return( getCreateStoredProcedureName() );
    }

    /**
     * Returns the Load SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return      String
     * @exception	Exception
     * @deprecated	In favor of getTheLoadSQL() which throws no type checked exception     
     */
    protected String getLoadSQL()
    	throws Exception
    {
        return( getLoadStoredProcedureName() );
    }

    /**
     * Returns the Update SQL Related String
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return     	String
     * @exception	Exception
     * @deprecated	In favor of getTheUpdateSQL() which throws no type checked exception
     */
    protected String getUpdateSQL()
    throws Exception
    {
        return( getUpdateStoredProcedureName() );
    }

	/**
	 * Returns the Delete SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return		String   
     * @exception	Exception
     * @deprecated	In favor of getThedeleteSQL() which throws no type checked exception
     */
    protected String getDeleteSQL()
   	throws Exception
    {
        return( getDeleteStoredProcedureName() );
    }

    /**
     * Returns the Create SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return    	String 
     */
    protected String getTheCreateSQL()
    {
        return( getTheCreateSQL() );
    }

    /**
     * Returns the Load SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return    	String
     */
    protected String getTheLoadSQL()
    {
        return( getTheLoadSQL() );
    }

    /**
     * Returns the Update SQL Related String
     * Must be overriden if using this base class' data access
     * implementation.
     * 
     * @return     String
     */
    protected String getTheUpdateSQL()
    {
        return( getTheUpdateSQL() );
    }

	/**
	 * Returns the Delete SQL Related String.
     * Must be overriden if using this base class' data access
     * implementation.
     * <p>
     * @return		String   
     */
    protected String getTheDeleteSQL()
    {
        return( getTheDeleteSQL() );
    }
        
    /**
     * Returns true or false depending upon the usage of Stored Procedures vs.
     * embedded SQL. 
     * <p>
     * @return      boolean
     */
    protected boolean isUsingStoredProcedures()
    {
        return( isUsingStoredProcedures );
    }
    
    /**
     * Sub-class assigns T/F depending upon the usage of Stored Procedures vs.
     * embedded SQL. 
     * <p>
     * @param       using
     */
    protected void isUsingStoredProcedures( boolean using )
    {
        isUsingStoredProcedures = using;
    }
    
// private methods

    /**
     * Inserts the associated dataArgs into the database, and returns
     * it's key as a FrameworkPrimaryKey
     * <p>
     * @param       dataArgs				Parameters, which represent the args to provide to the persistent store
     * @return      FrameworkPrimaryKey
     * @exception   FrameworkDAOException
     */
    private FrameworkPrimaryKey create( Collection dataArgs )
    throws FrameworkDAOException 
    {
        if ( dataArgs == null )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:create() - null Collection provided" );
        }

        Collection returnValues     = null;
        DatabaseQuerier dbQuerier   = null;
        FrameworkPrimaryKey key     = null;
        
        try
        {
            
            // Get a Database connection
            dbQuerier = (DatabaseQuerier)getConnection();
            
            if ( dbQuerier == null )
            	throw new FrameworkDAOException( "FrameworkDatabaseDAO:create(Collection) - getConnection() returned a null value.");
            
            /////////////////////////////////////////////////
            // execute the create stored proc.
            /////////////////////////////////////////////////
            if ( this.isUsingStoredProcedures() )
            {
                Object [] keys = getFrameworkPrimaryKeyShell().values();            
                
                for ( int i = 0; i < keys.length; i++ )
                {
                    dataArgs.add( new Parameter( keys[i], false /*out param*/  ));
                }
                
				returnValues = executeSQL( getCreateSQL(), dataArgs, false /* not select related */);
				                
//                returnValues = dbQuerier.executeStoredProcedure( getCreateSQL(), dataArgs );
                
                // the return values should be the key values of the created entity
                key = new FrameworkPrimaryKey( returnValues );
            }
            else
            {
                // create the database entry
                dbQuerier.executeStatement( getCreateSQL(), dataArgs );      
                
                // already assigned during argument acquisition...
                key = getValueObject().getFrameworkPrimaryKey();
            }            
        }
        catch ( Throwable e )
        {
            throw new FrameworkDAOException( "FrameworkDatabaseDAO:create( Collection ) " + e, e );
        }
        finally
        {
            if ( autoReleaseConnection() )
                release();
        }
        
        return( key );
    }

    /**
     * Executes the provided SQL string either as a stored procedure, or 
     * as a direct SQL call.
     * <p>
     * @param    	sql					SQL statement
     * @param       args				arguments to apply to the SQL statement
     * @param       isSelectRelated		is the sql statement select related
     * @exception	ExecuteStatementException	
     */
    protected Collection executeSQL( String sql, Collection args, boolean isSelectRelated )
    throws ExecuteStatementException
    {
        // Retrieve the database connection
        DatabaseQuerier dbQuerier   = null;
        Collection coll             = null;

		try
		{
			dbQuerier = (DatabaseQuerier)getConnection();       
    	}
    	catch( DatabaseConnectionFailureException exc )
    	{
			throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc, exc );
    	}        
        //////////////////////////////////////////////////
        // execute the delete stored procedure
        //////////////////////////////////////////////////
        if ( dbQuerier != null )
        {
            if ( isUsingStoredProcedures() )
            {
            	try
            	{
	                if ( isSelectRelated == false )
	                {
	                    coll = dbQuerier.executeStoredProcedure( sql, args );                  
	                }
	                else    // result set will be returned and handled
	                {
	                    coll = dbQuerier.executeStoredProcedure( sql, args, this ); 
	                }
            	}
            	catch( StoredProcedureException exc )
            	{
					throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc, exc );	            		
            	}
		    	catch( DatabaseConnectionFailureException exc1 )
		    	{
					throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc1, exc1 );
		    	}            	
            }
            else
            {
                if ( isSelectRelated == false )
                {
                	try
                	{
                    	dbQuerier.executeStatement( sql, args );
                	}
                	catch( ExecuteStatementException exc )
                	{
						throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc, exc );	                		
                	}
			    	catch( DatabaseConnectionFailureException exc1 )
			    	{
						throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc1, exc1 );
		    		}                  	
                }
                else    // result set will be returned and handled
                {
                	try
                	{
                    	coll = dbQuerier.executeSelectStatement( sql, args, this );
                	}
                	catch( SelectStatementException exc )
                	{
						throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc, exc );	                		
                	}
			    	catch( DatabaseConnectionFailureException exc1 )
			    	{
						throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - error executin sql " + sql + " - " + exc1, exc1 );
		    		}                  	
                }
            }            

        }
        else
        {
            throw new ExecuteStatementException( "FrameworkDatabaseDAO:executeSQL(...) - null IDatabaseQuerier returned from the Connectionpool Manager for sql call - " + sql );
        }
                
        return( coll );        
    }
        
// attributes

    /**
     * stored procedure indicator
     */
    protected boolean isUsingStoredProcedures = true;     

    /**
     * DatabaseQuerier in use
     */
    protected DatabaseQuerier databaseQuerier	= null;     
    
	/**
	 * Variable assigned by an external client forcing usage of a particulare 
	 * Connection pool named
	 */
	protected String connectionPoolName 			= null;
    
}

/*
 * Change Log:
 * $Log: FrameworkDatabaseDAO.java,v $
 */
