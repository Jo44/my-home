<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>

<%@ page import="fr.my.home.bean.YouTubePlaylist"%>
<%@ page import="fr.my.home.bean.YouTubeVideo"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
    String path = getServletContext().getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String error = (String) view.getValueForKey("error");
    String success = (String) view.getValueForKey("success");
    YouTubePlaylist playlist = (YouTubePlaylist) view.getValueForKey("playlist");
    List<YouTubeVideo> listVideo = (List<YouTubeVideo>) view.getValueForKey("listVideo");
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
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
								<div class="col-xs-12 col-sm-offset-1 col-sm-10">
									<div class="alert alert-danger panel-error center big-marged-top" role="alert">
										<p><strong>Oops ! </strong><%=error%></p>
									</div>
								</div>
								<%
								    } else if (success != null) {
								%>
								<div class="col-xs-12 col-sm-offset-1 col-sm-10">
									<div class="alert alert-success panel-error center big-marged-top" role="alert">
										<p><%=success%></p>
									</div>
								</div>
								<%
								    }
								%>
								<div class="col-xs-12">
									<form id="updatePlaylistForm" action="youtube_playlists" method="post">
										<input type="hidden" name="action" value="update" required>
										<input type="hidden" name="idPlaylist" value="<%=playlist.getId()%>" required>
										<br>
										<div class="col-xs-12 col-sm-offset-1 col-sm-5">
											<label for="textTitle">Titre de la playlist</label>
											<br>
											<input id="textTitle" name="titlePlaylist" type="text" pattern=".{1,50}"
												title="Entre 1 et 50 caractères" value="<%=playlist.getTitle()%>" required>
										</div>
										<div class="col-xs-12 col-sm-5">
											<label for="textType">Type de la playlist</label>
											<br>
											<input id="textType" name="typePlaylist" type="text" pattern=".{1,50}"
												title="Entre 1 et 50 caractères" value="<%=playlist.getType()%>" required>
										</div>
										<div class="col-xs-12 big-marged-top">
											<label class="lbl_check" for="checkbox">Playlist active : </label>
											<label class="switch">
												<input id="checkbox" name="checkbox_active" type="checkbox"
												<%
													if (playlist.isActive()) {
														out.print("checked");
												    }
												%>>
												<span class="slider"></span>
											</label>
										</div>
										<div class="col-xs-12 col-sm-12 marged-bottom">
											<button class="btn btn-success btn-sm btn-perso" type="submit">Enregistrer les modifications</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> Vidéo YouTube
								<div class="pull-right">
									<select id="selectPerPage" class="marged-right" onchange="changePerPage()">
										<option value="10">10</option>
										<option value="25">25</option>
										<option value="50">50</option>
									</select>
								</div>
							</div>
							<div class="panel-body">
								<%
								    if (listVideo == null || listVideo.size() < 1) {
								%>
								<div class="col-xs-12">
									<div>
										<p class="center marged-top">Aucune vidéo enregistrée.</p>
									</div>
									<div class="spaced-vertical">
										<p class="center">
											<a class="btn btn-primary btn-sm btn-perso" href="<%=path%>/youtube_videos?action=add&idPlaylist=<%=playlist.getId()%>">Ajouter une	vidéo</a>
										</p>
									</div>
								</div>
								<%
								    } else {
								%>
								<div>
									<div id="listingTable"></div>
									<div id="paginationBloc">
										<a href="javascript:prevPage()" id="btn_prev" class="page-arrow fa fa-fw fa-arrow-left on-left big-marged-left marged-top"></a>
										<p class="in-line marged-top no-marged-bottom italic dark-grey">Page <span id="page"></span></p>
										<a href="javascript:nextPage()" id="btn_next" class="page-arrow fa fa-fw fa-arrow-right on-right big-marged-right marged-top"></a>
									</div>
									<div class="spaced-vertical">
										<p class="center">
											<a class="btn btn-primary btn-sm btn-perso" href="<%=path%>/youtube_videos?action=add&idPlaylist=<%=playlist.getId()%>">Ajouter une vidéo</a>
										</p>
									</div>
								</div>
								<%
								    }
								%>
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
	<!-- Javascript Perso -->
	<script>
	var path = '<%=path%>';
	var playlistId = '<%=playlist.getId()%>';
	// Liste des vidéos récupérées
	var videos = [
		<%
			if (listVideo != null && listVideo.size() > 0) {
			    int first = 0;
		    	for (YouTubeVideo video : listVideo) {
		    	    if (first != 0) {
		    			out.print(", ");
		    	    }
		    	    out.print(" { ");
		    	    out.print("id: \"");
		    	    out.print(video.getId());
		    	    out.print("\", playlistId: \"");
		    	    out.print(playlist.getId());
		    	    out.print("\", saveDate: \"");
		    	    out.print(formatter.format(video.getSaveDate()));
		    	    out.print("\", artist: \"");
		    	    if (video.getArtist().indexOf("\"") != -1 || video.getArtist().indexOf("<") != -1 || video.getArtist().indexOf(">") != -1) {
		    	    	String formattedArtist = "";
		    	    	formattedArtist = video.getArtist().replaceAll("\"", "'");
		    	    	formattedArtist = formattedArtist.replaceAll("<", "");
		    	    	formattedArtist = formattedArtist.replaceAll(">", "");
		    	    	out.print(formattedArtist);
		    	    } else {
		    	    	out.print(video.getArtist());
		    	    }
		    	    out.print("\", title: \"");
		    	    if (video.getTitle().indexOf("\"") != -1 || video.getTitle().indexOf("<") != -1 || video.getTitle().indexOf(">") != -1) {
		    	    	String formattedTitle = "";
		    	    	formattedTitle = video.getTitle().replaceAll("\"", "'");
		    	    	formattedTitle = formattedTitle.replaceAll("<", "");
		    	    	formattedTitle = formattedTitle.replaceAll(">", "");
		    	    	out.print(formattedTitle);
		    	    } else {
		    	    	out.print(video.getTitle());
		    	    }
		    	    out.print("\", duration: \"");
		    	    out.print(video.getDuration());
		    	    out.print("\" } ");
		    	    first++;
		    	}
			}
		%>
	];
    </script>
    <script src="<%=path%>/js/pagination_video.js"></script>
</body>
</html>