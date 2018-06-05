<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String today = (String) view.getValueForKey("today");
	String j;
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Nouvelle note</title>
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
<link href="<%=path%>/css/notes.css" rel="stylesheet" type="text/css">
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
								<i class="fa fa-fw fa-edit"></i> Ajouter une note
								<div class="pull-right">
									<div class="btn-group">
										<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/notes?action=list&order-by=date&dir=desc">Retour</a>
									</div>
								</div>
							</div>
							<div class="panel-body">
								<form id="newNoteForm" action="notes" method="post">
									<br>
									<div class="col-xs-12 col-sm-6">
										<label for="inputDate">Date </label>
										<%
											if (today != null) {
										%>
										<!-- substring date 'yyyy-MM-dd HH:mm:ss.SSS' -->
										<input id="inputDate" type="date" name="date" value="<%=today.substring(0, 4)%>-<%=today.substring(5, 7)%>-<%=today.substring(8, 10)%>"
											required
										>
										<%
											} else {
										%>
										<input id="inputDate" type="date" name="date" value="2017-01-01" required>
										<%
											}
										%>
									</div>
									<div class="col-xs-12 col-sm-6">
										<label>Heure </label> <select id="inputHeure" name="hour" required>
										<%
											for (int i = 0; i < 24; i++) {
												if (i < 10)
													j = "0" + String.valueOf(i);
												else
													j = String.valueOf(i);
												if (j.equals(today.substring(11, 13))) {
											%>
											<option selected><%=j%></option>
											<%
												} else {
											%>
											<option><%=j%></option>
											<%
												}
											}
										%>
										</select> <label> Minute </label> <select id="inputMinute" name="minute" required>
										<%
											for (int i = 0; i < 60; i = i + 5) {
												if (i < 10)
													j = "0" + String.valueOf(i);
												else
													j = String.valueOf(i);
												if (j.equals(today.substring(14, 16))) {
											%>
											<option selected><%=j%></option>
											<%
												} else {
											%>
											<option><%=j%></option>
											<%
												}
											}
										%>
										</select>
									</div>
									<br> <br>
									<div class="col-xs-12 col-sm-12">
										<label for="textTitle">Titre </label> <br> <input id="textTitle" name="title" type="text" pattern=".{1,100}"
											title="Entre 1 et 100 caractères" autofocus required>
									</div>
									<br> <br> <br>
									<div class="col-xs-12">
										<label for="areaComment">Message </label> <br>
										<textarea id="areaComment" name="message" maxlength="5000" rows="10"></textarea>
									</div>
									<div class="col-xs-12 center spaced">
										<button class="btn btn-success btn-sm btn-perso" type="submit">Enregistrer</button>
									</div>
									<br>
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
	<!-- Script NicEdit (RichTextBox) -->
	<script src="<%=path%>/NicEdit/nicEdit.js" type="text/javascript"></script>
	<script type="text/javascript">
	bkLib.onDomLoaded(function() {
	    new nicEditor({
		maxHeight : 250,
		fullPanel : false,
		buttonList : [ 'fontFormat', 'fontSize', 'fontFamily', 'bold',
			'italic', 'underline', 'strikethrough', 'left',
			'center', 'right', 'justify', 'ol', 'ul', 'indent',
			'outdent', 'forecolor', 'bgcolor', 'link', 'unlink',
			'xhtml' ]
	    }).panelInstance('areaComment');
	});
    </script>
</body>
</html>