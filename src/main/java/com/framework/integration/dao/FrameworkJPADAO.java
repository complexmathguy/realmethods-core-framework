/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.dao;

import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.persistence.*;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.properties.PropertyFileLoader;


/**
 * Base class for all JPA DAO classes generated and supported by the framework.
 * 
 * @author realMethods, Inc.
 */
public class FrameworkJPADAO extends FrameworkDAO
{

	protected EntityManager getEntityManager()
	{
		return com.framework.integration.persistent.FrameworkPersistenceHelper.self().getEntityManager();
	}
	
	protected void rollback(EntityManager em)
	{
		// no_op
	}

	protected void commit(EntityManager em)
	{
		// no_op
	}
	
	protected void close(EntityManager em)
	{
		// no_op
	}

}
