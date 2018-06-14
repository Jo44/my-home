<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
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
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css">
<!-- Custom CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css">
<!-- Custom Fonts -->
<link href="<%=path%>/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css">
<!-- CSS Perso -->
<link href="<%=path%>/css/status_inside.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-info-circle"></i> Status
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/status_inside">Actualiser</a>
								</div>
							</div>
							<div class="panel-body">
								<%
									if (error != null) {
								%>
								<div class="alert alert-danger panel-error center" role="alert">
									<p><strong>Oops ! </strong><%=error%></p>
								</div>
								<%
									}
								%>
								<br>
								<div class="col-xs-12 center spaced-vertical">
									<br>
									<p>
										<i class="fa fa-fw fa-database"></i> Base de données :
										<%
										 	if (databaseOnline) {
										 		out.print("<span class='green-font bold'>En ligne</span>");
										 	} else {
										 		out.print("<span class='red-font bold'>Hors ligne</span>");
										 	}
										 %>
									</p>
									<br>
								</div>
								<br>
							</div>
						</div>
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