package com.services.sameas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.naming.*;
import javax.sql.*;

public class SameAsWS {

	/*
	 * User provides an URI and the API will return the DBpedia URI that is
	 * owl:sameAs with the one the user provided. Example:
	 * getSameAsURI("http://rdf.freebase.com/ns/m.015fr"); return:
	 * "http://dbpedia.org/resource/Brazil"
	 * 
	 * @param pURI URI from Freebase
	 * 
	 * @return
	 */
	public String getSameAsURI(String pURI, boolean pNormalize) {
		long startTime = System.currentTimeMillis();
		String sRet = "[";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");

			if (ds != null) {
				con = ds.getConnection();
			}

			st = con.createStatement();
			String sQuery = "Select dbpedia_uri from RAW_SAMEAS where link_target = '"
					+ pURI + "'";
			// String sQuery = "CALL spSelect('"+pURI+"');";
			// String sQuery =
			// "CALL spSelect('http://www.bbc.co.uk/nature/species/Green_Woodpecker#species');";

			rs = st.executeQuery(sQuery);

			// String sQuery =
			// "Select dbpedia_uri from RAW_SAMEAS where link_target = ?";
			// PreparedStatement preparedStatement =
			// con.prepareStatement(sQuery);
			// preparedStatement.setObject(1, pURI);
			// rs = preparedStatement.executeQuery(sQuery);

			String res = null;
			boolean bInternet = isConnected();
			while (rs.next()) {
				res = getRealURI(rs.getString(1), bInternet);

				if (pNormalize) {
					sRet = res;
					if (sRet != null)
						break;
				} else
					sRet += "{\"uri\":\"" + res + "\"},";
			}

		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(SameAsWS.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(SameAsWS.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		if (!pNormalize)
			sRet = sRet.substring(0, sRet.length() - 1) + "]";

		if (sRet.length() < 2) {
			// sRet = secondTry(pURI, pNormalize);
			sRet = null;
		}

		System.out.println("Total time [seconds]: "
				+ (System.currentTimeMillis() - startTime) * 0.001);

		return sRet;
	}

	/*
	 * private String secondTry(String pURI, boolean pNormalize) { String sRet =
	 * "["; Connection con = null; Statement st = null; ResultSet rs = null;
	 * System.out.println("Going to second try, table RAW_SAMEAS"); try {
	 * 
	 * Context ctx = new InitialContext(); DataSource ds = (DataSource)
	 * ctx.lookup("java:comp/env/jdbc/mysql");
	 * 
	 * if (ds != null) { con = ds.getConnection(); } // con =
	 * DriverManager.getConnection(url, user, password); st =
	 * con.createStatement(); String sQuery =
	 * "Select dbpedia_uri from RAW_SAMEAS where link_target = '" + pURI + "'";
	 * rs = st.executeQuery(sQuery); String res = null; boolean bInternet =
	 * isConnected(); while (rs.next()) { res =
	 * getRealURI(rs.getString(1),bInternet);
	 * 
	 * if (pNormalize) { sRet = res; if (sRet != null) break; } else sRet +=
	 * "{\"uri\":\"" + res + "\"},"; }
	 * 
	 * } catch (Exception ex) { Logger lgr =
	 * Logger.getLogger(SameAsWS.class.getName()); lgr.log(Level.SEVERE,
	 * ex.getMessage(), ex);
	 * 
	 * } finally { try { if (rs != null) { rs.close(); } if (st != null) {
	 * st.close(); } if (con != null) { con.close(); }
	 * 
	 * } catch (SQLException ex) { Logger lgr =
	 * Logger.getLogger(SameAsWS.class.getName()); lgr.log(Level.WARNING,
	 * ex.getMessage(), ex); } } if (!pNormalize) sRet = sRet.substring(0,
	 * sRet.length() - 1) + "]";
	 * 
	 * if (sRet.length() < 2) sRet = null; return sRet; }
	 */

	/*
	 * Just to check if there are internet connection
	 */
	private boolean isConnected() {
		boolean ret = false;
		try {
			URL url = new URL("http://dbpedia.org/resource/ISO_3166-1:BR");
			InputStream is = url.openConnection().getInputStream();
			is.close();
			ret = true;
		} catch (Exception e) {
			ret = false;
		}

		return ret;
	}

	private String getRealURI(String pURI, boolean pIsOnline) {
		String ret = pURI;
		try {
			if (pIsOnline) {
				URL url = new URL(pURI);

				InputStream is = url.openConnection().getInputStream();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("rel=\"foaf:primarytopic") > 0) {
						int ind = line.indexOf("href");
						ret = line.substring(ind + 6, line.length() - 3);
						break;
					}
				}
				reader.close();
			} else {
				// Get from LocalDataBase
				ret = getRedirect(pURI);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/*
	 * public void vote(String pURI, String pVote) { // Insert the vote at the
	 * DataBase. int iVote = Integer.valueOf(pVote).intValue(); Connection conn
	 * = null; Context ctx; try { ctx = new InitialContext();
	 * 
	 * DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");
	 * 
	 * if (ds != null) { conn = ds.getConnection(); } String pSql = null; if
	 * (iVote > 0) pSql =
	 * "UPDATE RAW_SAMEAS SET r_positive = r_positive +1 WHERE  dbpedia_uri = '"
	 * + pURI + "';"; else pSql =
	 * "UPDATE RAW_SAMEAS SET r_negative = r_negative + 1 WHERE  dbpedia_uri = '"
	 * + pURI + "';";
	 * 
	 * Statement stmt = conn.createStatement(); stmt.executeUpdate(pSql); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

	public void voteSuggestion(String pURI1, String pURI2, String pVote,
			String pSuggestion) {
		// Insert the vote at the DataBase.
		int iVote = Integer.valueOf(pVote).intValue();
		Connection conn = null;
		Context ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");

			if (ds != null) {
				conn = ds.getConnection();
			}
			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO VOTE (dbpedia_uri, link_target, vote, suggestion) VALUES (?, ?, ?, ?);");
			prep.setString(1, pURI1);
			prep.setString(2, pURI2);
			prep.setInt(3, iVote);
			prep.setString(4, pSuggestion);

			prep.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getRedirect(String pURI) {
		String sRet = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");

			if (ds != null) {
				con = ds.getConnection();
			}
			// con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String sQuery = "Select to_uri from REDIRECT where from = '" + pURI
					+ "'";
			rs = st.executeQuery(sQuery);
			String res = null;
			while (rs.next()) {
				res = rs.getString(1);
				sRet = res;
			}

		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(SameAsWS.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(SameAsWS.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		if (sRet.length() < 2)
			sRet = null;

		return sRet;
	}

	public void insertLink(String pSubject, String pObject) {
		Connection conn = null;
		Context ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");

			if (ds != null) {
				conn = ds.getConnection();
			}
			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO NEWLINKS (dbpedia_uri, link_target) VALUES (?, ?);");
			prep.setString(1, pObject);
			prep.setString(2, pSubject);

			prep.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
