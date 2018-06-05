/* Attributs */
var player;
var artistTxt;
var titleTxt;
var videoId;
var durationTxt;

/* Charge le lecteur YouTube */
function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
	height : '200',
	width : '100%',
	events : {
	    'onStateChange' : onPlayerStateChange
	},
	playerVars : {
	    'controls' : 1,
	    'fs' : 1,
	    'iv_load_policy' : 3,
	    'rel' : 0,
	    'showinfo' : 0
	}
    });
}

/*
 * Test si l'url propose est valide (ID video trouve) et si oui, essaye de
 * lancer la video
 */
function testVideo() {
    // Reinitialise la variable videoId
    videoId = "";

    // Recupere la valeur de l'input raw url video
    var rawUrl = document.getElementById("textRawUrlVideo").value;

    // Formatte l'url complete pour recuperer seulement l'ID de la video
    videoId = formatUrlVideo(rawUrl);

    // Si videoId trouve
    if (videoId) {
	// Affiche le player
	showPlayer();

	// Charge le lecteur
	player.loadVideoById(videoId);
    }
}

/*
 * Formatte l'url brut de YouTube (selon 2 formats possibles) et renvoi l'ID de
 * la video si trouve
 */
function formatUrlVideo(rawUrl) {

    // Formats possibles:
    // https://www.youtube.com/watch?v=9Ke4480MicU
    // https://youtu.be/9Ke4480MicU

    var videoUrl;

    // Recupere le debut de l'ID de la video
    if (rawUrl) {
	if (rawUrl.indexOf("watch?v=") != -1) {
	    videoUrl = rawUrl.substring(rawUrl.indexOf("watch?v=") + 8);
	} else if (rawUrl.indexOf("youtu.be/") != -1) {
	    videoUrl = rawUrl.substring(rawUrl.indexOf("youtu.be/") + 9);
	}
    }
    // Coupe a la fin de l'ID de la video
    if (videoUrl) {
	if (videoUrl.length > 11) {
	    videoUrl = videoUrl.substring(0, 11);
	}
    }
    return videoUrl;
}

/* Lorsque la video se lance, recupere les informations pour le formulaire */
function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING) {

	// Recupere le duo artiste/titre et essaye de le split
	var artistTitle = player.getVideoData().title;
	getArtistAndtitle(artistTitle);

	// Recupere la duree totale et la formatte
	var rawDuration = player.getDuration();
	getFormatDuration(rawDuration);

	// Recupere les champs du formulaire
	var artist = document.getElementById("textArtist");
	var title = document.getElementById("textTitle");
	var urlVideo = document.getElementById("textUrlVideo");
	var duration = document.getElementById("textDuration");

	// Remplis les champs
	artist.value = artistTxt;
	title.value = titleTxt;
	urlVideo.value = videoId;
	duration.value = durationTxt;
    }
}

/*
 * Essaye de recuperer un artiste et un titre a partir de la variable
 * data.getTitle()
 */
function getArtistAndtitle(artistTitle) {
    if (artistTitle) {
	if (artistTitle.indexOf(" - ") != -1) {
	    // Si un tiret est present, decoupe l'artiste et le titre
	    artistTxt = artistTitle.substring(0, artistTitle.indexOf(" - "));
	    titleTxt = artistTitle.substring(artistTitle.indexOf(" - ") + 3);
	} else {
	    // Sinon, garde l'ensemble dans l'artiste
	    artistTxt = artistTitle;
	    titleTxt = "";
	}
	artistTxt = toTitleCase(artistTxt);
	titleTxt = toTitleCase(titleTxt);
    }
}

/*
 * Met en majuscule la premiere lettre de chaque mot
 */
function toTitleCase(str) {
    return str.replace(/\w\S*/g, function(txt){
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}

/*
 * Formate la duree selon le format maximum 'hhh:mm:ss' et minimum 'm:ss' a
 * partir du nombre de secondes (en float)
 */
function getFormatDuration(rawDuration) {
    var hours = 0;
    var minutes = 0;
    var secondes = 0;
    var hoursStr = "";
    var minutesStr = "0";
    var secondesStr = "00";

    // Determine les heures / minutes / secondes
    if (rawDuration) {
	if (rawDuration <= 0) {
	    // Si la duree totale est inferieure ou egale a '000:00:00'
	    hours = 0;
	    hoursStr = "";
	    minutes = 0;
	    minutesStr = "0";
	    secondes = 0;
	    secondesStr = "00";
	} else if (rawDuration >= 3600060) {
	    // Si la duree totale est superieure ou egale a '999:59:59'
	    hours = 999;
	    hoursStr = "999";
	    minutes = 59;
	    minutesStr = "59";
	    secondes = 59;
	    secondesStr = "59";
	} else {
	    // Si la duree est comprise entre '000:00:00' et '999:59:59'
	    if (rawDuration > 3600) {
		hours = Math.trunc(rawDuration / 3600);
		hoursStr = hours;
	    }
	    if (rawDuration > 60) {
		minutes = Math.trunc((rawDuration - (hours * 3600)) / 60);
		// Rajoute un 0 si besoin
		if (hours > 0 && minutes < 9) {
		    minutesStr = "0" + minutes;
		} else {
		    minutesStr = minutes;
		}
	    }
	    secondes = Math
		    .trunc(rawDuration - (hours * 3600) - (minutes * 60));
	    // Rajoute un 0 si besoin
	    if (secondes > 9) {
		secondesStr = secondes;
	    } else {
		secondesStr = "0" + secondes;
	    }
	}
    }

    // Reforme la duree
    if (hours > 0) {
	durationTxt = hoursStr + ":" + minutesStr + ":" + secondesStr;
    } else {
	durationTxt = minutesStr + ":" + secondesStr;
    }
}

/* Cache le player et efface tous les champs */
function clearFields() {
    // Arrete la lecture du player
    stopPlayer();

    // Cache le player
    hidePlayer();

    // Recupere les champs du formulaire
    var rawUrlVideo = document.getElementById("textRawUrlVideo");
    var artist = document.getElementById("textArtist");
    var title = document.getElementById("textTitle");
    var urlVideo = document.getElementById("textUrlVideo");
    var duration = document.getElementById("textDuration");

    // Efface les champs
    rawUrlVideo.value = "";
    artist.value = "";
    title.value = "";
    urlVideo.value = "";
    duration.value = "";
}

/* Affiche le player */
function showPlayer() {
    document.getElementById('preview-youtube').style.display = "block";
}

/* Cache le player */
function hidePlayer() {
    document.getElementById('preview-youtube').style.display = "none";
}

/* Stop le player */
function stopPlayer() {
    if (player) {
	player.stopVideo();
    }
}