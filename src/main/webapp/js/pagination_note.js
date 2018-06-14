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
    if (notes.length > 0) {

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
    let table, thead, tbody, tr, th, td, span, a, url, value, div;

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
    span.innerText = 'Date';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=date&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=date&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Title
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-4');
    span = document.createElement('span');
    span.setAttribute('class', 'title-order-bottom');
    span.innerText = 'Titre';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=title&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=title&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Message
    th = document.createElement('th');
    th.setAttribute('class', 'col-xs-5');
    span = document.createElement('span');
    span.setAttribute('class', 'title-order-bottom');
    span.innerText = 'Message';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class', 'in-line');
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=message&dir=asc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/notes?action=list&order-by=message&dir=desc';
    a.setAttribute('href', url);
    a.setAttribute('class',
	    'fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Fin THead
    thead.appendChild(tr);
    table.appendChild(thead);

    // TBody
    tbody = document.createElement('tbody');
    // Pour chaque entree du tableau
    for (let i = (page - 1) * records_per_page; i < (page * records_per_page)
	    && i < notes.length; i++) {
	// Nouvelle ligne
	tr = document.createElement('tr');
	tr.setAttribute('class', 'link-tab-list');
	// Td Date
	td = document.createElement('td');
	a = document.createElement('a');
	value = path + '/notes?action=details&id=' + notes[i].id;
	a.setAttribute('href', value);
	a.innerText = notes[i].saveDate;
	td.appendChild(a);
	tr.appendChild(td);
	// Td Title
	td = document.createElement('td');
	a = document.createElement('a');
	value = path + '/notes?action=details&id=' + notes[i].id;
	a.setAttribute('href', value);
	if (notes[i].title.length < 30) {
	    a.innerText = notes[i].title;
	} else {
	    a.innerText = notes[i].title.substring(0, 29) + '...';
	}
	td.appendChild(a);
	tr.appendChild(td);
	// Td Message
	td = document.createElement('td');
	a = document.createElement('a');
	value = path + '/notes?action=details&id=' + notes[i].id;
	a.setAttribute('href', value);
	if (notes[i].message.length < 40) {
	    a.innerText = notes[i].message;
	} else {
	    a.innerText = notes[i].message.substring(0, 39) + '...';
	}
	td.appendChild(a);
	tr.appendChild(td);
	// Ajoute la ligne
	tbody.appendChild(tr);
	// Fin TBody
    }

    // Fin de tableau
    table.appendChild(tbody);

    // Retourne le tableau
    return table;
}

// Calcul le nombre de page total
function numPages() {
    return Math.ceil(notes.length / records_per_page);
}

// Charge la premiere page par defaut + horloge
window.onload = function() {
    changePage(1);
    horloge('horloge');
};