/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//***********************************
// Imports
//***********************************

/**
 * Base Exception class for MQ related actions.
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMQBaseException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public FrameworkMQBaseException()
    {
        super();
    }

    /** 
     * Constructor - with IBM mq exception error code result
     *      
     * @param	result
     */
    public FrameworkMQBaseException( int result )
    {
        super();
        m_Result = result;
    }

    /** Constructor with message.
     *
     * @param message   text of the exception
     */
    public FrameworkMQBaseException( String message )
    {
        super(message);    
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FrameworkMQBaseException( String message, Throwable exception )
    {
        super( message, exception ); 
    }

// base class overload(s)

    /**
     * returns the error mess
     */
    public String getMessage()
    {
        //********************************************
        // if no result, treat as a normal exception
        //********************************************
        if ( m_Result == -1 )
            return( super.getMessage() );
        else
        {
            //********************************************
            // build the error buffer
            //********************************************
            buildErrorBuffer();

            //********************************************
            // returns the value from the result array
            // which is zero-based, while the error
            // type MQ results are 1-based
            //********************************************
            return( m_sErrorBuf[ m_Result-1 ] );

        }
    }


//************************************************************************    
// Private / Protected Methods
//************************************************************************
    /**
     * conditionally builds the buffer for MQ
     * error messages
     */
    final private void buildErrorBuffer()
    {
        if ( m_sErrorBuf == null )
        {
            //*************************
            // not build yet, so do so
            //*************************
            m_sErrorBuf = new String[12];

            //*************************
            // populate it
            //*************************
            m_sErrorBuf[0] = "Check correct queue manager name is being used; retry after the MQA has restarted MQSeries.";
            m_sErrorBuf[1] = "Check queue name is correct( and local for an input queue); retry later if queue requires exclusive use, or after MQA has build missing queue.";
            m_sErrorBuf[2] = "Retry after MQA correct Q problem.";
            m_sErrorBuf[3] = "Resolve problem with adapter function or MQSeries configuration.";
            m_sErrorBuf[4] = "";
            m_sErrorBuf[5] = "Check correct queue manager and name specified; request authorization if needed.";
            m_sErrorBuf[6] = "MQA must recover lost data from logs.";
            m_sErrorBuf[7] = "Resolve error in adapter program.";
            m_sErrorBuf[8] = "Resolve problem indicated by the MQ-Reason.";
            m_sErrorBuf[9] = "The Q_Option specified for input/output is not valid. It could only be 1, 2, 3, or 4";
            m_sErrorBuf[10] = "Failed to read/write to queue - IOException";
            m_sErrorBuf[11] = "Properties file not found";
        }
    }

//************************************************************************    
// Attributes
//************************************************************************
    private static String [] m_sErrorBuf    = null;
    private int m_Result                    = -1;
}


/*
 * Change Log:
 */




