package com.mhealth.googleapi;

import java.io.InputStreamReader;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

/**
 * Hello world!
 *
 */
public class OAuthorizer 
{
	
	/** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;
	  
	  private static final java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"), ".store/oAuth_sample");

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	  
	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;
	  
    public static void main( String[] args )
    {
        System.out.println( "Starting oAuth2.0 Authorization" );
        try {
        	 // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // initialize the data store factory
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			authorize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static Credential authorize() throws Exception {
    	System.out.println(System.getProperty("user.home"));
	    // load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(OAuthorizer.class.getResourceAsStream("/client_secrets.json")));
	    
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(CalendarScopes.CALENDAR_READONLY)).setDataStoreFactory(dataStoreFactory)
	        .build();
	    // authorize, Starting local jetty http server at 55667 port, make sure you specify
	    //http://localhost:55667/Callback as a callback url
	    //
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(55667).build()).authorize("user");
	  }
}
