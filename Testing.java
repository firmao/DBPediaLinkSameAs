package com.sameas;

public class Testing {

	public static void main(String args[]) {
		try {
			SameAsX sameAs = new SameAsX();
			String uriBrasil = "http://rdf.freebase.com/ns/m.015fr";
			String uriSweden = "http://rdf.freebase.com/ns/m/0d0vqn";
			String uriScotland = "http://rdf.freebase.com/ns/m/06q1r";
			//System.out.println("out: " + sameAs.getSameAsURI(uri));
			System.out.println(uriBrasil + "<owl:sameAs>" + sameAs.searchURI(uriBrasil));
			System.out.println(uriSweden + "<owl:sameAs>" + sameAs.searchURI(uriSweden));
			System.out.println(uriScotland + "<owl:sameAs>" + sameAs.searchURI(uriScotland));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
