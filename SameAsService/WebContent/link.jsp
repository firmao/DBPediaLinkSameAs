<%@page import="com.services.sameas.SameAsWS"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	if (request.getParameter("vote") != null) {
		String vote = request.getParameter("vote");
		String suggestion = request.getParameter("suggestion");
		String s = session.getAttribute("subject").toString();
		String p = session.getAttribute("dbpedialink").toString();
		SameAsWS sameAs = new SameAsWS();
		//sameAs.vote(dbpediaURI, vote);
		sameAs.voteSuggestion(p, s, vote, suggestion);
%>
<script>
	window.open("congrat.html");
</script>
<%
	out.close();
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
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
	$(window).load(function() {
		$("#loading").hide();
	})
</script>
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
	<%=" < " + "<a href=" + s + " target=\"content\">" + s
					+ "</a>" + " > "%>
	<%=" < http://owl:SameAs > "%>
	<%=" < " + "<a href=" + p + " target=\"content\">" + p
					+ "</a>" + " > "%>

	<a href="link.jsp?vote=1" class="vote up"><img
		title="vote for this URI as a good address." src="img/vote-down.png"
		alt="+1" /></a>
	<a href="#" class="vote down"><img
		title="vote against this URI because it is incorrect or spam."
		src="img/vote-up.png" alt="-1" onclick="makeSuggestion()" /></a>
	<div id="suggestionDiv" style="display: none;">
		Sugestion:<input id="txtSuggestion" type="text" size="100"
			maxlength="749">
		<button onclick="recordSuggestion()">Send Suggestion</button>
	</div>

	<script>
		function makeSuggestion() {
			//alert("");
			var r = confirm("Would you like to suggest a link ?");
			if (r == true) {
				document.getElementById('suggestionDiv').style.display = "block";
			} else {
				recordSuggestion();
			}
		}
		function recordSuggestion() {
			window.location
					.assign("link.jsp?vote=-1&suggestion="
							+ document.getElementById("txtSuggestion").value);
		}
	</script>

	<br>Go back to begin ?
	<a href="index_ant.jsp">Yes ?</a>
</body>
</html>
