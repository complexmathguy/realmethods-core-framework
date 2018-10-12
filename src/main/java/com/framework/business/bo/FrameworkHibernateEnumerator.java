/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.business.bo;

import java.io.Serializable; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Types; 
import org.hibernate.HibernateException; 
import org.hibernate.usertype.UserType; 

public class FrameworkHibernateEnumerator<E extends Enum<E>> 
implements UserType 
{ 
    protected FrameworkHibernateEnumerator(Class<E> c) 
    { 
        this.clazz = c; 
    } 
 
    private static final int[] SQL_TYPES = {Types.VARCHAR}; 
    public int[] sqlTypes() 
    { 
        return SQL_TYPES; 
    } 
 
    public Class returnedClass() 
    { 
        return clazz; 
    } 
 
    public Object nullSafeGet(ResultSet resultSet, String[] names, org.hibernate.engine.spi.SessionImplementor paramSessionImplementor, Object paramObject) throws HibernateException, SQLException 
    {
        String name = resultSet.getString(names[0]); 

    	// System.out.println( "FrameworkHibernateEnumerator:nullSafeGet()- name:" + name + ", class:" + clazz.getName());

        E result = null; 
        if (!resultSet.wasNull()) 
        { 
            result = Enum.valueOf(clazz, name);
        }    

        return result; 
    } 
 
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, org.hibernate.engine.spi.SessionImplementor paramSessionImplementor) throws HibernateException, SQLException 
    {
        if (null == value) 
        {
        	// System.out.println( "FrameworkHibernateEnumerator:nullSafeSet() value is null - but index is" + String.valueOf(index) );

            preparedStatement.setNull(index, Types.VARCHAR); 
        } 
        else 
        {
        	// System.out.println( "FrameworkHibernateEnumerator:nullSafeSet()- " + ((Enum)value).name() );
        	
            preparedStatement.setString(index, ((Enum)value).name()); 
        } 
    } 
 
    public Object deepCopy(Object value) throws HibernateException
    { 
        return value; 
    } 
 
    public boolean isMutable() 
    { 
        return false; 
    } 
 
    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
         return cached;
    } 

    public Serializable disassemble(Object value) throws HibernateException 
    { 
        return (Serializable)value; 
    } 
 
    public Object replace(Object original, Object target, Object owner) throws HibernateException 
    { 
        return original; 
    } 
    
    public int hashCode(Object x) throws HibernateException 
    { 
        return x.hashCode(); 
    } 
    
    public boolean equals(Object x, Object y) throws HibernateException 
    { 
        if (x == y) 
            return true; 
        if (null == x || null == y) 
            return false; 
        return x.equals(y); 
    } 

// attributes
    
    private Class<E> clazz = null; 
    
} 
