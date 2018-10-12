/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.etc;

import javax.jms.*;

import com.framework.common.FrameworkBaseObject;

/**
 * Test MessageListener implementatio
 * <p>
 * @author    realMethods, Inc.
 */
public class TestJMSMessageListener 
	extends FrameworkBaseObject
	implements MessageListener
{
	public TestJMSMessageListener()
	{}
	
// MessageListener implementation

    /**
     * Method called by JMS to handle the reception of a Message
     *
     * @param	message		input msg
     */
    public void onMessage( javax.jms.Message message) 
    {
    	if ( message != null )
		{	
    		try
			{
				if ( message instanceof javax.jms.ObjectMessage )						
					logMessage( "TestJMSMessageListener.onMessage() recevied msg " + ((com.framework.common.message.TaskJMSMessage)((javax.jms.ObjectMessage)message).getObject()).getJMSString() + " from " + message.getJMSDestination().toString() + " with correlation id " + message.getJMSCorrelationID() );
				else //  assume it is a TextMessage
					logMessage( "TestJMSMessageListener.onMessage() recevied msg " + ((javax.jms.TextMessage)message).getText()+ " from " + message.getJMSDestination().toString() );	
			}
			catch( JMSException exc )
			{
				logMessage( "TestJMSMessageListener.onMessage() caught the following Exception - " + exc );
			}
    	}
		else
			logMessage( "TestJMSMessageListener.onMessage() recevied a null javax.jms.Message" );				    	
    }	
}
