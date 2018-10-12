/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.notify;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.FrameworkBaseObject;

/**
 * Wrapper for a notification type and a value object.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.notify.ValueObjectNotificationManager	
 */
public class ValueObjectNotificationEvent extends FrameworkBaseObject
{
    /**
     * constructor
     *
     * @param     	vo			target
     * @param       type		notification type
     * @exception   IllegalArgumentException
     */
    public ValueObjectNotificationEvent( IFrameworkValueObject vo, ValueObjectNotificationType type )
    throws IllegalArgumentException
    {
        if ( vo == null )
            throw new IllegalArgumentException( "ValueObjectNotificationEvent() - null IFrameworkValueObject provided during construction." );
            
        if ( type == null )
            throw new IllegalArgumentException( "ValueObjectNotificationEvent() - null ValueObjectNotificationType  provided during construction." );
            
        this.valueObject 		= vo;
        this.notificationType 	= type;
    }
 
    /**
     * returns the valueObject 
     *
     * @return      IFrameworkValueObject
     */ 
    public IFrameworkValueObject getValueObject()
    { return( valueObject ); }
    
    /**
     * returns the notification type
     *
     * @return      ValueObjectNotificationType
     */
    public ValueObjectNotificationType getNotificationType()
    { return( notificationType ); }
    
// attributes
    
    /**
     * target
     */
    protected IFrameworkValueObject valueObject             = null;
    /**
     * type of notification
     */
    protected ValueObjectNotificationType notificationType  = null;
}

/*
 * Change Log:
 * $Log: ValueObjectNotificationEvent.java,v $
 */

