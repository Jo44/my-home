<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
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
	long usedSpace = (long) view.getValueForKey("usedSpace");

	long progressValue;
	if (usedSpace <= 0) {
		progressValue = 0;
	} else if (usedSpace >= 5368709120L) {
		progressValue = 100;
	} else {
		progressValue = (usedSpace * 100) / 5368709120L;
	}

	String formatUsedSpace;
	DecimalFormat formatDecimal = new DecimalFormat("#.00");
	if (usedSpace <= 0) {
		formatUsedSpace = "0 octet";
	} else if (usedSpace == 1) {
		formatUsedSpace = "1 octet";
	} else if (usedSpace < 1024) {
		formatUsedSpace = String.valueOf(usedSpace) + " octets";
	} else if (usedSpace < (1024 * 1024)) {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / 1024)) + " ko";
	} else if (usedSpace < (1024 * 1024 * 1024)) {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / (1024 * 1024))) + " Mo";
	} else {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / (1024 * 1024 * 1024))) + " Go";
	}
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
						<div class="panel panel-default panel-add center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-clipboard"></i> Ajouter des fichiers
								<div class="pull-right">
									<button id="sendFilesButton" class="btn btn-success btn-xs btn-perso" form="newFileForm" type="submit" disabled>Envoyer</button>
								</div>
							</div>
							<div class="panel-body">
								<div class="col-xs-12 col-sm-offset-1 col-sm-10 marged-top marged-bottom">
									<div class="center-div">
										<p>Espace disque utilisé :<br /><span class="bold"><%=formatUsedSpace%></span> sur <span class="bold">5 Go</span></p>
									</div>
									<div class="progress marged-top" title="<%=formatUsedSpace%> sur 5 Go">
										<div class="progress-bar
										<%
											if (progressValue >= 70 && progressValue < 90) {
												out.print(" progress-bar-warning");
											} else if (progressValue >= 90) {
												out.print(" progress-bar-alert");
											}
										%>
										" role="progressbar" aria-valuenow="<%=progressValue%>" aria-valuemin="0" aria-valuemax="100" style="width: <%=progressValue%>%;">
										<%
											if (progressValue > 20) {
										%>
											<span class="progress-bar-value"><%=progressValue%> %</span>
										<%
											}
										%>
										</div>
										<%
											if (progressValue <= 20) {
										%>
										<span class="progress-bar-value-inverted"><%=progressValue%> %</span>										
										<%
											}
										%>
									</div>
									<hr>
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
									<form id="newFileForm" action="files" method="post" enctype="multipart/form-data">
										<label for="fileInput" class="label-file">Choisir des fichiers</label>
										<p class="center bold">( Maximum: 100 Mo par fichier - 250 Mo par envoi )</p>
										<p id="selectedFiles"></p>
										<input type="file" name="file" id="fileInput" multiple onchange="fileDetails()" />
										<noscript>Javascript doit être activé sur votre navigateur pour enregistrer de nouveaux fichiers.</noscript>
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
								<i class="fa fa-fw fa-files-o"></i> Vos fichiers
								<div class="pull-right">
									<select id="selectByPage" class="marged-right" onchange="changeByPage()">
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
								<div class="big-spaced-vertical">
									<p class="center">Aucun fichier enregistré.</p>
								</div>
								<%
									} else {
								%>
								<noscript>
									<div class="spaced-vertical">Javascript doit être activé sur votre navigateur pour afficher la liste des fichiers enregistrés.</div>
								</noscript>
								<div id="listingTable"></div>
								<div id="paginationBloc">
									<a href="javascript:prevPage()" id="btn_prev" class="page-arrow fa fa-fw fa-arrow-left on-left big-marged-left marged-top marged-bottom"></a>
									<p class="in-line marged-top marged-bottom italic dark-grey">
										Page <span id="page"></span>
									</p>
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
					if (file.getName().indexOf("\"") != -1 || file.getName().indexOf("<") != -1
							|| file.getName().indexOf(">") != -1) {
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
	<script src="<%=path%>/js/form_file.js"></script>
</body>
</html>