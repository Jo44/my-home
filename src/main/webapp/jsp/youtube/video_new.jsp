<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	int idPlaylist = (int) view.getValueForKey("idPlaylist");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Vidéo YouTube</title>
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
								<i class="fa fa-fw fa-play-circle"></i> Vidéo YouTube
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/youtube_playlists?action=update&idPlaylist=<%=String.valueOf(idPlaylist)%>&order-by=date&dir=desc">Retour</a>
								</div>
							</div>
							<div class="panel-body">
								<%
									if (error != null) {
								%>
								<div class="col-xs-12 col-sm-offset-1 col-sm-10">
									<div class="alert alert-danger panel-error center" role="alert">
										<p>
											<strong>Oops ! </strong><%=error%></p>
									</div>
								</div>
								<%
									}
								%>
								<div class="col-xs-12 marged-top">
									<label for="textRawUrlVideo">URL de la vidéo</label> <br> <input class="col-xs-offset-1 col-xs-10" id="textRawUrlVideo"
										name="rawUrlVideo" type="text" pattern=".{1,50}" title="Entre 1 et 50 caractères"
										placeholder="ex: https://www.youtube.com/watch?v=8AF-Sm8d8yk">
								</div>
								<div class="col-xs-12 marged-top marged-bottom">
									<button onclick="testVideo()" class="btn btn-primary btn-sm btn-perso btn-fix-long marged-left"
										title="Test la vidéo et remplis les champs automatiquement">Tester</button>
									<button onclick="clearFields()" class="btn btn-danger btn-sm btn-perso btn-fix-long marged-right" title="Efface tous les champs">Effacer</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="preview-youtube" class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> Prévisualisation
							</div>
							<div class="panel-body preview-fixed">
								<div id="player"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> Détails de la vidéo
							</div>
							<div class="panel-body">
								<form id="newVideoForm" action="youtube_videos" method="post">
									<input type="hidden" name="action" value="add" required> <input type="hidden" name="idPlaylist"
										value="<%=String.valueOf(idPlaylist)%>">
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textArtist">Artiste</label> <br> <input class="col-xs-offset-2 col-xs-8 col-sm-offset-1 col-sm-10" id="textArtist"
											name="artistVideo" type="text" pattern=".{1,50}" title="Entre 1 et 50 caractères" placeholder="ex: Bigflo & Oli" required>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textTitle">Titre</label> <br> <input class="col-xs-offset-2 col-xs-8 col-sm-offset-1 col-sm-10" id="textTitle"
											name="titleVideo" type="text" pattern=".{1,50}" title="Entre 1 et 50 caractères" placeholder="ex: Dommage" required>
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textUrlVideo">ID YouTube</label> <br> <input class="col-xs-offset-2 col-xs-8 col-sm-offset-2 col-sm-8" id="textUrlVideo"
											name="urlVideo" type="text" pattern=".{11}" title="11 caractères" placeholder="ex: 8AF-Sm8d8yk" required>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textDuration">Durée</label> <br> <input class="col-xs-offset-4 col-xs-4 col-sm-offset-3 col-sm-6" id="textDuration"
											name="durationVideo" type="text" pattern="[0-9]{0,3}[:]{0,1}[0-9]{1,2}[:]{1}[0-9]{2}"
											title="Entre 4 et 12 caractères, au format 'hhh:mm:ss' (min. 'm:ss')" placeholder="ex: 3:22" required>
									</div>
									<div class="col-xs-12 col-sm-12 big-marged-top marged-bottom">
										<button class="btn btn-success btn-sm btn-perso" type="submit">Ajouter la vidéo</button>
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
	<!-- YouTube API -->
	<script src="//www.youtube.com/iframe_api"></script>
	<script src="<%=path%>/js/preview_youtube.js"></script>
</body>
</html>