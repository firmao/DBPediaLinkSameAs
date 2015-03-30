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
			//response.getWriter().write("SameAsServletService:");
			if(request.getParameter("uri") != null)
			{
				request.getSession().setAttribute("subject", request.getParameter("uri"));
				SameAsWS sameas = new SameAsWS();
				String dbpediaLink = sameas.getSameAsURI(request.getParameter("uri"),true);
				if(dbpediaLink != null)
					response.sendRedirect("link.jsp?dbpedialink=" + dbpediaLink);
				else
					response.sendRedirect("notfound.jsp");
				//response.getWriter().write(sameas.getSameAsURI(request.getParameter("uri"),true));
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
