package com.services.sameas;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.riot.WebContent;

import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class JenaSparql {
	public static List<String> getJenaSparql(String pURI) {
		List<String> rLst = new ArrayList<String>();
		
		
		String service = "http://dbpedia.org/sparql";
		
		String squery = "prefix owl: <http://www.w3.org/2002/07/owl#> "
				+ "SELECT ?obj WHERE {"
				+ "<"+pURI+"> (owl:sameAs|^owl:sameAs) ?obj}";
		
		QueryEngineHTTP qexec = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(service, squery);

		qexec.setSelectContentType(WebContent.contentTypeResultsJSON);
		
		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				int ind1 = soln.toString().indexOf("<") +1;
				int ind2 = soln.toString().indexOf(">");
				String sURI = soln.toString().substring(ind1, ind2);
				rLst.add(sURI);
				//System.out.println(soln.toString());
				// String x = soln.get("Concept").toString();
				// System.out.print(x + "\n");
			}

		} catch (QueryExceptionHTTP e) {
			System.out.println(service + " is DOWN");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			qexec.close();
		}
		
		return rLst;
	}
	
	public static void main(String args[])
	{
		System.out.println(getJenaSparql("http://purl.org/collections/nl/am/p-3317"));
	}
}
