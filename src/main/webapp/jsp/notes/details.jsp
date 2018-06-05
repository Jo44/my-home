<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>

<%@ page import="fr.my.home.bean.Note"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	Note note = (Note) view.getValueForKey("note");
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
<title>My Home | Détails de la note</title>
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
								<i class="fa fa-fw fa-edit"></i> Détails de la note
								<div class="pull-right">
									<div class="btn-group">
										<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/notes?action=list&order-by=date&dir=desc">Retour</a>
									</div>
								</div>
							</div>
							<div class="panel-body">
								<table class="table table-striped">
									<tr>
										<th class="col-xs-3">Date / Heure</th>
										<td class="col-xs-9"><%=formatter.format(note.getSaveDate())%></td>
									</tr>
									<tr>
										<th>Titre</th>
										<td><%=note.getTitle()%></td>
									</tr>
									<tr>
										<th>Message</th>
										<td></td>
									</tr>
								</table>
								<div class="message-box center"><%=note.getMessage()%></div>
								<br>
								<div class="center">
									<button type="button" class="btn btn-danger btn-sm btn-perso" data-toggle="modal" data-target=".modal-confirm" data-backdrop="static">Supprimer</button>
								</div>
								<br>
								<!-- modal : confirmation suppression -->
								<div class="modal fade modal-confirm" tabindex="-1" role="dialog" aria-labelledby="modal_label">
									<div class="modal-dialog modal-sm" role="document">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close">
													<span aria-hidden="true"><i class="fa fa-fw fa-times"></i></span>
												</button>
												<h4 class="modal-title" id="modal_label">Confirmation</h4>
											</div>
											<div class="modal-body">
												<p>Voulez-vous confirmer la suppression de cette note ?</p>
											</div>
											<div class="modal-footer center">
												<a class="btn btn-danger btn-sm btn-perso" href="<%=path%>/notes?action=delete&id=<%=note.getId()%>">Supprimer</a>
												<button type="button" class="btn btn-primary btn-sm btn-perso" data-dismiss="modal">Annuler</button>
											</div>
										</div>
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
