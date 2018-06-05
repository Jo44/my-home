// Attributs
let current_page = 1;
let records_per_page = 10;
let table;

// Lors du changement du nombre d'entree affichee par page
function changePerPage() {
    records_per_page = document.getElementById("selectPerPage").value;
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

    // Si au moins une entree
    if (playlists.length > 0) {

        // Recuperation du DOM
        let btn_next = document.getElementById("btn_next");
        let btn_prev = document.getElementById("btn_prev");
        let listing_table = document.getElementById("listingTable");
        let page_span = document.getElementById("page");

        // Si au moins une entree, affiche le nombre d'entree par page
        document.getElementById("selectPerPage").style.visibility = "visible";

        // Valide la page actuelle
        if (page < 1)
            page = 1;
        if (page > numPages())
            page = numPages();

        // Assemble le tableau HTML
        table = assembleHtml(page);

        // Ajout du tableau dans l'HTML
        listing_table.appendChild(table);

        // Ajout du numero de la page actuelle
        page_span.innerHTML = page;

        // Affiche ou cache toute la pagination si une seule page
        let paginationBloc = document.getElementById("paginationBloc");
        if (numPages() === 1) {
            paginationBloc.style.display = "none";
        } else {
            paginationBloc.style.display = "block";
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
        document.getElementById("selectPerPage").style.visibility = "hidden";
    }
}

// Assemble le tableau HTML
function assembleHtml(page) {
    let table, thead, tbody, tr, th, td, span, label, input, p, button, a, img, h4, url, value, div, div2, div3, div4;

    // Table
    table = document.createElement('table');
    table.setAttribute('class','table table-striped');

    // THead
    thead = document.createElement('thead');
    tr = document.createElement('tr');
    // Th Title
    th = document.createElement('th');
    th.setAttribute('class','col-xs-3');
    span = document.createElement('span');
    span.setAttribute('class','title-order-bottom');
    span.innerText = 'Titre';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class','in-line');
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=title&dir=asc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=title&dir=desc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Type
    th = document.createElement('th');
    th.setAttribute('class','col-xs-3');
    span = document.createElement('span');
    span.setAttribute('class','title-order-bottom');
    span.innerText = 'Type';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class','in-line');
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=type&dir=asc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=type&dir=desc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Active
    th = document.createElement('th');
    th.setAttribute('class','col-xs-1');
    span = document.createElement('span');
    span.setAttribute('class','title-order-bottom');
    span.innerText = 'Active';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class','in-line');
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=active&dir=asc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=active&dir=desc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Date
    th = document.createElement('th');
    th.setAttribute('class','col-xs-3');
    span = document.createElement('span');
    span.setAttribute('class','title-order-bottom');
    span.innerText = 'Date d\'ajout';
    th.appendChild(span);
    div = document.createElement('div');
    div.setAttribute('class','in-line');
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=active&dir=asc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-up order-arrow small-marged in-block');
    div.appendChild(a);
    a = document.createElement('a');
    url = path + '/youtube_playlists?action=list&order-by=active&dir=desc';
    a.setAttribute('href',url);
    a.setAttribute('class','fa fa-fw fa-angle-down order-arrow small-marged in-block');
    div.appendChild(a);
    th.appendChild(div);
    tr.appendChild(th);
    // Th Action
    th = document.createElement('th');
    th.setAttribute('class','col-xs-2');
    th.innerText = 'Actions';
    tr.appendChild(th);
    // Fin THead
    thead.appendChild(tr);
    table.appendChild(thead);

    // TBody
    tbody = document.createElement('tbody');
    // Pour chaque entree du tableau
    for (let i = (page - 1) * records_per_page; i < (page * records_per_page)
    && i < playlists.length; i++) {
        // Nouvelle ligne
        tr = document.createElement('tr');
        tr.setAttribute('class','link-tab-list');
        // Td Title
        td = document.createElement('td');
        if (playlists[i].title.length < 30) {
            td.innerText = playlists[i].title;
        } else {
            td.innerText = playlists[i].title.substring(0, 29);
        }
        tr.appendChild(td);
        // Td Type
        td = document.createElement('td');
        if (playlists[i].type.length < 20) {
            td.innerText = playlists[i].type;
        } else {
            td.innerText = playlists[i].type.substring(0, 19);
        }
        tr.appendChild(td);
        // Td Active
        td = document.createElement('td');
        label = document.createElement('label');
        label.setAttribute('class','switch');
        input = document.createElement('input');
        input.setAttribute('id','checkbox');
        input.setAttribute('name','checkbox_active');
        input.setAttribute('type','checkbox');
        input.setAttribute('disabled','disabled');
        if (playlists[i].active === 'true') {
            input.setAttribute('checked','checked');
        }
        label.appendChild(input);
        span = document.createElement('span');
        span.setAttribute('class','slider');
        label.appendChild(span);
        td.appendChild(label);
        tr.appendChild(td);
        // Td Date
        td = document.createElement('td');
        td.innerText = playlists[i].createDate;
        tr.appendChild(td);
        // Td Action
        td = document.createElement('td');
        a = document.createElement('a');
        url = path + '/youtube_playlists?action=update&idPlaylist=' + playlists[i].id + '&order-by=date&dir=desc';
        a.setAttribute('href',url);
        a.setAttribute('class','btn btn-primary btn-xs btn-perso small-marged');
        img = document.createElement('i');
        img.setAttribute('class','fa fa-fw fa-pencil white');
        a.appendChild(img);
        td.appendChild(a);
        a = document.createElement('a');
        a.setAttribute('class','btn btn-danger btn-xs btn-perso small-marged');
        a.setAttribute('data-toggle','modal');
        value = '.modal-confirm-' + playlists[i].id;
        a.setAttribute('data-target',value);
        a.setAttribute('data-backdrop','static');
        img = document.createElement('i');
        img.setAttribute('class','fa fa-fw fa-times white');
        a.appendChild(img);
        td.appendChild(a);
        tr.appendChild(td);
        // Ajoute la ligne
        tbody.appendChild(tr);
        // Modal confirmation de suppression
        div = document.createElement('div');
        value = 'modal fade modal-confirm-' + playlists[i].id;
        div.setAttribute('class',value);
        div.setAttribute('tabindex','-1');
        div.setAttribute('role','dialog');
        div.setAttribute('aria-labelledby','modal_label');
        div2 = document.createElement('div');
        div2.setAttribute('class','modal-dialog modal-sm');
        div2.setAttribute('role','document');
        div3 = document.createElement('div');
        div3.setAttribute('class','modal-content');
        div4 = document.createElement('div');
        div4.setAttribute('class','modal-header');
        button = document.createElement('button');
        button.setAttribute('type','button');
        button.setAttribute('class','close');
        button.setAttribute('data-dismiss','modal');
        button.setAttribute('aria-label','Close');
        span = document.createElement('span');
        span.setAttribute('aria-hidden','true');
        img = document.createElement('i');
        img.setAttribute('class','fa fa-fw fa-times');
        span.appendChild(img);
        button.appendChild(span);
        div4.appendChild(button);
        h4 = document.createElement('h4');
        h4.setAttribute('class','modal-title');
        h4.setAttribute('id','modal_label');
        h4.innerText = 'Confirmation';
        div4.appendChild(h4);
        div3.appendChild(div4);
        div4 = document.createElement('div');
        div4.setAttribute('class','modal-body');
        p = document.createElement('p');
        p.innerText = 'Voulez-vous confirmer la suppression de cette playlist ?';
        div4.appendChild(p);
        div3.appendChild(div4);
        div4 = document.createElement('div');
        div4.setAttribute('class','modal-footer center');
        a = document.createElement('a');
        value = path + '/youtube_playlists?action=delete&idPlaylist=' + playlists[i].id;
        a.setAttribute('href',value);
        a.setAttribute('class','btn btn-danger btn-sm btn-perso btn-fixed');
        a.innerText = 'Supprimer';
        div4.appendChild(a);
        button = document.createElement('button');
        button.setAttribute('type','button');
        button.setAttribute('class','btn btn-primary btn-sm btn-perso btn-fixed');
        button.setAttribute('data-dismiss','modal');
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
    return Math.ceil(playlists.length / records_per_page);
}

// Charge la premiere page par defaut
window.onload = function() {
    changePage(1);
};