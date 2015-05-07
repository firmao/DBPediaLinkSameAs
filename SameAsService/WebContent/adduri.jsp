<%@page import="com.services.sameas.SameAsWS"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	if (request.getParameter("subject") != null) {
		String s = request.getParameter("subject");
		String o = request.getParameter("object");
		SameAsWS sa = new SameAsWS();
		sa.insertLink(s, o);
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
<title property="dc:title">DBpedia SameAs</title>
<link rel="stylesheet" href="style.css" type="text/css" />
<script src="jquery.js" type="text/javascript"></script>
<script src="zeroclipboard.js" type="text/javascript"></script>
<script src="script.js" type="text/javascript"></script>
</head>
<body>
	Your link will be evaluated before the publication.
	<br>
	<br> URI (Subject):
	<input id="txtSubject" value="<%=session.getAttribute("subject")%>"
		type="text" size="60" maxlength="749">
	URI (Object):
	<input id="txtObject" type="text" size="60" maxlength="749">
	<button onclick="recordLink()">Send the link :)</button>
	<script>
		function recordLink() {
			window.location.assign("adduri.jsp?subject="
					+ document.getElementById("txtSubject").value + "&object="
					+ document.getElementById("txtObject").value);
		}
	</script>
</body>
</html>
