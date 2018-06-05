<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	boolean databaseOnline = (boolean) view.getValueForKey("databaseOnline");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Status</title>
<link rel="icon" type="image/x-icon" href="<%=path%>/img/favicon.ico" />
</head>
<body>
	<!-- CONTENT -->
	<br> ******************************************************************
	<br> **************************&nbsp;&nbsp;&nbsp;&nbsp;MY HOME&nbsp;&nbsp;&nbsp;&nbsp;**************************
	<br> ******************************************************************
	<br>
	<br> &nbsp;&nbsp;&nbsp;&nbsp;Status de Tomcat : ONLINE
	<br>
	<br> &nbsp;&nbsp;&nbsp;&nbsp;Status de Hibernate :
	<%
	 	if (databaseOnline) {
	 		out.print("ONLINE");
	 	} else {
	 		out.print("OFFLINE");
	 	}
 	%>
 	<br>
	<br>
	<!-- END CONTENT -->
</body>
</html>