package com.servlet.sameas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.services.sameas.SameAsWS;

/**
 * Servlet implementation class SameAsServlet
 */
public class SameAsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SameAsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequestAndRespond(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequestAndRespond(request, response);
	}

	private void handleRequestAndRespond(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if(request.getParameter("uri") != null)// Graphical User Interface.
			{
				String uri = request.getParameter("uri");
				uri = uri.replaceAll("123nada", "#").trim();
				request.getSession().setAttribute("subject", uri);
				SameAsWS sameas = new SameAsWS();
				String dbpediaLink = sameas.getSameAsURI(uri,true);
				if(dbpediaLink != null)
				{
					response.sendRedirect("link.jsp?dbpedialink=" + dbpediaLink);
					//response.sendRedirect("link_frame.jsp?dbpedialink=" + dbpediaLink);
				}else
					response.sendRedirect("notfound.jsp");
				//response.getWriter().write(sameas.getSameAsURI(request.getParameter("uri"),true));
			}
			if(request.getParameter("uris") != null)// Just the service on the web.
			{
				String uri = request.getParameter("uris");
				uri = uri.replaceAll("123nada", "#").trim();
				request.getSession().setAttribute("subject", uri);
				SameAsWS sameas = new SameAsWS();
				String dbpediaLink = sameas.getSameAsURI(uri,true);
				if(dbpediaLink != null)
				{
					response.getWriter().write(dbpediaLink);
				}else
					response.sendRedirect("notfound.jsp");
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
