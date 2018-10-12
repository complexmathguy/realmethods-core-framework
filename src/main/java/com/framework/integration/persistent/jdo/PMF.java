package com.framework.integration.persistent.jdo;

import javax.jdo.*;

public final class PMF{
	private static PersistenceManagerFactory pmfInstance = null;		

	static
	{
		try
		{
			pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
			System.out.println( "JDO PersistenceManager Factory created..." );
		}
		catch( Throwable exc )
		{
			System.out.println( "JDO PersistencManager Factory failed..." );
		}
	}

	private PMF() {
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}