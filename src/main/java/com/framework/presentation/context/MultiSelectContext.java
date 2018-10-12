/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.context;

//***************************
//Imports
//***************************

import java.util.Collection;

import com.framework.common.context.IFrameworkContext;

/**
 * Class to hold data related to the process of multiple selections 
 */
public class MultiSelectContext implements IFrameworkContext
{
	
// access methods

	public Object[] getSelected()
	{
		return( selected );	
	}

	public void setSelected( Object [] selected )
	{
		this.selected = selected;	
	}
	
	public Collection getSelectFrom()
	{
		return( selectFrom );
	}

	public void setSelectFrom( Collection selectFrom )
	{
		this.selectFrom = selectFrom;	
	}
	
	public Collection getAttributes()
	{
		return( attributes );
	}
	
	public void setAttributes( Collection attributes )
	{
		this.attributes = attributes;
	}
	
// attributes

	protected Object [] selected 	= null;
	protected Collection selectFrom	= null;
	protected Collection attributes = null;
}

/*
 * Change Log:
 * $Log$
 */



