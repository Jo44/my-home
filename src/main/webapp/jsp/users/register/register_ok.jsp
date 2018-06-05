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
<title>My Home | Confirmation</title>
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
						<div class="pull-right">
							<a class="btn btn-primary btn-xs btn-perso" href="<%=path%>/check">Retour</a>
						</div>
					</div>
					<div class="padding-body center">
						<p>Une dernière étape ..</p>
						<br>
						<p>Veuillez consulter votre boîte mail pour valider votre inscription avant la première connexion à l'aide du lien fournis.</p>
						<br>
						<p>
							Si vous rencontrez un soucis lors de l'activation, vous pouvez contacter l'administrateur par <a href="mailto:****@****.com">email</a>.
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>