<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>

<%@ page import="fr.my.home.bean.YouTubePlaylist"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
    String path = getServletContext().getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String error = (String) view.getValueForKey("error");
    String success = (String) view.getValueForKey("success");
    List<YouTubePlaylist> listPlaylist = (List<YouTubePlaylist>) view.getValueForKey("listPlaylist");
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
<title>My Home | Playlists YouTube</title>
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
						<div class="panel panel-large center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-play-circle"></i> Playlists YouTube
								<div class="pull-right">
									<select id="selectPerPage" class="marged-right" onchange="changePerPage()">
										<option value="10">10</option>
										<option value="25">25</option>
										<option value="50">50</option>
									</select>
									<a class="btn btn-success btn-xs btn-perso" href="<%=path%>/youtube_playlists?action=add">Ajouter</a>
									<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/youtube_player">Retour</a>
								</div>
							</div>
							<div class="panel-body">
								<%
								    if (error != null) {
								%>
								<div class="col-xs-12">
									<div class="alert alert-danger panel-error center big-marged-top big-marged-bottom" role="alert">
										<p><strong>Oops ! </strong><%=error%></p>
									</div>
								</div>
								<%
								    } else if (success != null) {
								%>
								<div class="col-xs-12">
									<div class="alert alert-success panel-error center big-marged-top big-marged-bottom" role="alert">
										<p><%=success%></p>
									</div>
								</div>
								<%
								    }
									if (listPlaylist == null || listPlaylist.size() < 1) {
								%>
								<br>
								<p class="center">Aucune playlist enregistrée.</p>
								<br>
								<%
								    } else {
								%>
								<div id="listingTable"></div>
								<div id="paginationBloc">
									<a href="javascript:prevPage()" id="btn_prev" class="page-arrow fa fa-fw fa-arrow-left on-left big-marged-left marged-top marged-bottom"></a>
									<p class="in-line marged-top marged-bottom italic dark-grey">Page <span id="page"></span></p>
									<a href="javascript:nextPage()" id="btn_next" class="page-arrow fa fa-fw fa-arrow-right on-right big-marged-right marged-top marged-bottom"></a>
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
	// Liste des playlists récupérés
	var playlists = [
		<%
			if (listPlaylist != null && listPlaylist.size() > 0) {
			    int first = 0;
		    	for (YouTubePlaylist playlist : listPlaylist) {
		    	    if (first != 0) {
		    			out.print(", ");
		    	    }
		    	    out.print(" { ");
		    	    out.print("id: \"");
		    	    out.print(playlist.getId());
		    	    out.print("\", createDate: \"");
		    	    out.print(formatter.format(playlist.getCreateDate()));
		    	    out.print("\", title: \"");
		    	    if (playlist.getTitle().indexOf("\"") != -1 || playlist.getTitle().indexOf("<") != -1 || playlist.getTitle().indexOf(">") != -1) {
		    	    	String formattedTitle = "";
		    	    	formattedTitle = playlist.getTitle().replaceAll("\"", "'");
		    	    	formattedTitle = formattedTitle.replaceAll("<", "");
		    	    	formattedTitle = formattedTitle.replaceAll(">", "");
		    	    	out.print(formattedTitle);
		    	    } else {
		    	    	out.print(playlist.getTitle());
		    	    }
		    	    out.print("\", type: \"");
		    	    if (playlist.getType().indexOf("\"") != -1 || playlist.getType().indexOf("<") != -1 || playlist.getType().indexOf(">") != -1) {
		    	    	String formattedType = "";
		    	    	formattedType = playlist.getType().replaceAll("\"", "'");
		    	    	formattedType = formattedType.replaceAll("<", "");
		    	    	formattedType = formattedType.replaceAll(">", "");
		    	    	out.print(formattedType);
		    	    } else {
		    	    	out.print(playlist.getType());
		    	    }
		    	    out.print("\", active: \"");
		    	    if (playlist.isActive()) {
		    	    	out.print("true");
		    	    } else {
		    	    	out.print("false");
		    	    }
		    	    out.print("\" } ");
		    	    first++;
		    	}
			}
		%>
	];
    </script>
    <script src="<%=path%>/js/pagination_playlist.js"></script>
</body>
</html>