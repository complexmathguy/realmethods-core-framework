/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.namespace;

/**
 * Provided as a central location for static names in the system.  Once the application is
 * started, they shouldn't require modification.  
 * <p> 
 * @author    realMethods, Inc.
 */
public class FrameworkNameSpace
{
	/**
	 * SMTP related
	 */
    final public static String SMTP_HOST_NAME = "SMTP_HOST_NAME";
    final public static String SMTP_AUTH_USER = "SMTP_AUTH_USER";
    final public static String SMTP_AUTH_PWD = "SMTP_AUTH_PWD";
    final public static String SMTP_DEBUG = "SMTP_DEBUG";
    final public static String SMTP_PORT = "SMTP_PORT";
    
	
    /**
     * Logged in status 
     */
	final public static String LOGGED_IN = "false";
	
    /*
     * ESB security service identifier
     */
	final public static String framework_security_service = "framework_security_service";
	
    /*
     * ESB security service identifier
     */
	final public static String framework_business_object_notification = "framework_business_object_notification";
		
    /*
     * ESB security service identifier
     */
	final public static String framework_log_service = "framework_log_service";
	
	/*
	 * default ESB manager name
	 */
	final public static String DefaultESBManager = "DefaultESBManager";
	
	/*
	 * name in of the Axis type mapping registry
	 */
	final public static String AxisTypeMappingRegistry  = "AxisTypeMappingRegistry";

	/*
	 * name in cache of multi-selection collections
	 */
	final public static String MULTI_SELECT_COLLECTIONS  = "MULTI_SELECT_COLLECTIONS";

	/*
	 * hibernate configuration file name
	 */
	final public static String HIBERNATE_CONFIG_FILE  = "hibernate.cfg.xml";
		
	/*
	 * framework.property indicator to use internal caching within the dao abstraction
	 */
	final public static String DAO_CACHE_INTERNALLY  = "DAO_CACHE_INTERNALLY";
	
	/**
	 * failure during token validation
	 */		
	final public static String TOKEN_VALIDATION_ERROR  = "TOKEN_VALIDATION_ERROR";

	/**
	 * duplicator form submission error
	 */		
	final public static String DUPLICATE_FORM_SUBMISSION_ERROR  = "DUPLICATE_FORM_SUBMISSION_ERROR";
		
	/**
	 * unique session key for the token
	 */		
	final public static String TOKEN_KEY  = "com.framework.common.namespace.TokenKey";
	/**
	 * framework.xml category for startup class specification
	 */
	final public static String STARTUPS = "FrameworkStartups";
	
	/**
	 * default Logger, used for internal framework logging
	 */
	final public static String FRAMEWORK_LOGGER_NAME  = "com.framework";
	 
	/**
	 * name ascribed to the security related IFramworkStartup implementation
	 */	 
	final public static String SECURITY_PROPERTIES  = "security";
	
	/**
	 * framework.xml property name for the uid generator implementation
	 */
	final public static String FRAMEWORK_UID_GENERATOR = "FrameworkUIDGenerator";
	
	/**
	 * framework.xml property name for the default db conn. name, as
	 * specified in the connectionpool.properties.
	 */
	final public static String DEFAULT_CONNECTION_NAME = "DefaultDatabaseConnectionName";

	/**
	 * framework.xml property name for the default java.sql.Time format.
	 */
	final public static String DEFAULT_TIME_FORMAT = "DefaultTimeFormat";
	
	/**
	 * framework.xml property name for the default java.sql.Date format.
	 */
	final public static String DEFAULT_DATE_FORMAT = "DefaultDateFormat";
	
	/**
	 * framework.xml property name for the default java.sql.Timestamp format.
	 */
	final public static String DEFAULT_TIMESTAMP_FORMAT = "DefaultTimestampFormat";
	
    /**
     * global identifier for the Application MBean Domain
     * default value is "rMFramework"
     */
    public static String MBEAN_DOMAIN = "rMFramework";    

    /**
     * global identifier for the name of the servlet
     */
    public static String SERVLET_NAME = "ServletName";    

    /**
     * global identifier for the type of objects to clear from the cache
     */
    final public static String USOM_CACHE_CLEAR_TYPE = "USOM_CACHE_CLEAR_TYPE";    

    /**
     * global identifier for the MAX num entries to allow in the USOM before refresh
     */
    final public static String USOM_MAX = "USOM_MAX";    

    /**
     * global identifier for the frequency, in milliseconds, to check the USOM size
     */
    final public static String USOM_MAX_CHECK_PERIOD_IN_MILLIS = "USOM_MAX_CHECK_PERIOD_IN_MILLIS";    
	
    /**
     * global identifier for the JMX Server to Register MBeans with
     */
    final public static String JMX_MBEAN_SERVER_IMPLEMENTATION = "JMX_MBEAN_SERVER_IMPLEMENTATION";    

    /**
     * global identifier for the FrameworkEvents being enabled
     */
    final public static String FRAMEWORK_EVENTS_ENABLED = "FrameworkEventsEnabled";    


    /**
     * global identifier for the SOAP Server URI
     */
    final public static String SOAP_SERVER_URI = "SOAPServerURI";    
    
    /**
     * global identifier for the Locale in Use
     */
    final public static String USER_LOCALE = "realMethods_User_Locale";
    
    /**
     * global named used for the Session variable of IFrameworkCache type
     */
    final public static String APPLICATION_CACHE_NAME = "UserSessionObjectManagerClassName";                

    
    /**
     * global for the name of the connection pool entry for the JMS related Topic
     * designated for UserSessionAdministration and UserSession listening
     */
    final public static String JMX_SELF_REGISTRATION = "JMXSelfRegistration";                

    /**
     * global for the name of the connection pool entry for the JMS related Topic
     * designated for UserSessionAdministration and UserSession listening
     */
    final public static String USER_SESSION_ADMIN_JMS = "UserSessionAdminJMS";    

    /**
     * global for the Framework's default Log Task Handler
     */
    final public static String DEFAULT_LOG_TASK_NAME = "FrameworkLogTask";
    
    /**
     * global for the Framework's Logging mechanism
     */
    final public static String DEFAULT_LOG_HANDLER_NAME = "DEFAULT_LOG_HANDLER_NAME";

    /**
     * global for the Framework's TaskMessageDriveBeanWrapper in use indicator
     */
    final public static String TASK_MDB_WRAPPER_IN_USE = "TASK_MDB_WRAPPER_IN_USE";

    /**
     * global for the Framework's Log4J FrameworkCategory configuration parameters
     */
    final public static String ROOT_LOG4J_CATEGORY = "Framework Root Category";    

    /**
     * global for the Framework's Log4J default configuration parameters
     */
    final public static String DEFAULT_LOG4J_CONFIGURATION_FILE_PREFIX = "log4j";    

    /**
     * global for the Framework's JNDI EJB name prefix 
     */
    final public static String JNDI_EJB_NAME_PREFIX = "JNDIEJBNamePrefix";

    /**
     * global for the Framework's JNDI related args
     */
    final public static String JNDI_ARGS= "JNDI_ARGS";

    /**
     * global default LDAP Connection Name to use when
     * performing LDAP related actions, such as authentication, if applicable
     */
    final public static String DEFAULT_LDAP_CONNECTION_NAME = "DEFAULT_LDAP_CONNECTION_NAME";    
    
    /**
     * global for the Framework's connectionpool template file
     */
    final public static String CONNECTION_POOL_TEMPLATE = "connectionpool.template";
    
    /**
     * Framework's common property file  
     */
    final public static String FRAMEWORK_PROPERTIES_FILE = "framework.xml";
    
    /**
     * JNDI name reference for the Framework's properties
     */
    final public static String FRAMEWORK_PROPERTIES = "framework";
    
    /**
     * global connection pool properties file
     */
    final public static String CONNECTION_PROPERTIES_FILE_NAME = "connectionpool.properties";
    
    /**
     * global task related properties file
     */
    final public static String TASK_PROPERTIES_FILE_NAME = "task.properties";

    /**
     * global log related properties file
     */
    final public static String LOG_PROPERTIES_FILE_NAME = "log.properties";

    /**
     * global security related properties file
     */
    final public static String SECURITY_PROPERTIES_FILE_NAME = "security.properties";
    
    /**
     * global name for the FrameworkLogHandler
     */
    final public static String FRAMEWORK_LOG_HANDLER = "logHandler";
    
    /**
     * global name of the Framework as an entity
     */
    final public static String FRAMEWORK_NAME = "realMethodsFramework";
	
    /**
     * global name of the Framework property UserSessionObjectManager class to dynamically load
     */
    final public static String USOM_CLASS_NAME = "UserSessionObjectManagerClassName";
        
    /**
     * global name for the application identifier
     */
    final public static String APPLICATION_ID_TAG = "ApplicationID";
    
    /**
     * global JMS Queue Factory related parameters
     */
    final public static String JMS_QUEUE_FACTORY = "JMS_QUEUE_FACTORY";
    
    /**
     * global JMS Topic Factory related parameters
     */
    final public static String JMS_TOPIC_FACTORY = "JMS_TOPIC_FACTORY";
        
    /**
     * global name of the Framework Event JMS Queue
     *
     */
     final public static String EVENT_QUEUE_NAME = "FrameworkEventJMS";
     
    /**
     * global name of the Framework Task Message JMS Queue
     *
     */
     final public static String TASK_EXECUTION_QUEUE = "FrameworkTaskExecutionJMS";
     
     /**
      * global indicator for value object notification
      */
    final public static String VALUE_OBJECT_NOTIFICATION = "ValueObjectNotification";    
    final public static String FRAMEWORK_VALUE_OBJECT_NOTIFICATION_JMS = "FrameworkValueObjectNotificationJMS";
    
    /**
     * global name of the Framework License File
     *
     */
    final public static String LICENSE_FILE_NAME = "framework.license";
     
    /**
     * global name for the Framework Hook Manager
     *
     */
     final public static String FRAMEWORK_HOOK_MANAGER = "Hooks";
         
    /**
     * name ascribed to this instance of the Framework.  use the -D argument during
     * the execution of your application. Use the
     * -D parameter when running your application and provide any name,
     * unique if desired.  Uniqueness becomes important for the purpose
     * of having different property settings per instance of the Framework
     * in a clustered environment
     *
     * usage -DFrameworkInstance=MyInstance
     */
    public static String INSTANCE_NAME = "DefaultInstance";
    
    /**
     * home name of this instance of the Framework. Will normally be in the form of a directory
     * structure.  use the -D argument during the execution of your application so the framework.license
     * can be located.
     *
     * usage: -DFRAMEWORK_HOME=c:/myapplication/realmethods/home
     */
    public static String FRAMEWORK_HOME = java.lang.System.getProperty( "FRAMEWORK_HOME" ) != null ? java.lang.System.getProperty( "FRAMEWORK_HOME" ).replace( '\\', java.io.File.separatorChar ) : ".";

    /**
     * name of the framework license file to validate. 
     * If not provided, framework.license is used.  Used in conjunctino with FRAMEWORK_HOME
     *
     * usage: -DFRAMEWORK_LICENSEFILE_NAME=myframework.license
     */
    public static String FRAMEWORK_LICENSEFILE_NAME = java.lang.System.getProperty( "FRAMEWORK_LICENSEFILE_NAME" ) != null ? java.lang.System.getProperty( "FRAMEWORK_LICENSEFILE_NAME" ).replace( '\\', java.io.File.separatorChar ) : "framework.license";

    public static boolean GOOGLE_LICENSE_ENV = java.lang.System.getProperty( "google.license.env" ) != null ? true : false;    

    /**
     * Global location of the framework related property files
     */
    public static String FRAMEWORK_PROPERTIES_LOCATION = null;
    public static boolean USING_JDO = java.lang.System.getProperty( "USING_JDO" ) != null ? true : false;

    public static boolean USING_HIBERNATE = java.lang.System.getProperty( "USING_HIBERNATE" ) != null || USING_JDO == false ? true : false;
	/**
	 * current framework version
	 */
	public static final String version = "6.0";
	
	public static final String versionSuffix 	= "";
	
	    
    /**
     * default constructor - deter instantiation
     */
    protected FrameworkNameSpace()
    {}  
     
}

/*
 * Change Log:
 * $Log: FrameworkNameSpace.java,v $
 */


