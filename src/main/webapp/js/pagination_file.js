// Attributs
let current_page = 1;
let records_per_page = 10;
let table;

// Lors du changement du nombre d'entree affichee par page
function changeByPage() {
    records_per_page = document.getElementById("selectByPage").value;
    current_page = 1;
    changePage(current_page);
}

// Lors du clic sur la page precedente
function prevPage() {
    if (current_page > 1) {
		current_page--;
		changePage(current_page);
    }
}

// Lors du clic sur la page suivante
function nextPage() {
    if (current_page < numPages()) {
		current_page++;
		changePage(current_page);
    }
}

// Lors de l'appel d'une page
function changePage(page) {

    // Recuperation du DOM
    let btn_next = document.getElementById("btn_next");
    let btn_prev = document.getElementById("btn_prev");
    let select_by_page = document.getElementById("selectByPage");
    let listing_table = document.getElementById("listingTable");
    let pagination_bloc = document.getElementById("paginationBloc");
    let page_span = document.getElementById("page");

    // Si au moins une entree
    if (files.length > 0) {

	// Si au moins une entree, affiche le nombre d'entree par page
	select_by_page.style.visibility = "visible";

	// Valide la page actuelle
	if (page < 1)
	    page = 1;
	if (page > numPages())
	    page = numPages();

	// Assemble le tableau HTML
	table = assembleHtml(page);

	// Vide l'ancien tableau si existant dans l'HTML
	while (listing_table.firstChild) {
	    listing_table.removeChild(listing_table.firstChild);
	}

	// Ajout du tableau dans l'HTML
	listing_table.appendChild(table);

	// Ajout du numero de la page actuelle
	page_span.innerHTML = page;

	// Affiche ou cache toute la pagination si une seule page
	if (numPages() === 1) {
	    pagination_bloc.style.display = "none";
	} else {
	    pagination_bloc.style.display = "block";
	}

	// Affiche (ou non) la fleche 'Precedent'
	if (page === 1) {
	    btn_prev.style.visibility = "hidden";
	} else {
	    btn_prev.style.visibility = "visible";
	}

	// Affiche (ou non) la fleche 'Suivant'
	if (page === numPages()) {
	    btn_next.style.visibility = "hidden";
	} else {
	    btn_next.style.visibility = "visible";
	}
    } else {
	// Si aucune entree, cache le nombre d'entree par page
	select_by_page.style.visibility = "hidden";
    }
}

// Assemble le tableau HTML
function assembleHtml(page) {
    let table, thead, tbody, tr, th, td, span, p, button, a, img, h4, url, value, div, div2, div3, div4;

    // Table
    table = document.createElement('table');
    table.setAttribute('class', 'table table-striped');

    // THead
    thead = document.createElement('thead');
    tr = document.createElement('tr');
    // Th Date
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-3');
    span = document.createElement('span');
    span.setAttribute('class', 'title-order-bottom');
    span.innerText = 'Date d\'ajout';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=date&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=date&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Weight
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-2');
    span = document.createElement('span');
    span.setAttribute('class', 'title-order-bottom');
    span.innerText = 'Poids';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=weight&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=weight&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Name
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-5');
    span = document.createElement('span');
    span.setAttribute('class', 'title-order-bottom');
    span.innerText = 'Nom';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=name&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/files?action=list&order-by=name&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Action
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-2');
    th.innerText = 'Actions';
    tr.appendChild(th);
    // Fin THead
    thead.appendChild(tr);
    table.appendChild(thead);

    // TBody
    tbody = document.createElement('tbody');
    // Pour chaque entree du tableau
    for (let i = (page - 1) * records_per_page; i < (page * records_per_page)
	    && i < files.length; i++) {
	// Nouvelle ligne
	tr = document.createElement('tr');
	tr.setAttribute('class', 'link-tab-list');
	// Td Date
	td = document.createElement('td');
	td.innerText = files[i].uploadDate;
	tr.appendChild(td);
	// Td Weight (unite en fonction valeur)
	td = document.createElement('td');
	if (files[i].weight <= 1) {
	    td.innerText = files[i].weight + ' octet';
	} else if (files[i].weight < 1024) {
	    td.innerText = files[i].weight + ' octets';
	} else if (files[i].weight < (1024 * 1024)) {
	    td.innerText = (files[i].weight / 1024).toFixed(2) + ' ko';
	} else {
	    td.innerText = (files[i].weight / (1024 * 1024)).toFixed(2) + ' Mo';
	}
	tr.appendChild(td);
	// Td Name
	td = document.createElement('td');
	if (files[i].name.length < 20) {
	    td.innerText = files[i].name;
	} else {
	    td.innerText = files[i].name.substring(0, 20) + '...';
	}
	tr.appendChild(td);
	// Td Action
	td = document.createElement('td');
	a = document.createElement('a');
	url = path + '/files?action=get&id=' + files[i].id;
	a.setAttribute('href', url);
	a
		.setAttribute('class',
			'btn btn-primary btn-xs btn-perso small-marged');
	img = document.createElement('i');
	img.setAttribute('class', 'fa fa-fw fa-download white');
	a.appendChild(img);
	td.appendChild(a);
	a = document.createElement('a');
	a.setAttribute('class', 'btn btn-danger btn-xs btn-perso small-marged');
	a.setAttribute('data-toggle', 'modal');
	value = '.modal-confirm-' + files[i].id;
	a.setAttribute('data-target', value);
	a.setAttribute('data-backdrop', 'static');
	img = document.createElement('i');
	img.setAttribute('class', 'fa fa-fw fa-times white');
	a.appendChild(img);
	td.appendChild(a);
	tr.appendChild(td);
	// Ajoute la ligne
	tbody.appendChild(tr);
	// Modal confirmation de suppression
	div = document.createElement('div');
	value = 'modal fade modal-confirm-' + files[i].id;
	div.setAttribute('class', value);
	div.setAttribute('tabindex', '-1');
	div.setAttribute('role', 'dialog');
	div.setAttribute('aria-labelledby', 'modal_label');
	div2 = document.createElement('div');
	div2.setAttribute('class', 'modal-dialog modal-sm');
	div2.setAttribute('role', 'document');
	div3 = document.createElement('div');
	div3.setAttribute('class', 'modal-content');
	div4 = document.createElement('div');
	div4.setAttribute('class', 'modal-header');
	button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('class', 'close');
	button.setAttribute('data-dismiss', 'modal');
	button.setAttribute('aria-label', 'Close');
	span = document.createElement('span');
	span.setAttribute('aria-hidden', 'true');
	img = document.createElement('i');
	img.setAttribute('class', 'fa fa-fw fa-times');
	span.appendChild(img);
	button.appendChild(span);
	div4.appendChild(button);
	h4 = document.createElement('h4');
	h4.setAttribute('class', 'modal-title');
	h4.setAttribute('id', 'modal_label');
	h4.innerText = 'Confirmation';
	div4.appendChild(h4);
	div3.appendChild(div4);
	div4 = document.createElement('div');
	div4.setAttribute('class', 'modal-body');
	p = document.createElement('p');
	p.innerText = 'Voulez-vous confirmer la suppression de ce fichier ?';
	div4.appendChild(p);
	div3.appendChild(div4);
	div4 = document.createElement('div');
	div4.setAttribute('class', 'modal-footer center');
	a = document.createElement('a');
	value = path + '/files?action=delete&id=' + files[i].id;
	a.setAttribute('href', value);
	a.setAttribute('class', 'btn btn-danger btn-sm btn-perso btn-fixed');
	a.innerText = 'Supprimer';
	div4.appendChild(a);
	button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('class',
		'btn btn-primary btn-sm btn-perso btn-fixed');
	button.setAttribute('data-dismiss', 'modal');
	button.innerText = 'Annuler';
	div4.appendChild(button);
	div3.appendChild(div4);
	div2.appendChild(div3);
	div.appendChild(div2);
	tbody.appendChild(div);
	// Fin TBody
    }

    // Fin de tableau
    table.appendChild(tbody);

    // Retourne le tableau
    return table;
}

// Calcul le nombre de page total
function numPages() {
    return Math.ceil(files.length / records_per_page);
}

// Charge la premiere page par defaut + horloge
window.onload = function() {
    changePage(1);
    horloge('horloge');
};