package com.framework.integration.persistent.jpa;

import javax.persistence.*;

public final class EMF{
	private static EntityManagerFactory emfInstance = null;
	
	static
	{
		try
		{
			emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
			System.out.println( "JPA EntityManager Factory created..." );
		}
		catch( Throwable exc )
		{
			System.out.println( "JPA EntityManager Factory is unavailable..." );
		}
	}
	
	private EMF() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}
}