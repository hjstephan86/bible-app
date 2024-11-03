function init() {
	var selectSearch = document.getElementById("selectSearch");
	var submitSearch = document.getElementById("submitSearch");
	populateSelectSearch(selectSearch);
	if (inputSearch.value != '') {
		selectSearch.disabled = false;
		submitSearch.disabled = false;
	}
	// Set the bible name
	document.getElementById('inputBibleName').value = bibleName;
}

function doSubmit() {
	document.getElementById('inputBibleName').value = document.getElementById('selectBibleName').value;
	document.getElementById('searchForm').submit();
}

function populateSelectSearch(selectSearch) {
	addOption(selectSearch, "Alle");
	addOption(selectSearch, "AT");
	addOption(selectSearch, "NT");
	for (var i = 0; i < books.length; i++) {
		addOption(selectSearch, books[i]);
	}
	if (selectedSearchValue != null) {
		selectSearch.value = selectedSearchValue;
	}
}

function addOption(selectSearch, name) {
	selectSearch.add(new Option(name, name));
}

var inputSearch = document.getElementById("inputSearch");

inputSearch.addEventListener("input", function (event) {
	var selectSearch = document.getElementById("selectSearch");
	var submitSearch = document.getElementById("submitSearch");
	if (inputSearch.value != '') {
		selectSearch.disabled = false;
		submitSearch.disabled = false;
	} else {
		selectSearch.disabled = true;
		submitSearch.disabled = true;
	}
});