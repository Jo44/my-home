<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.ObjectIPAPI"%>
<%@ page import="fr.my.home.bean.ViewJSP"%>

<%
    String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	ObjectIPAPI objectIPAPI = (ObjectIPAPI) view.getValueForKey("objectIPAPI");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title>My Home | Localisation</title>
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
<link href="<%=path%>/css/localisation.css" rel="stylesheet" type="text/css">
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
								<i class="fa fa-fw fa-sitemap"></i> Localisation
							</div>
							<div class="panel-body">
								<%
								    if (error != null) {
								%>
								<div class="alert alert-danger panel-error center" role="alert">
									<p>
										<strong>Oops ! </strong><%=error%></p>
								</div>
								<%
								    }
								%>
								<form id="localisationForm" action="localisation" method="post">
									<br>
									<div class="col-xs-12 center spaced">
										<label for="inputWebsiteIP">Site Web ou adresse IP : </label><br> <input id="inputWebsiteIP" type="text" name="websiteIP"
											maxlength="100" required autofocus
										>
									</div>
									<div class="col-xs-12 center spaced">
										<button class="btn btn-primary btn-sm btn-perso" type="submit">Rechercher</button>
									</div>
									<br>
								</form>
							</div>
						</div>
						<%
						    if (objectIPAPI != null) {
						%>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fa fa-fw fa-globe"></i> Carte
							</div>
							<div class="panel-body">
								<p class="center spaced">
									<span class="bold">IP : </span><%=objectIPAPI.getQuery()%><br> <span class="bold">Organisation : </span><%=objectIPAPI.getOrg()%><br>
									<span class="bold">Pays : </span><%=objectIPAPI.getCountry()%><br>
								</p>
							</div>
							<div class="panel-body">
								<div id="map"></div>
								<script>
								      var map;
								      function initMap() {
								      	  var coordonnees = {lat: <%= objectIPAPI.getLat() %>, lng: <%= objectIPAPI.getLon() %>};
								    	  
								    	  var map = new google.maps.Map(document.getElementById('map'), {
								          center: coordonnees,
								          zoom: 6,
								          styles: [
								              {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
								              {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
								              {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
								              {
								                featureType: 'administrative.locality',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#d59563'}]
								              },
								              {
								                featureType: 'poi',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#d59563'}]
								              },
								              {
								                featureType: 'poi.park',
								                elementType: 'geometry',
								                stylers: [{color: '#263c3f'}]
								              },
								              {
								                featureType: 'poi.park',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#6b9a76'}]
								              },
								              {
								                featureType: 'road',
								                elementType: 'geometry',
								                stylers: [{color: '#38414e'}]
								              },
								              {
								                featureType: 'road',
								                elementType: 'geometry.stroke',
								                stylers: [{color: '#212a37'}]
								              },
								              {
								                featureType: 'road',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#9ca5b3'}]
								              },
								              {
								                featureType: 'road.highway',
								                elementType: 'geometry',
								                stylers: [{color: '#746855'}]
								              },
								              {
								                featureType: 'road.highway',
								                elementType: 'geometry.stroke',
								                stylers: [{color: '#1f2835'}]
								              },
								              {
								                featureType: 'road.highway',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#f3d19c'}]
								              },
								              {
								                featureType: 'transit',
								                elementType: 'geometry',
								                stylers: [{color: '#2f3948'}]
								              },
								              {
								                featureType: 'transit.station',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#d59563'}]
								              },
								              {
								                featureType: 'water',
								                elementType: 'geometry',
								                stylers: [{color: '#17263c'}]
								              },
								              {
								                featureType: 'water',
								                elementType: 'labels.text.fill',
								                stylers: [{color: '#515c6d'}]
								              },
								              {
								                featureType: 'water',
								                elementType: 'labels.text.stroke',
								                stylers: [{color: '#17263c'}]
								              }
								            ]
								          });
								    	  var marker = new google.maps.Marker({
								    		position: coordonnees,
								    		map: map
								    	  });
								      }
								    </script>
								<!-- Google Maps API -->
								<script src="https://maps.googleapis.com/maps/api/js?key=****&callback=initMap" async defer></script>
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
	<!-- jQuery -->
	<script src="<%=path%>/js/jquery.js"></script>
	<!-- Bootstrap Core JavaScript -->
	<script src="<%=path%>/js/bootstrap.js"></script>
</body>
</html>