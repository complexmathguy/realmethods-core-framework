/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.taglib;

import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import javax.servlet.jsp.tagext.BodyTagSupport;

import com.framework.common.exception.ObjectCreationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.cache.IUserSessionObjectManager;

import com.framework.presentation.translator.IBaseTranslator;


/**
 * This class is the place to apply the appropriate
 * IBaseTranslator for the tag library file FrameworkTagLib.tld
 * <p>
 * @author    realMethods, Inc.
 */
public class TranslatorTag extends BodyTagSupport
{
//***********************************************
// Public Methods
//***********************************************
    /**
     * default constructor
     */
    public TranslatorTag()
    {
        super();
    }

    /**
     * Sets the parameter "Type" which correlates to the 
     * class name of the Translator. The container will use this to set
     * the class type of the scripting variable.
     * 
     * @param type Type of Translator
     */
    public void setType( String type )
    {
        this.type = type;
    }

    /**
     * Start Tag processing.
     * @return SKIP_BODY
     * @exception JspTagException
     */
    public int doStartTag() throws JspTagException 
    {
        int nReturn = SKIP_BODY;
        
        // Validate that we have a name and type
        if ((id != null) && (type != null))
        {
            // Create the class of TYPE specified
            IBaseTranslator newTranslator = null;
            try
            {
                newTranslator = createTranslator();
            }
            catch (ObjectCreationException exc)
            {
                throw new JspTagException("Inside TranslatorTag::doStartTag() - could not create translator because :" + exc);
            }
            
            pageContext.setAttribute( id, newTranslator, PageContext.PAGE_SCOPE);
        }

        return nReturn;
    }

    /**
     * End of tag processing.
     * @return EVAL_PAGE
     * @exception JspTagException
     */
    public int doEndTag() throws JspTagException 
    {
        return EVAL_PAGE;
    }

//***********************************************
// Protected/Private Methods
//***********************************************
    /**
     * Helper method to dyamically create the translator to use.
     * @return 		IBaseTranslator
     * @exception	ObjectCreationException
     */
    protected IBaseTranslator createTranslator() 
    throws ObjectCreationException
    {
        // ------------------------------
        // Create the translator
        // ------------------------------
        // Now retrieve the Class object for the class name
        IBaseTranslator translatorClassInstance = null;

        try
        {
            translatorClassInstance = (IBaseTranslator)Class.forName( type ).newInstance();
        }
        catch ( Throwable exc )
        {
            throw new ObjectCreationException( "TranslatorTag:createTranslator(..) - Translator class not found - " + exc, exc );            
        }

        IUserSessionObjectManager usom = getUserSessionObjectManager();

        // initialize with both the servletRequest, and the USOM, since both
        // are valid caches.
        translatorClassInstance.init( pageContext.getRequest(), usom );
        
        // make sure the class created implements IBaseTranslator
        if ( !(translatorClassInstance instanceof IBaseTranslator ) )
        {
            throw new ObjectCreationException( "TranslatorTag:createTranslator(..) - Translator class created doesn't implement IBaseTranslator." );
        }

        return(IBaseTranslator)translatorClassInstance;
    }
    
    /**
     * Retrieves the UserSessionObjectManager by first looking to see if there is an IUserSession
     * implementation for the current HttpSession.  If so, get the USOM from it, otherwise,
     * look for the USOM withi the HttpSession directly.  The first case provides for USOM access when
     * the realMethods Presentation Tier is in use.  The second provides for USOM access when Struts is in
     * use.
     * <p>
     * @return IUserSessionObjectManager 	null if none is found.
     */
    protected IUserSessionObjectManager getUserSessionObjectManager()
    {
        IUserSessionObjectManager returnManager = null;
        
        // Retrieve the current session
        HttpSession currentSession = pageContext.getSession();
        returnManager = ( (IUserSessionObjectManager)currentSession.getAttribute( FrameworkNameSpace.APPLICATION_CACHE_NAME ) );
        
        return returnManager;
    }

//***********************************************
// Attributes
//***********************************************
    /**
     * Type of translator - class name.
     */
    private String type = null;
    
   /**
    * Static constant for the class name for UserSessionObjectManager.
    */
   private final static String USERSESSIONOBJECTMANAGER_CLASSNAME = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.USOM_CLASS_NAME );
}

/*
 * Change Log:
 * $Log: TranslatorTag.java,v $
 */
