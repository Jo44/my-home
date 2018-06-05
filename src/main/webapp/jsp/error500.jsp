<%@ page import="fr.my.home.bean.User"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Erreur 500</title>
<link rel="icon" type="image/x-icon" href="<%=path%>/img/favicon.ico" />
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css">
<!-- Custom CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css">
<!-- Custom Fonts -->
<link href="<%=path%>/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css">
<!-- CSS Perso -->
<link href="<%=path%>/css/error.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12 center">
						<h2>Erreur 500</h2>
						<h3>Le serveur a rencontré une erreur :'(</h3>
						<h4><%=request.getAttribute("javax.servlet.forward.request_uri")%></h4>
						<h4>
							<a class="grey-link marged-top" href="<%=path%>/status_inside"><i class="fa fa-fw fa-arrow-circle-right"></i> voir Status</a>
						</h4>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="<%=path%>/js/jquery.js"></script>
	<!-- Bootstrap Core JavaScript -->
	<script src="<%=path%>/js/bootstrap.js"></script>
</body>
</html>