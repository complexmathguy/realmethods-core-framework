/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.misc;

import com.framework.common.startup.StartupManager;

//*****************************
// Imports
//*****************************

public class Main
{
    public static void main(String args[])
    {
    	StartupManager.getInstance().start( null /*get app. id from framework.xml*/, 
    			null /*use -D property for prop. file location*/ );
    }
}