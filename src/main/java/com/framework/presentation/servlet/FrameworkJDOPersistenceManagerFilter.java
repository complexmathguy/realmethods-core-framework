package com.framework.presentation.servlet;

import com.framework.common.misc.Utility;
import com.framework.integration.persistent.*;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.jdo.*;

public class FrameworkJDOPersistenceManagerFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException
	{
		try
		{
			FrameworkPersistenceHelper.self().getPersistenceManager();
			chain.doFilter(req, res);
		}
		finally
		{
			FrameworkPersistenceHelper.self().closePersistenceManager();
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
