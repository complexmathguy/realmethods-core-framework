/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.dao;

import java.util.Properties;

import javax.jdo.*;
import javax.persistence.EntityManager;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.properties.PropertyFileLoader;
import com.framework.integration.persistent.FrameworkHibernatorInterceptorFactory;

/**
 * Base class for all JDO DAO classes generated and supported by the framework.
 * 
 * @author realMethods, Inc.
 */
public class FrameworkJDODAO extends FrameworkDAO
{
	protected PersistenceManager getEntityManager()
	{
		return com.framework.integration.persistent.FrameworkPersistenceHelper.self().getPersistenceManager();
	}

	protected void rollback(PersistenceManager pm)
	{
		// no_op
	}

	protected void commit(PersistenceManager pm)
	{
		// no_op
	}
	
	protected void close(PersistenceManager pm)
	{
		// no_op
	}
	
}
