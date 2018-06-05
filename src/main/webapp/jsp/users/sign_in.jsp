<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
    String path = getServletContext().getContextPath();
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	boolean databaseOnline = (boolean) view.getValueForKey("databaseOnline");
	String userName = (String) view.getValueForKey("userName");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Connexion</title>
<link rel="icon" type="image/x-icon" href="<%=path%>/img/favicon.ico" />
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css">
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css">
<!-- CSS Perso -->
<link href="<%=path%>/css/login.css" rel="stylesheet" type="text/css">
<!-- Cookie Consent (cookieconsent.insites.com) -->
<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.css" />
<script src="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.js"></script>
<script>
	window.addEventListener("load",	function() {
		window.cookieconsent.initialise({
			"palette" : {
				"popup" : {
					"background" : "#333333",
					"text" : "#dddddd"
				},
				"button" : {
					"background" : "transparent",
					"text" : "#f0ad4e",
					"border" : "#f0ad4e"
				}
			},
			"position" : "bottom-right",
			"content" : {
				"message" : "Ce site web utilise des cookies pour vous apporter une meilleure expérience. Nous ne stockons personnellement aucune donnée sensible.",
				"dismiss" : "OK",
				"link" : "En savoir plus"
			}
		})
	});
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-xs-offset-1 col-xs-10 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
				<div class="panel panel-default center-div">
					<div class="panel-heading">
						<h3 class="panel-title in-line">Connexion</h3>
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
						<form role="form" action="check" method="post">
							<fieldset>
								<div class="form-group">
									<input class="form-control center" placeholder="Login" name="user_name" type="text" pattern=".{3,30}" required
									<%
										if (userName != null) {
											out.print("value='" + userName + "'");
									    } else {
											out.print(" autofocus");
									    }
									%>>
								</div>
								<div class="form-group">
									<input class="form-control center" placeholder="Password" name="user_pass" type="password" pattern=".{6,30}" title="Entre 6 et 30 caractères"
										required
									<%
										if (userName != null) {
											out.print(" autofocus");
									    }
									%>>
								</div>
								<div class="checkbox center">
									<label> <input name="remember" type="checkbox" value="true"
									<%
										if (userName != null) {
											out.print(" checked");
									    }
									%>>
										<span> Se rappeler de moi</span>
									</label>
								</div>
								<button type="submit" class="btn btn-sm btn-block btn-medium">Connexion</button>
							</fieldset>
						</form>
						<div class="center marged-top">
							<a class="login-link" href="<%=path%>/register">Pas encore inscrit ? Créer un compte</a>
						</div>
						<div class="center">
							<a class="login-link" href="<%=path%>/forgot">Identifiants oubliés ?</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>