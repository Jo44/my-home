<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Playlist YouTube</title>
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
<link href="<%=path%>/css/youtube.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> Playlist YouTube
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/youtube_playlists?action=list&order-by=date&dir=desc">Retour</a>
								</div>
							</div>
							<div class="panel-body">
								<%
									if (error != null) {
								%>
								<div class="alert alert-danger panel-error center big-marged-top" role="alert">
									<p>
										<strong>Oops ! </strong><%=error%></p>
								</div>
								<%
									}
								%>
								<form id="newPlaylistForm" action="youtube_playlists" method="post">
									<input type="hidden" name="action" value="add" required> <br>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textTitle">Titre de la playlist</label> <br> <input id="textTitle" name="titlePlaylist" type="text" pattern=".{1,50}"
											title="Entre 1 et 50 caractères" autofocus required>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textType">Type de la playlist</label> <br> <input id="textType" name="typePlaylist" type="text" pattern=".{1,50}"
											title="Entre 1 et 50 caractères" required>
									</div>
									<div class="col-xs-12 col-sm-12">
										<br>
										<p>Une fois enregistrée, vous devrez modifier votre playlist pour ajouter des vidéos</p>
									</div>
									<div class="col-xs-12 col-sm-12 spaced-vertical">
										<button class="btn btn-success btn-sm btn-perso" type="submit">Enregistrer</button>
									</div>
								</form>
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