/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.taglib;

//*******************************************
// Imports
//*******************************************
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspTagException;

import javax.servlet.jsp.tagext.TagSupport;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.namespace.FrameworkNameSpace;

/**
 * Applies a hidden field containing the current token value located
 * in the HttpSession.
 * <p>
 * @author    realMethods, Inc.
 */
public class TokenTag extends TagSupport
{
//***********************************************
// Public Methods
//***********************************************
    /**
     * default constructor
     */
    public TokenTag()
    {
        super();
    }

    /**
     * Start Tag processing
     * <p>
     * @return int - SKIP_BODY
     * @exception JspTagException
     */
    public int doStartTag() throws JspTagException 
    {
        // Flush the page's output before we start
        try
        {
            pageContext.getOut().flush();
        }
        catch ( Throwable e )
        {
            // do nothing
        }        
        
        return SKIP_BODY;
    }

    /**
     * End of tag processing.
     * <p>
     * @return int - EVAL_PAGE
     * @exception JspTagException
     */
    public int doEndTag() throws JspTagException 
    {
    	try
    	{
            // Then print out the value
            pageContext.getOut().print( getTokenFieldOutput() );
        }
        catch ( Throwable ex )
        {
            new FrameworkBaseObject().logErrorMessage("TokenTag:doEndTag caught: " + ex);
        }
        return EVAL_PAGE;
    }

//***********************************************
// Protected/Private Methods
//***********************************************

	/**
	 * Returns the HTML text for the  hidden token field
	 * @return		hidden token field
	 */
	protected String getTokenFieldOutput()
	{		
		StringBuffer output = new StringBuffer();
		String token		= (String)pageContext.getSession().getAttribute( FrameworkNameSpace.TOKEN_KEY );

		if ( token == null )	// not generated externally, so do it now
		{		
			HttpSession session = pageContext.getSession();
			
			token = com.framework.common.misc.Utility.generateUID() + session.getId();
		
			session.setAttribute( FrameworkNameSpace.TOKEN_KEY, token );
		}
		
		output.append( "<input type=\"hidden\" name=\"" );
		output.append( FrameworkNameSpace.TOKEN_KEY );
		output.append( "\" value=\"" );
		output.append( token  );
		output.append( "\">" );
		
		return( output.toString() );
	}
	
//***********************************************
// Attributes
//***********************************************

}

/*
 * Change Log:
 * $Log: TokenTag.java,v $

 */
