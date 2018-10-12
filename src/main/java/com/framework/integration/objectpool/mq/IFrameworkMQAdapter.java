/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.mq;

//************************************
// Imports
//************************************
import java.util.Collection;

import com.framework.common.message.IFrameworkMQMessage;

/**
 * This interface is soon to be deprecated.
 *
 * <p>
 * @author    realMethods, Inc.
 * 
 * @deprecated	version 2.3
 */
public interface IFrameworkMQAdapter
{
    /**
     * Called by Adapter Consumer when data is required
     *
     * @return      IFrameworkMQMessage
     */
    public IFrameworkMQMessage provideDataToSend();

    /**
     * Called by the Adapter Consumer to provide data
     * <p>
     * @param       data
     */
    public void receiveData( String data );        

    /**
     * Called by the Adapter Consumer to determine 
     * if more data can be provided to the Adapater
     *
     * @param       currentMessage
     * @return      boolean
     */
    public boolean isAbleToReceiveMoreData( String currentMessage );
    
    /**
     * returns a collection of the data received...one entry for each call
     * to receive
     *
     * @return      Collection
     */
    public Collection getResultsAsCollection();
    
    /**
     * returns a String comprised of the concatenation of each received data entry
     *
     * @return      String
     */    
    public String getReceviedDataAsString();
    
    /**
     * returns the last String provided
     *
     * @return      String
     */
    public String getLastReceivedData();    
    
    /**
     * Method determines if the input message will cause another message to be
     * sent on the corresponding MQ.  Returning null implies there is no msg to re-send
     *
     * @param       inputMessage
     * @return      the output message to resend
     */
    public String determineMessageToReSend( String inputMessage );    
}

/*
 * Change Log:
 * $Log: IFrameworkMQAdapter.java,v $
 */
