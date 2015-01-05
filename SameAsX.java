package com.sameas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class SameAsX {
	
	/*
	 * User provides an URI and the
	 * API will return the DBpedia URI that is owl:sameAs with the one the user
	 * provided. Example: getSameAsURI("http://rdf.freebase.com/ns/m.015fr");
	 * return: "http://dbpedia.org/resource/Brazil"
	 * @param pURI URI from Freebase
	 * @return 
	 */
	public static String searchURI(String pURI) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(pURI);
		HttpResponse httpResponse = httpclient.execute(request);
		InputStream in = httpResponse.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			int ind = line.indexOf("area.adjectival_form");
			if(ind > 0)
			{
				line = line.split("\"")[1];
				reader.close();
				in.close();
				break;
			}
		}
		Properties properties = new Properties();
		properties.load(new FileInputStream("country.properties"));
		line = properties.getProperty(line);
		return "http://dbpedia.org/resource/" + line;
	}
}
