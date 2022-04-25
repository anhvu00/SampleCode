package com.kyron.BouncyCastleDemo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Get application properties values
 * @author anh
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DemoSSLConfig {
	
	@Value( "${bc.rootCN:root_CA_default}" )
	private String rootCN;
	
	@Value( "${bc.notdefined:default_value}" )
	private String defvalue;
	
	@Value( "${bc.host:localhost}" )
	private String host;
	
	@Value( "${bc.port:8443}" )
	private String port;
	
	@Value( "${bc.serverName:server}" )
	private String serverName;
	
	@Value( "${bc.serverPassword:changeit}" )
	private String serverPassword;
    private char[] serverPasswordAry;
	
	@Value( "${bc.clientName:client}" )
	private String clientName;
	
	@Value( "${bc.clientPassword:changeit}" )
	private String clientPassword;
    private char[] clientPasswordAry;
	
	@Value( "${bc.trustStoreName:trustStore}" )
	private String trustStoreName;
	
	@Value( "${bc.trustStoreType:PKCS12}" )
	private String trustStoreType;
	
	@Value( "${bc.trustStorePassword:changeit}" )
	private String trustStorePassword;
    private char[] trustStorePasswordAry;
	
	@Value( "${bc.keyPassword:changeit}" )
	private String keyPassword;	
    private char[] keyPasswordAry;
    
	@Value( "${bc.keyStoreType:PKCS12}" )
	private String keyStoreType;
	
	private String trustStoreFileExt;
	private String keyStoreFileExt;
	
    @PostConstruct
    public void init() {
    	// transform passwords to char arrays
    	keyPasswordAry = keyPassword.toCharArray();
    	serverPasswordAry = serverPassword.toCharArray();
    	clientPasswordAry = clientPassword.toCharArray();
    	trustStorePasswordAry = trustStorePassword.toCharArray();
    	trustStoreFileExt = ((trustStoreType.equalsIgnoreCase("PKCS12")) ? ".p12" : ".jks");
    	keyStoreFileExt = ((keyStoreType.equalsIgnoreCase("PKCS12")) ? ".p12" : ".jks");
    }
    

	public String getRootCN() {
		return rootCN;
	}


	public String getDefvalue() {
		return defvalue;
	}


	public String getHost() {
		return host;
	}


	public String getPort() {
		return port;
	}


	public String getServerName() {
		return serverName;
	}


	public String getServerPassword() {
		return serverPassword;
	}


	public char[] getServerPasswordAry() {
		return serverPasswordAry;
	}


	public String getClientName() {
		return clientName;
	}


	public String getClientPassword() {
		return clientPassword;
	}


	public char[] getClientPasswordAry() {
		return clientPasswordAry;
	}


	public String getTrustStoreName() {
		return trustStoreName;
	}


	public String getTrustStorePassword() {
		return trustStorePassword;
	}


	public char[] getTrustStorePasswordAry() {
		return trustStorePasswordAry;
	}


	public String getKeyPassword() {
		return keyPassword;
	}


	public char[] getKeyPasswordAry() {
		return keyPasswordAry;
	}


	public String getTrustStoreType() {
		return trustStoreType;
	}


	public String getKeyStoreType() {
		return keyStoreType;
	}


	public String getTrustStoreFileExt() {
		return trustStoreFileExt;
	}


	public String getKeyStoreFileExt() {
		return keyStoreFileExt;
	}


}
