package com.services.sameas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			// con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String sQuery = "Select dbpedia_uri from RAW_SAMEAS where link_target = '"
					+ pURI + "'";
			rs = st.executeQuery(sQuery);

			while (rs.next()) {
				if (pNormalize) {
					sRet = rs.getString(1);
					if (sRet != null)
						break;
				} else
					sRet += "{\"uri\":\"" + rs.getString(1) + "\"},";
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
		
		if(sRet.length() < 2)
			sRet = null;
		return sRet;
	}

	public void vote(String pURI, String pVote) {
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
			String pSql = null;
			if(iVote > 0)
				pSql = "UPDATE RAW_SAMEAS SET r_positive = r_positive +1 WHERE  dbpedia_uri = '"+pURI+"';";
			else
				pSql = "UPDATE RAW_SAMEAS SET r_negative = r_negative + 1 WHERE  dbpedia_uri = '"+pURI+"';";
			
			 Statement stmt = conn.createStatement();
			 stmt.executeUpdate(pSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
