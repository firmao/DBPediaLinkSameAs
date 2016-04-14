package com.services.sameas;

import java.util.Map;

public class MyUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(myReplace("http://www.rewq.com/resource/ddd#fdsf$fdsg,fff&gfd*gfd(gfdsds'fdsa'fdddd", "'", "\\\\'"));
	}

	public static String myReplace(String pURI, String pRegex, String pReplace) {
		// TODO Auto-generated method stub
		String sRet = null;
		sRet = pURI.replaceAll(pRegex, pReplace);
		return sRet;
	}
	
	public static String formatOut(String ret, int pEp, int pEv) {
		//String sRet = ret + "<br>"
		String sRet = "<style type=\"text/css\">"
				+ ".tg  {border-collapse:collapse;border-spacing:0;}"
				+ ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}"
				+ ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}"
				+ ".tg .tg-3fma{color:#fd6864}"
				+ "</style>"
				+ "<table class=\"tg\">"
				+ "<tr>"
				+ "<th class=\"tg-031e\">Property</th>"
				+ "<th class=\"tg-031e\">Value</th>"
				+ "</tr>";
		for (String kS : JenaSparql.mMatching.keySet()) {
			sRet = sRet + "<tr>  "
					+ "<td class=\"tg-3fma\"><a href='"+kS.substring(2)+"'> "+kS.substring(2)+" </a></td>"
					+ "<td class=\"tg-3fma\"><a href='"+JenaSparql.mMatching.get(kS)+"'> "+ printMatch(JenaSparql.mMatching.get(kS))+" </a></td>"
					+ "</tr>";
			
		}
		sRet = sRet + "</table>" + ret;
		
		return sRet;
	}

	private static String printMatch(String pValue) {
		String sRet = null;
		if (JenaSparql.lstEV.contains(pValue))
			sRet = "=" +pValue;
		else
			sRet = pValue;
		
		return sRet;
	}

}
