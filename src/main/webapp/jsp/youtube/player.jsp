<%@ page import="java.util.*"%>

<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>
<%@ page import="fr.my.home.bean.YouTubeVideo"%>

<%
    String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	List<YouTubeVideo> listVideo = (List<YouTubeVideo>) view.getValueForKey("listVideo");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | YouTube Player</title>
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
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div id="panelDark" class="panel panel-default center-div">
							<div id="panelHeadingDark" class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> YouTube Player
								<div class="pull-right">
									<div class="btn-group">
										<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/youtube_playlists?action=list&order-by=date&dir=desc">Gestion des playlists</a>
									</div>
								</div>
							</div>
							<div class="panel-body">
								<%
									if (listVideo != null && listVideo.size() > 0) {
								%>
								<noscript><div class="spaced-vertical dark-grey">Javascript doit être activé sur votre navigateur pour afficher le lecteur YouTube.</div></noscript>
								<div id="player"></div>
								<div class="spaced-vertical">
									<button class="btn btn-primary btn-xs btn-perso btn-fix-long marged-right" onclick="previousVideo()">Précédent</button>
									<button class="btn btn-primary btn-xs btn-perso btn-fix-long marged-left" onclick="nextVideo()">Suivant</button>
								</div>
								<% } else { %>
								<br>
								<p class="center light-grey">Aucune vidéo de playlist active.</p>
								<br>
								<% } %>
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
	<!-- Javascript Perso -->
	<script src="<%=path%>/js/player_youtube.js"></script>
	<script>
	// Liste des vidéos actuellement en playlist
	var videoIDs = [
		<%
			if (listVideo != null && listVideo.size() > 0) {
			    int first = 0;
		    	for (YouTubeVideo video : listVideo) {
		    	    if (first != 0) {
		    			out.print(", ");
		    	    }
		    	    out.print("'" + video.getIdUrl() + "'");
		    	    first++;
		    	}
			}
		%>
	];
    </script>
</body>
</html>