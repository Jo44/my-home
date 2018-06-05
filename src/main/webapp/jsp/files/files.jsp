<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>

<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.CustomFile"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String error = (String) view.getValueForKey("error");
    String success = (String) view.getValueForKey("success");
    List<CustomFile> listFile = (List<CustomFile>) view.getValueForKey("listFile");
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
<title>My Home | Fichiers</title>
<link rel="icon" type="image/x-icon" href="<%=path%>/img/favicon.ico" />
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css">
<!-- Custom CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css">
<!-- Custom Fonts -->
<link href="<%=path%>/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
<!-- CSS Tooltips -->
<link href="<%=path%>/css/tools/tooltip.css" rel="stylesheet" type="text/css">
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css">
<!-- CSS Perso -->
<link href="<%=path%>/css/files.css" rel="stylesheet" type="text/css">
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
								<i class="fa fa-fw fa-clipboard"></i> Ajouter des fichiers
								<div class="pull-right">
									<button class="btn btn-success btn-xs btn-perso" form="newFileForm" type="submit">Envoyer</button>
								</div>
							</div>
							<div class="panel-body">
								<div class="add-file">
									<%
										if (error != null) {
									%>
									<div class="alert alert-danger panel-error center marged-top" role="alert">
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
									<div class="col-xs-12 col-sm-offset-1 col-sm-10 marged-top marged-bottom">
										<form id="newFileForm" action="files" method="post" enctype="multipart/form-data">
											<input type="hidden" name="MAX_FILE_SIZE" value="10485760" /> <input class="center-div" type="file" name="fichier" id="fichier" multiple />
											<div class="max-file">
												<p class="center bold">(Maximum: 10 Mo / fichier - 50 Mo total)</p>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-1 col-md-10 col-md-offset-1 col-lg-offset-1 col-lg-10 col-lg-offset-1">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-files-o"></i> Vos fichiers
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
									if (listFile == null || listFile.size() < 1) {
								%>
								<div class="no-file">
									<p class="center">Aucun fichier enregistré.</p>
								</div>
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
	// Liste des fichiers récupérés
	var files = [
		<%if (listFile != null && listFile.size() > 0) {
			    int first = 0;
		    	for (CustomFile file : listFile) {
		    	    if (first != 0) {
		    			out.print(", ");
		    	    }
		    	    out.print(" { ");
		    	    out.print("id: \"");
		    	    out.print(file.getId());
		    	    out.print("\", uploadDate: \"");
		    	    out.print(formatter.format(file.getUploadDate()));
		    	    out.print("\", weight: \"");
		    	    out.print(file.getWeight());
		    	    out.print("\", name: \"");
		    	    if (file.getName().indexOf("\"") != -1 || file.getName().indexOf("<") != -1 || file.getName().indexOf(">") != -1) {
		    	    	String formattedName = "";
		    	    	formattedName = file.getName().replaceAll("\"", "'");
		    	    	formattedName = formattedName.replaceAll("<", "");
		    	    	formattedName = formattedName.replaceAll(">", "");
		    	    	out.print(formattedName);
		    	    } else {
		    	    	out.print(file.getName());
		    	    }
		    	    out.print("\" } ");
		    	    first++;
		    	}
			}%>
	];
    </script>
    <script src="<%=path%>/js/pagination_file.js"></script>
</body>
</html>