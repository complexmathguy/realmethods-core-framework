/************************************************************************ 
*   realMethods: Professionally Supported Open Source Framework
*
*   The contained free software can be redistributed and/or modified
*   under the terms of the GNU General Lesser Public License as published by
*   the Free Software Foundation.  See terms of license at gnu.org.
*
*   This program is distributed WITHOUT ANY WARRANTY.  See the
*   GNU General Public License for more details.
*************************************************************************/

package com.framework.integration.objectpool.mq;

//************************************
// Imports
//************************************

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.message.FrameworkMQMessage;
import com.framework.common.message.IFrameworkMQMessage;

/**
 * Provides base capabilities of an IFrameworkMQAdapter.  An MQ adapater
 * is an entity that knows how to send and receive format data
 * in a well-defined fashion on an IBM MQ Queue.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMQAdapter 
    extends FrameworkBaseObject implements IFrameworkMQAdapter
{

// constructors

    /**
     * Use this constructor when receiving a single message
     */     
    public FrameworkMQAdapter()
    {
        messageToSend = "";
    }

    /**
     * Use this constructor when you possibly have a message to receive
     * that is terminated by a special indicator
     *
     * @param	terminator
     */
    public FrameworkMQAdapter( FrameworkMQReceiveTerminator terminator )
    {
        mqReceiveTerminator = terminator ;
    }
    
    /**
     * Use this constructor when you have a message to send
     *
     * @param 	theMessageToSend
     */
    public FrameworkMQAdapter( String theMessageToSend )
    {
        messageToSend = theMessageToSend;
    }

    /**
     * Use this constructor when you have a message to send and message
     * to receive that may require multiple invocations and is terminated
     * by the special terminator.
     *
     * @param       theMessageToSend
     * @param       terminator
     */
    public FrameworkMQAdapter( String theMessageToSend, FrameworkMQReceiveTerminator terminator )
    {
        messageToSend      		= theMessageToSend;
        mqReceiveTerminator   	= terminator;        
    }
    
    /**
     * Called by Adapter Consumer when data is required
     * <p>
     * @return      IMQMessage
     */
    public IFrameworkMQMessage provideDataToSend()
    {
        return( new FrameworkMQMessage( messageToSend ) );
    }

    /**
     * Called by the Adapter Consumer to provide data
     * <p>
     * @param 	data
     */
    public void receiveData( String data )
    {
        lastDataReceived = data;
        
        receiveData.add( lastDataReceived );        
    }
    

    /**
     * Called by the Adapter Consumer to determine 
     * if more data can be provided to the Adapater
     * <p>
     * @param       currentMessage
     * @return      boolean
     */
    public boolean isAbleToReceiveMoreData( String currentMessage )
    {
        boolean bCanReceiveMore = false;
        
        if ( mqReceiveTerminator != null && currentMessage != null )
        {
            bCanReceiveMore = currentMessage.endsWith( mqReceiveTerminator.toString() );
        }
        
        return( bCanReceiveMore );
    }
        
    /**
     * Returns a collection of the data received...one entry for each call
     * to receive.
     * <p>
     * @return      Collection
     */
    public Collection getResultsAsCollection()
    {
        return( receiveData );
    }

    /**
     * Returns a String comprised of the concatenation of each received data entry
     * <p>
     * @return      String
     */    
    public String getReceviedDataAsString()
    {
        StringBuffer sBuffer    = new StringBuffer();
        Iterator iter           = receiveData.iterator();
        
        while( iter.hasNext() )
        {
            sBuffer.append( (String)iter.next() );
        }
        return( sBuffer.toString() );
    }
    
    /**
     * Returns the last String provided
     *
     * @return      String
     */
    public String getLastReceivedData()
    {
        return( lastDataReceived );
    }
    
    /**
     * Method determines if the input message will cause another message to be
     * sent on the corresponding MQ.  Returning null implies there is no msg to re-send
     * <p>
     * Overload this method for Adapter that are involved with receiving  data
     * that requires multiple send/receive sequences.
     *  <p>
     * @param       inputmsg
     * @return       the output message to resend
     */
    public String determineMessageToReSend( String inputmsg )
    {
        return( null );
    }
    
// attributes

    /**
     * Collection of received data inputs
     */
    protected ArrayList receiveData                      			= new ArrayList();
    
    /**
     * Receive Termination indicator - null means only one receive is permissible
     */
    protected FrameworkMQReceiveTerminator mqReceiveTerminator     = null;
    
    /**
     * Last data received by class consumer
     */
    protected String lastDataReceived                    			= null;
    
    /**
     * The acutal message to send
     */
    protected String messageToSend                       			= null;
    
}


/*
 * Change Log:
 * $Log: FrameworkMQAdapter.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:39  tylertravis
 * initial sourceforge cvs revision
 *
 */
