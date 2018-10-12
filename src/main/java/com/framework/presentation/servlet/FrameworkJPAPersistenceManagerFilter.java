package com.framework.presentation.servlet;

import com.framework.common.misc.Utility;

import java.io.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;
import javax.servlet.http.*;

import com.framework.integration.persistent.*;

public class FrameworkJPAPersistenceManagerFilter implements Filter
{
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException
	{
		EntityManager mgr = null;
		EntityTransaction tx = null;
		try
		{
			mgr = FrameworkPersistenceHelper.self().getEntityManager();
			tx = mgr.getTransaction();
			tx.begin();
			
			chain.doFilter(req, res );
			
			tx.commit();
		}
		catch( Throwable exc )
		{
			if( mgr != null && mgr.isOpen() && tx.isActive())
				tx.rollback();
		}
		finally
		{
			FrameworkPersistenceHelper.self().closeEntityManager();
		}	
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
	}

	@Override
	public void destroy()
	{
	}

}
