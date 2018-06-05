<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Profil</title>
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
<link href="<%=path%>/css/profil.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-user"></i> Mon profil
								<div class="pull-right">
									<div class="btn-group">
										<a class="btn btn-warning btn-xs btn-perso" href="<%=path%>/profil?action=update">Modifier</a> <a class="btn btn-primary btn-xs btn-perso"
											href="<%=path%>/home">Retour</a>
									</div>
								</div>
							</div>
							<div class="panel-body">
								<%
									if (error != null) {
								%>
								<div class="alert alert-danger panel-error center" role="alert">
									<p>
										<strong>Oops ! </strong><%=error%></p>
								</div>
								<%
									} else if (success != null) {
								%>
								<div class="alert alert-success panel-error center marged-top" role="alert">
									<p><%=success%></p>
								</div>
								<%
									}
								%>
								<div class="row">
									<div class="col-xs-offset-3 col-xs-2">
										<span class="bold">Login</span>
									</div>
									<div class="col-xs-6">
										<span><%=user.getName()%></span>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-offset-3 col-xs-2">
										<span class="bold">Password</span>
									</div>
									<div class="col-xs-6">
										<span>************</span>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-offset-3 col-xs-2">
										<span class="bold">Email</span>
									</div>
									<div class="col-xs-6">
										<span><%=user.getEmail()%></span>
									</div>
								</div>
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