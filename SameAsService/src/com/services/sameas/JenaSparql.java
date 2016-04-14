package com.services.sameas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.riot.WebContent;

import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class JenaSparql {
	public static int ep=0, ev=0;
	public static Map<String, String> mMatching = new HashMap<String, String>();
	public static List<String> lstEV = new ArrayList<String>();
	public static List<String> getJenaSparql(String pURI) {
		List<String> rLst = new ArrayList<String>();
		
		
		String service = "http://dbpedia.org/sparql";
		
		String squery = "prefix owl: <http://www.w3.org/2002/07/owl#> "
				+ "SELECT ?obj WHERE {"
				+ "<"+pURI+"> (owl:sameAs|^owl:sameAs) ?obj}";
		
		//String squery = "select ?property ?value where { <http://dbpedia.org/resource/Brazil> ?property ?value "
		//		+ "filter (?property=<http://www.w3.org/2000/01/rdf-schema#label>) "
		//		+ "}";
		
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
	
	public static Map<String, String> getPropValue(String pURI) {
		HashMap<String, String> mRet = new HashMap<String, String>();

		String service = "http://lod.openlinksw.com/sparql";
		// String query = "ASK { }";
		String squery = "select ?property ?value where { <" + pURI
				+ "> ?property ?value}";

		// Query query = QueryFactory.create(squery);
		// QueryExecution qexec = QueryExecutionFactory.sparqlService(service,
		// query);

		QueryEngineHTTP qexec = (QueryEngineHTTP) QueryExecutionFactory
				.sparqlService(service, squery);
		// request JSON results
		qexec.setSelectContentType(WebContent.contentTypeResultsJSON);

		// QueryExecution qe = QueryExecutionFactory.sparqlService(service,
		// squery);
		int i = 0;
		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				// System.out.println(soln.toString());
				String p = soln.get("property").toString();
				String v = soln.get("value").toString();
				mRet.put((i++) + ":" + p, v);
				// System.out.println(p + " = " + v);
			}

		} catch (QueryExceptionHTTP e) {
			System.out.println(service + " is DOWN");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			qexec.close();
		}

		return mRet;
	}

	/*
	 * Implement a service to get the similarity level between S and O. select
	 * ?property ?value where { <http://dbpedia.org/resource/Brazil> ?property
	 * ?value } SPARQL endpoints (complete): http://lod.openlinksw.com/sparql,
	 * http://uriburner.com/sparql and http://factforge.net/sparql (i.e. DBpedia
	 * endpoint does not have information about all properties and values from a
	 * freebase or nytimes URI). List of “Alive” SPARQL endpoints:
	 * http://www.w3.org/wiki/SparqlEndpoints
	 */
	public static long getSimilarityLevel(String pUriS, String pUriO) {
		long startTime = System.currentTimeMillis();
		long iRes = 0;
		Map<String, String> mS = getPropValue(pUriS);
		Map<String, String> mO = getPropValue(pUriO);

		int iSizeS = mS.size();
		int iSizeO = mO.size();
		int diff = iSizeS - iSizeO;
		if (diff < 0)
			diff = iSizeO - iSizeS;
		int iTotal = iSizeO;
		if (iSizeO < iSizeS)
			iTotal = iSizeS;

		int NEP = 0;
		int NEV = 0;

		for (String kS : mS.keySet()) {
			for (String kO : mO.keySet()) {
				if (kS.equalsIgnoreCase(kO)) {
					NEP++;
					System.out.println("NEP.Name=" + kS);

					for (String vO : mO.values()) {
						if (mS.get(kS).equalsIgnoreCase(vO))
						{
							NEV++;
							System.out
									.println("NEV.value=" + mS.get(kS));
						}
					}
				}
			}
		}
		
		System.out.println("Number of Equal Properties=" + NEP);
		System.out.println("Number of Equal Values=" + NEV);
		System.out.println("Total=" + iTotal);
		
		System.out.println("Total time [seconds]: "
				+ (System.currentTimeMillis() - startTime) * 0.001);
		
		iRes = (100 * NEV) / NEP;
		System.out.println("Similarity Level=" + iRes + "%");
		return iRes;

	}
	
	/* Get the Endpoint in Domain Level.
	 * i.e. getEndPoint(http://es.dbpedia.org/resource/Brazil)
	 * @returns http://es.dbpedia.org/sparql
	 */
	public static String getEndPoint(String pURI) {
		String sRet = null;
		String [] sDomain = pURI.split("/");
		sRet = "http://" + sDomain[2].trim() + "/sparql";
		
		return sRet;
	}
	
	public static String getSimilarityLevel(String s, String endS,
			String o, String endO) {
		String ret = null;
		Map<String, String> mS = getPropValue(s, endS);
		Map<String, String> mO = getPropValue(o, endO);
		if(mS.size() < 1 || mO.size() < 1)
			return "Problem with endPoint, not possible to get the properties";
		
		double d = getPropComparison(mS, mO);
		
		if(d > 0.0){
			ret = "The Similary Level is: " + d + "%  <br> EqualProperties=" + ep + " EqualValues=" + ev;
			ret = MyUtil.formatOut(ret, ep, ev) + ". Total properties Sub: " + mS.size() + " Total properties Obj: " + mO.size();
		}
			else
			ret = "There is no Similarity between S and O";
		return ret;
	}
	
	

	public static Map<String, String> getPropValue(String pURI, String pEndPoint) {

		HashMap<String, String> mRet = new HashMap<String, String>();

		QueryEngineHTTP qexec = null;
		try {

			// String query = "ASK { }";
			String squery = "select ?property ?value where { <" + pURI
					+ "> ?property ?value}";
			System.out.println(pEndPoint);
			System.out.println(squery);

			boolean bErr=true;

			while (bErr) {
				try {
					bErr = false;
					qexec = (QueryEngineHTTP) QueryExecutionFactory
							.sparqlService(pEndPoint, squery);
					// request JSON results
					qexec.setSelectContentType(WebContent.contentTypeResultsJSON);
				} catch (QueryExceptionHTTP e) {
					bErr=true;
					System.err.println(pEndPoint + " is DOWN");
				}
				
			}
			// QueryExecution qe = QueryExecutionFactory.sparqlService(service,
			// squery);
			int i = 0;

			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				// System.out.println(soln.toString());
				String p = soln.get("property").toString();
				String v = soln.get("value").toString();
				mRet.put((i++) + ":" + p, v);
				// System.out.println(p + " = " + v);
			}

		} catch (QueryExceptionHTTP e) {
			System.err.println(pEndPoint + " is DOWN");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (qexec != null)
				qexec.close();
		}

		return mRet;
	}
	
	private static double getPropComparison(Map<String, String> mS,
			Map<String, String> mO) {
		double dRet = 0.0;
		int iSizeS = mS.size();
		int iSizeO = mO.size();
		int diff = iSizeS - iSizeO;
		if (diff < 0)
			diff = iSizeO - iSizeS;
		int iTotal = iSizeO;
		if (iSizeO < iSizeS)
			iTotal = iSizeS;

		int NEP = 0;
		int NEV = 0;

		for (String kS : mS.keySet()) {
			for (String kO : mO.keySet()) {
				if (kS.equalsIgnoreCase(kO)) {
					NEP++;
					// System.out.println("CountPropEq=" + cProEq + " KeySub=" +
					// kS + " KeyObj=" + kO);
					

					for (String vO : mO.values()) {
						if (mS.get(kS).equalsIgnoreCase(vO)) {
							NEV++;
							// System.out
							// .println("CountValEq=" + cValEq + " ValueSub=" +
							// mS.get(kS) + " ValueObj=" + vO);
							System.out.println("Equal property=" + kS);
							System.out
							.println("Equal value=" + mS.get(kS));
							lstEV.add(mS.get(kS));
							mMatching.put(kS, mS.get(kS));
						}
					}
				}
			}
		}
		
		if (NEP > 0) {
			ep = NEP;
			ev = NEV;
			System.out.println("Number of Equal Properties=" + NEP);
			System.out.println("Number of Equal Values=" + NEV);
			System.out.println("TotalPropertiesS=" + iSizeS + " - TotalPropertiesO=" + iSizeO);

			// iRes = (100 * NEV) / NEP;
			// =((((F425-C425)*(D425/F425))/F425)*100)
			//dRet = (((((double)iTotal - (double)NEP) * ((double)NEV / (double)iTotal)) / (double)iTotal) * 100.0);
			dRet = ((100.0 * (double)NEV) / (double)NEP);
		}
		return dRet;
	}
	
	public static void main(String args[])
	{
		System.out.println(getJenaSparql("http://purl.org/collections/nl/am/p-3317"));
	}
}
