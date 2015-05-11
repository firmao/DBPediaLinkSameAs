<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<h1>Provide your URI</h1>
	<input type="text" id="uri" size="80"><button onclick="callService()">Get DBpedia URI</button>
	<br>Example: http://rdf.freebase.com/ns/m.015fr
	<script>
		function callService() {
			var url = document.getElementById("uri").value.replace("#", "123nada");
			window.location
					.assign("SameAsServlet?uri="
							+ url);
		}
	</script>
</body>
</html>
