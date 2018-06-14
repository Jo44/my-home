// Formatte l'affichage du poids d'un fichier
function formatSize(fSizeRaw) {
    let fSize;
    if (fSizeRaw <= 1) {
	// Poids en octet
	fSize = '<span class="bold">' + fSizeRaw + '</span> octet'; 
    } else if (fSizeRaw < 1024) {
	// Poids en octets
	fSize = '<span class="bold">' + fSizeRaw + '</span> octets';
    } else if (fSizeRaw < (1024*1024)) {
	// Poids en ko
	fSize = '<span class="bold">' + (fSizeRaw / 1024).toFixed(2) + '</span> ko';
    } else {
	// Poids en Mo
	fSize = '<span class="bold">' + (fSizeRaw / (1024 * 1024)).toFixed(2) + '</span> Mo';
    }
    return fSize;
}

// Affiche le detail de chaque fichier et determine si l'upload est valide ou non
function fileDetails() {
    
    // Attribut
    let validFile = true;
    let validRequest = true;
    let nbFile = 0;
    let totalRequestWeight = 0;
    let filesDetails = "";
    
    // Recupere les elements du DOM
    let fileInput = document.getElementById('fileInput');
    let selectedFile = document.getElementById('selectedFiles');
    let sendFilesButton = document.getElementById('sendFilesButton');

    // Valide ou verifie si aucun fichier n'est selectionne
    if (fileInput.files.length > 0) {
	
        // Pour chaque fichier selectionne
        for (let i = 0; i <= fileInput.files.length - 1; i++) {

            // Recupere le nom et le poids du fichier
            let fName = fileInput.files.item(i).name;
            let fSizeRaw = fileInput.files.item(i).size;
            
            // Determine si le poids du fichier est valide
            validFile = fSizeRaw <= (100 * (1024 * 1024));

            // Ajout le poids au poids total
            totalRequestWeight += fSizeRaw;
            
            // Formatte le poids du fichier
            let fSize = formatSize(fSizeRaw);

            // Affiche les details du fichier
            if (i > 0) {
                filesDetails += '<br />';
            }
            if (validFile) {
                filesDetails += fName + ' (' + fSize + ') <i class="fa fa-fw fa-check-circle green"></i>';
            } else {
                validRequest = false;
                filesDetails += fName + ' (' + fSize + ') <i class="fa fa-fw fa-times-circle red"></i>';
            }
            nbFile++;
        }
        
        // Determine si le poids total des fichiers est valide
        if (validRequest) {
            validRequest = totalRequestWeight <= (250 * (1024 * 1024));
        }
        
        // Ajoute le total des fichiers
        if (fileInput.files.length < 2) {
            if (totalRequestWeight <= (250 * (1024 * 1024))) {
        	filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">1</span> fichier (' + formatSize(totalRequestWeight) + ') <i class="fa fa-fw fa-check-circle green"></i><br />';
            } else {
        	filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">1</span> fichier (' + formatSize(totalRequestWeight) + ') <i class="fa fa-fw fa-times-circle red"></i><br />';
            }
        } else {
            if (totalRequestWeight <= (250 * (1024 * 1024))) {
        	filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">' + fileInput.files.length + '</span> fichiers (' + formatSize(totalRequestWeight) + ') <i class="fa fa-fw fa-check-circle green"></i><br />';
            } else {
        	filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">' + fileInput.files.length + '</span> fichiers (' + formatSize(totalRequestWeight) + ') <i class="fa fa-fw fa-times-circle red"></i><br />';
            }
        }

        // Affiche les détails
        selectedFile.innerHTML = filesDetails;
        

    } else {
        // Efface les details si aucun fichier selectionne
        selectedFile.innerHTML = "";
        validRequest = false;
    }
    
    // Active ou non le bouton 'Envoyer'
	sendFilesButton.disabled = !validRequest;
}