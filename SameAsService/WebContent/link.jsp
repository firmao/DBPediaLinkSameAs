<%@page import="com.services.sameas.SameAsWS"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	if (request.getParameter("vote") != null) {
		String vote = request.getParameter("vote");
		String dbpediaURI = (String) session
				.getAttribute("dbpedialink");
		SameAsWS sameAs = new SameAsWS();
		sameAs.vote(dbpediaURI, vote);
%>
<script>
	window.open("congrat.html");
</script>
<%
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title property="dc:title">Rate the link</title>
<link rel="stylesheet" href="style.css" type="text/css" />
<script src="jquery.js" type="text/javascript"></script>
<script src="zeroclipboard.js" type="text/javascript"></script>
<script src="script.js" type="text/javascript"></script>
</head>
<body>


	<h1>Rate this link</h1>
	<%
		if (request.getParameter("dbpedialink") != null) {
			session.setAttribute("dbpedialink",
					request.getParameter("dbpedialink"));
		} else
			response.sendRedirect("notfound.jsp");
	String s = session.getAttribute("subject").toString();
	String p = session.getAttribute("dbpedialink").toString();
	%>
	<%=" < " + "<a href=" + s + " >" + s + "</a>" + " > "%>
	<%=" < http://owl:SameAs > "%>
	<%=" < " + "<a href=" + p + " >" + p + "</a>" + " > "%>

	<a href="link.jsp?vote=1" class="vote up"><img
		title="vote for this URI as a good address." src="img/vote-down.png"
		alt="+1" /></a>
	<a href="link.jsp?vote=-1" class="vote down"><img
		title="vote against this URI because it is incorrect or spam."
		src="img/vote-up.png" alt="-1" /></a>

	<br>
	<br>
	<br>Go back to the index ?
	<a href="index.jsp">Yes ?</a>
	<div id="logos">
		<a id="dbpedia-logo" href="http://dbpedia.org/" title="DBpedia"><img
			src="img/dbpedia.jpg" alt="" /></a> <a id="sameas-logo"
			href="http://sameas.org/" title="SameAs"><img
			src="img/sameas.jpg" alt="" /></a>
	</div>
</body>
</html>