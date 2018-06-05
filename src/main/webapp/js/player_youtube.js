/* Attributs */
var player;
var currentVideoId = 0;

/* Charge le lecteur YouTube */
function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
	height : '700',
	width : '100%',
	events : {
	    'onReady' : onPlayerReady,
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

/* Charge la video une fois le lecteur charge */
function onPlayerReady(event) {
    event.target.loadVideoById(videoIDs[currentVideoId]);
}

/* A la fin de la video, passe automatiquement a la suivante */
function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.ENDED) {
	nextVideo();
    }
}

/* Lors du clic sur 'Precedent', passe a la video precedente */
function previousVideo() {
    currentVideoId--;
    if (currentVideoId < 0) {
	currentVideoId = videoIDs.length - 1;
    }
    player.loadVideoById(videoIDs[currentVideoId]);
}

/* Lors du clic sur 'Suivant', passe a la video suivante */
function nextVideo() {
    currentVideoId++;
    if (currentVideoId >= videoIDs.length) {
	currentVideoId = 0;
    }
    player.loadVideoById(videoIDs[currentVideoId]);
}