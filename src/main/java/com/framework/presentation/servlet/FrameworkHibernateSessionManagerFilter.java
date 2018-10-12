package com.framework.presentation.servlet;

import com.framework.integration.persistent.*;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class FrameworkHibernateSessionManagerFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException
	{
		try
		{
			FrameworkPersistenceHelper.self().getCurrentSession();
			chain.doFilter(req, res);
		}
		catch( Throwable exc )
		{
		}
		finally
		{
			FrameworkPersistenceHelper.self().closeSession();
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
