package com.framework.presentation.servlet;

import com.framework.integration.persistent.FrameworkPersistenceHelper;


import javax.servlet.*;
import javax.servlet.http.*;


public class FrameworkBaseStrutsFilterDispatcher
    extends org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter
{
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
    throws java.io.IOException, ServletException
    {
//        System.out.println("  ** Inside of BaseServlet.doGet() ");
        try
        {
            // Begin unit of Hibernate work
        	FrameworkPersistenceHelper.self().getCurrentSession().beginTransaction();

            super.doFilter(req, res, chain);

            // End unit of Hibernate work
            FrameworkPersistenceHelper.self().getCurrentSession().getTransaction().commit();	
        }
        catch (Exception ex)
        {
        	FrameworkPersistenceHelper.self().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
        finally
        {
        	FrameworkPersistenceHelper.self().closeSession();
        }
    }
}
