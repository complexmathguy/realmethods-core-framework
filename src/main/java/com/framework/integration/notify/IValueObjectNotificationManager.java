/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.notify;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.exception.ValueObjectNotificationException;

/**
 * Value Object notification manager base interface.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IValueObjectNotificationManager extends java.io.Serializable
{
	/**
	 * Determines how to use JMS in order to subscribe for
	 * events related to the provided value object.  Provide a non-null
	 * value for sMsgSelector to filter on the which IFrameworkValueObject's
	 * you would like to be notified about.  See the JDK 1.3 specs for more
	 * information on JMS and message selector syntax.
	 * <p>
	 * @param		listener		who to notify
	 * @param       valueObject		object target to notify on
	 * @param     	msgSelector		query like criteria to notify on
	 * @exception	ValueObjectNotificationException
	 */
    public void subscribeForValueObjectNotification(	javax.jms.MessageListener listener, 
                                                        IFrameworkValueObject valueObject,
                                                        String msgSelector )
   	throws ValueObjectNotificationException;
                                                
	/**
	 * Notify any listeners of the ValueObjectNotificationEvent.
	 * <p>
	 * @param		notificationEvent
	 * @exception	ValueObjectNotificationException
	 */
    public void notifyValueObjectListeners( ValueObjectNotificationEvent notificationEvent )
    throws ValueObjectNotificationException;   
}

/*
 * Change Log:
 * $Log: IValueObjectNotificationManager.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:13  tylertravis
 * initial sourceforge cvs revision
 *
 */
