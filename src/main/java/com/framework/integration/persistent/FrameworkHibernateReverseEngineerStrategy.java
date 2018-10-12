package com.framework.integration.persistent;

import java.util.*;

import org.hibernate.cfg.reveng.*;

public class FrameworkHibernateReverseEngineerStrategy extends DelegatingReverseEngineeringStrategy  
{

    public FrameworkHibernateReverseEngineerStrategy(ReverseEngineeringStrategy delegate) 
    {
    	super(delegate);
    }

    public String foreignKeyToEntityName(String keyname, 
    		TableIdentifier fromTable, 
    		List fromColumnNames, 
    		TableIdentifier referencedTable, 
    		List referencedColumnNames, 
    		boolean uniqueReference) 
    {
    	String key = super.foreignKeyToEntityName(keyname, fromTable, fromColumnNames, referencedTable, referencedColumnNames, uniqueReference);
    	System.out.println( "key.entityName  = " + key );
    	return( key );
    }

	public String foreignKeyToCollectionName(String keyname, TableIdentifier fromTable, List fromColumns, TableIdentifier referencedTable, List referencedColumns, boolean uniqueReference)
	{
		String key = super.foreignKeyToCollectionName(keyname, fromTable, fromColumns, referencedTable, referencedColumns, uniqueReference);
		
		System.out.println( "key.collectionname = " + key );
		return( key );
	}
    
/**
    public String foreignKeyToEntityName(String keyname, TableIdentifier fromTable, TableIdentifier referencedTable ) 
    {
    	String key = super.foreignKeyToEntityName(keyname, fromTable, referencedTable );
    	System.out.println( "key.entityName  = " + key );
    	return( key );
    }
    
	public String foreignKeyToCollectionName(String keyname, TableIdentifier fromTable, TableIdentifier referencedTable )
	{
		String key = super.foreignKeyToCollectionName(keyname, fromTable, referencedTable );
		
		System.out.println( "key.collectionname = " + key );
		return( key );
	}
	
	public String jdbcToHibernateType( int sqlType, int length, int precision, int scale ) 
	{
		String type = super.jdbcToHibernateType( sqlType, length, precision, scale );

		if ( type != null && type.equalsIgnoreCase("set") )
		{
			type = "array";
		}
		return( type );
		
	}
**/
	public String columnToHibernateTypeName(TableIdentifier table, String columnName, int sqlType, int length, int precision, int scale, boolean nullable, boolean generatedIdentifier) 
	{
		String type = super.columnToHibernateTypeName(table, columnName, sqlType, length, precision, scale, nullable, generatedIdentifier );

		System.out.println( "hibernate.type = "  + type );
		if ( type != null && type.equalsIgnoreCase("set") )
		{
			type = "array";
		}
		return( type );
		
	}

	public String getTableIdentifierStrategyName(org.hibernate.cfg.reveng.TableIdentifier t) 
	{
		return "native"; 
	}
	
	public String tableToClassName(TableIdentifier tableIdentifier) 
	{
		return( super.tableToClassName(tableIdentifier));
	}	
	

}
