<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
    String path = getServletContext().getContextPath();
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String token = (String) view.getValueForKey("token");
    String error = (String) view.getValueForKey("error");
    boolean databaseOnline = (boolean) view.getValueForKey("databaseOnline");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Modification du mot de passe</title>
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
			<div class="col-xs-offset-1 col-xs-10 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
				<div class="panel panel-default center-div">
					<div class="panel-heading">
						<h3 class="panel-title in-line">Modification du mot de passe</h3>
						<%
						    if (databaseOnline) {
						%>
						<img class="on-right" src="<%=path%>/img/trafficlight-green.png" alt="ON">
						<%
						    } else {
						%>
						<img class="on-right" src="<%=path%>/img/trafficlight-red.png" alt="OFF">
						<%
						    }
						%>
					</div>
					<div class="panel-body">
						<%
						    if (error != null) {
						%>
						<div class="alert alert-danger center" role="alert">
							<p>
								<strong><%=error%></strong>
							</p>
						</div>
						<%
						    }
						%>
						<form role="form" action="reinit" method="post">
							<fieldset>
								<div class="form-group">
									<input class="form-control center" id="inputNewPassword1" type="password" name="newpassword" pattern=".{6,30}"
										placeholder="Nouveau mot de passe" title="Entre 6 et 30 caractères" required
									/>
								</div>
								<div class="form-group">
									<input class="form-control center" id="inputNewPassword2" type="password" name="newpassword-confirm" pattern=".{6,30}"
										placeholder="Confirmation mot de passe" title="Entre 6 et 30 caractères" required
									/>
								</div>
								<input type='hidden' name='token' value='<%if (token != null && token.trim().length() > 0) { out.print(token); }%>'>
								<button type="submit" class="btn btn-sm btn-block btn-medium">Modifier</button>
								<a class="btn btn-sm btn-block btn-medium" href="<%=path%>/check">Revenir à la connexion</a>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>