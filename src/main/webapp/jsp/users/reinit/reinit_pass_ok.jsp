<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
	String path = getServletContext().getContextPath();
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<!-- Redirection automatique apr�s 5 secondes -->
<meta http-equiv="refresh" content="5;url=<%=path%>/disconnect" />
<title>My Home | R�-initialisation effectu�e</title>
<link rel="icon" type="image/x-icon" href="<%=path%>/img/favicon.ico" />
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css">
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css">
<!-- CSS Perso -->
<link href="<%=path%>/css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-xs-offset-1 col-xs-10 col-sm-offset-3 col-sm-6">
				<div class="panel panel-default center-div">
					<div class="panel-heading">
						<h3 class="panel-title in-line">Confirmation</h3>
					</div>
					<div class="padding-body center">
						<p>Vous avez correctement r�-initialiser le mot de passe de votre compte !</p>
						<br>
						<p>Vous allez �tre automatiquement rediriger vers la page de connexion ..</p>
						<p>
							<a class="login-link" href="<%=path%>/disconnect">Cliquer ici pour �tre directement redirig�</a>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>