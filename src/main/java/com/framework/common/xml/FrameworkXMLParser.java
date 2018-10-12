/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.xml;

//***************************
//Imports
//***************************
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.framework.common.FrameworkBaseObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 * Provides core XML helper methods for parsing and querying a DOM.
 * <p>
 * @author		realMethods, Ic.
 */
public class FrameworkXMLParser extends FrameworkBaseObject
{

//************************************************************************    
// Constructors
//************************************************************************
	public FrameworkXMLParser()
	{
	}
	
//************************************************************************    
// Public Methods
//************************************************************************

   /** 
    * Loads the XML document.  Uses overload version parseDocument( InputStream ).
    * <p>
  	* @param 	location		where in the xml document
   	* @return 	Element 
   	*/
  	public Element parseDocument( String location )
  	{
		root = null;
        
	  	try
	  	{
	  		URL url = new URL(location);
		  	root = parseDocument( url.openStream() );   
	  	}
	  	catch ( Throwable exc )
	  	{
		  	logDebugMessage("FrameworkXMLParser:parserDocument(String) - " + exc);

		  	root = null;
	  	}
        
	  	return( root );
  }
    
   /** 
   	* Loads the XML document.
   	* <p>
   	* @param 	stream
   	* @return 	Element 
   	*/  
	public Element parseDocument( InputStream stream )
	{
		inputStream = stream;		
	  	root = null;
	  	document = null;
        
	  	try
	  	{
		  	InputSource xmlInp = new InputSource( stream );

		 	DocumentBuilderFactory docBuilderFactory = null;		 	
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			
		  	DocumentBuilder parse = docBuilderFactory.newDocumentBuilder();
		  	document = parse.parse(xmlInp);
		  	root = document.getDocumentElement();
            
		  	try
		  	{
			 	root.normalize();
		  	}
		  	catch( NoSuchMethodError nsmError )
		  	{
				logErrorMessage("FrameworkXMLParser:parserDocument(String) - " + nsmError);
	  		}
		}
  		catch ( SAXParseException err )
  		{
	  		logErrorMessage("FrameworkXMLParser ** Parsing error" + ", line " +
 			err.getLineNumber () + ", msg " + err.getMessage()  );
                           
	  		root = null;
 	 	}
  		catch ( Throwable e )
  		{
	  		logErrorMessage("FrameworkXMLParser:parseDocument( InputStream stream ) error: " + e);

		  	root = null;
	  	}

	  	return root;
  	}
  	
   /**
   	* Retrieves the SubTagValue of the node passed in base upon the passed in tag name.
   	* <p>
   	* @param 	node
   	* @param   	subTagName
   	* @return 	The tag value
   	*/
  	public String getSubTagValue( Node node, String subTagName )
  	{
	  	String returnString = "";

	  	// If the passed in node is not null
  		if ( node != null )
  		{
	  		// Retrieve the children nodes
  			NodeList  children = node.getChildNodes();

  			// no since going on...
  			if ( children == null )	
	  			return( returnString );
		
  			// Cycle through the children nodes
  			for ( int innerLoop =0; innerLoop < children.getLength(); innerLoop++ )
  			{
	  			// Retrieve node
  				Node  child = children.item(innerLoop);

			  	//If there was a child and the name is not null and the nodename is the tag passed in
  				if ( (child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName) )
  				{
	  				// Retrieve the child node
  					Node grandChild = child.getFirstChild();

  					// If there is a child node return the node value
		  			if ( grandChild != null && grandChild.getNodeValue() != null )
		  			{
			  			returnString = grandChild.getNodeValue();
			  			break;
		  			}
	  			}
  			} // end inner loop
	  	}
	  	return returnString;
  	}	

	/**
	 * Returns the key/value pairing for all the attribute name/values for the first node
	 * occurance of the given node name.
	 * <p>
	 * @param 	nodeName
	 * @return	attribute name/value pairings
	 */
	public Map getAttributesForFirstOccurance( String nodeName )
	{
		Map attributes = new HashMap();
		
		// Retrieve a list of elements that are of default view
		NodeList list = root.getElementsByTagName(nodeName);
        
		if ( list != null )
		{
			// Get the node - only the retrieve the first one
			Node node = list.item(0);
			
			if ( node != null )
			{
				attributes = getAttributes( node ); 
			}
			
	//		System.out.println("\n\nretrievefirstfortagname - " + attributes );
		}
		
		return( attributes );
	}
	
	/**
	 * Returns a Collection of key/value Maps for all the attribute name/values 
	 * for each node of the given node name.
	 * <p>
	 * @param 	nodeName
	 * @return	a Collection of attribute name/value pairings
	 */
	public Collection getAttributesForEachOccurance( String nodeName )
	{
		ArrayList list = new ArrayList();
		
		// Retrieve a list of elements that correspond to the provided node name
		NodeList nodes 	= root.getElementsByTagName( nodeName );
		
		if ( nodes != null )
		{
	        Node node = null;
	        
			for( int index = 0; index < nodes.getLength(); index++ )
			{
				node =  nodes.item( index );
				
				if ( node != null )
				{
					list.add( getAttributes( node ) ); 
				}
			}
		}
		else
		{
			logErrorMessage( "FrameworkXMLParser.getAttributesForEachOccurance() - Node name " + nodeName + " is unable to be located." );
		}
					
	//	System.out.println("\n\ngetAttributesForEachOccurance - " + list );
							
		return( list );
	}

	/**
	 * Returns a Collection of element names associated with the nodeName
	 * <p>
	 * @param 	nodeName
	 * @return	a Collection of element names
	 */
	public Collection getElementsForEachOccurance( String nodeName )
	{
		ArrayList list = new ArrayList();
		
		// Retrieve a list of elements that correspond to the provided node name
		NodeList nodes 	= root.getElementsByTagName( nodeName );
		
		if ( nodes != null )
		{
			Node node = null;
	        
			for( int index = 0; index < nodes.getLength(); index++ )
			{
				node =  nodes.item( index );
				
				if ( node != null )
				{
					list.add( getElements( node ) ); 
				}
			}
		}
		else
		{
			logErrorMessage( "FrameworkXMLParser.getElementsForEachOccurance() - Node name " + nodeName + " is unable to be located." );
		}
					
	//	System.out.println("\n\ngetAttributesForEachOccurance - " + list );
							
		return( list );
	}
		
		
	/**
	 * Returns a key/value Map for all the attribute name/values for the node by nodeName with
	 * the attribute name/value pairing
	 * <p>
	 * @param 	nodeName
	 * @param	attributeName
	 * @param	attributeValue
	 * @return	a Collection of attribute name/value pairings
	 */
	public Map getAttributesForEachOccuranceBy( String nodeName, String attributeName, String attributeValue )
	{
		Map attributes = new HashMap();
		
		// Retrieve a list of elements that correspond to the provided node name
		NodeList nodes 	= root.getElementsByTagName( nodeName );
		
		if ( nodes != null )
		{
			Node node = null;
	        boolean bFound = false;
			for( int index = 0; index < nodes.getLength() && bFound==false; index++ )
			{
				node =  nodes.item( index );
				
				if ( node != null 
					&& node.getAttributes().getNamedItem(attributeName).getNodeValue().equals( attributeValue ) )
				{
					attributes = getAttributes( node );
					bFound = true; 
				}
			}
		}
		else
		{
			logErrorMessage( "FrameworkXMLParser.getAttributesForEachOccuranceBy() - Node name " + nodeName + " is unable to be located." );
		}
	//	System.out.println("\n\ngetAttributesForEachOccuranceBy - " + attributes );
							
		return( attributes );
	}		
	/**
	 * Returns a Collection of String representations of the values for each node referenced by 
	 * nodeName and the attribute for each referenced by attributeName
	 * <p>
	 * @param 	nodeName
	 * @param   attributeName
	 * @return	a Collection of attribute name/value pairings
	 */
	public Collection getAttributesForEachOccurance( String nodeName, String attributeName )
	{
		ArrayList list = new ArrayList();
		
		// Retrieve a list of elements that correspond to the provided node name
		NodeList nodes 	= root.getElementsByTagName( nodeName );
		
		if ( nodes != null )
		{
			Node node = null;
	        
			for( int index = 0; index < nodes.getLength(); index++ )
			{
				node =  nodes.item( index );
				
				if ( node != null )
				{
					list.add( node.getAttributes().getNamedItem(attributeName).getNodeValue() ); 
				}
			}
		}
		else
		{
			logErrorMessage( "FrameworkXMLParser.getAttributesForEachOccurance() - Node name " + nodeName + " is unable to be located." );
		}
	//	System.out.println("\n\ngetAttributesForEachOccurance - " + list );					
		return( list );
	}		
	/**
	 * Retrieves the attributes from the specified node's children.
	 * <p>
	 * @param   node
	 * @return  HashMap
	 */
	protected Map getAttributes( Node node )
	{
		HashMap attributes 		= new HashMap();
		NamedNodeMap nodeMap 	= null;
		Node childsNode			= null;
		
		if ( node != null )
		{		
			nodeMap 	= node.getAttributes();
			childsNode	= null;
		
			for ( int j = 0; j < nodeMap.getLength(); j++ )
			{
				childsNode = nodeMap.item( j );
			
				if ( childsNode != null )
				{
					attributes.put( childsNode.getNodeName(), childsNode.getNodeValue() );
				}
			}
		}
		
	//	System.out.println("\n\ngetAttributes - " + attributes );		
		return attributes;
	}

	/**
	 * Retrieves the elements from the specified node's children.
	 * <p>
	 * @param   node
	 * @return  Collection
	 */
	protected Collection getElements( Node node )
	{
		ArrayList elements 		= new ArrayList();
		NodeList nodeList 		= null;
		Node childsNode			= null;
		
		if ( node != null )
		{		
			nodeList 	= node.getChildNodes();
			childsNode	= null;
		
			for ( int j = 0; j < nodeList.getLength(); j++ )
			{
				childsNode = nodeList.item( j );
			
				if ( childsNode != null )
				{
					elements.add( childsNode.getNodeValue() );
				}
			}
		}
		
	//	System.out.println("\n\ngetAttributes - " + attributes );		
		return elements;
	}
	
	/**
	 * Retrieves the attributes from the specified node's children.
	 * <p>
	 * @param   node
	 * @return  HashMap
	 */
	protected Map getChildAttributes( Node node )
	{
		HashMap attributes 		= new HashMap();
		NamedNodeMap nodeMap 	= null;
		Node childsNode			= null;
		NodeList children		= null;
		Node child				= null;
		
		if ( node != null )
		{
			children = node.getChildNodes();
			
			for ( int i = 0; i < children.getLength();  i++ )
			{	
				child		= children.item( i );			
				nodeMap 	= child.getAttributes();
				childsNode	= null;
			
				for ( int j = 0; j < nodeMap.getLength(); j++ )
				{
					childsNode = nodeMap.item( j );
				
					if ( childsNode != null )
					{
						attributes.put( childsNode.getNodeName(), childsNode.getNodeValue() );
					}
				}
			} 
		}
		
	//	System.out.println("\n\ngetchildAttributes - " + attributes );		
		return attributes;
	}
	
	/**
	 * Root of the document.
	 * @return		the root of the looaded document, null if none loaded
	 */
	public Element getRoot()
	{
		return ( root );
	}
	
	/**
	 * The document itself.
	 * 
	 * @return		the document
	 */
	public Document getDocument()
	{
		return( document );
	}
	
	/**
	 * Returns the input stream
	 * @return	input stream
	 */
	public InputStream getInputStream()
	{
		return( inputStream );
	}
	
	/**
	 * Closes the contained input stream.
	 */
	public void close()
	{
		if( inputStream != null )
		{
			try
			{		
				inputStream.close();
			}
			catch( IOException exc )
			{
				logErrorMessage( "FrameworkXMLParser.close() - failed to close input stream - " + inputStream.toString() );
			}
		}
	}
	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

	/**
	 * root node of open document
	 */
	private Element root = null;
	
	/**
	 * The document itself
	 */
	private Document document = null;
	
	/**
	 * input stream contain xml content
	 */
	private InputStream inputStream = null;
}

/*
 * Change Log:
 * $Log$
 */
