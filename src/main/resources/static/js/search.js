function init() {
	const searchSelect = document.getElementById("selectSearch");
	const searchSubmit = document.getElementById("submitSearch");

	populateSelectSearch(searchSelect);

	// Simplify conditional logic
	const isInputEmpty = !inputSearch.value; // Assuming 'inputSearch' is defined elsewhere
	searchSelect.disabled = isInputEmpty;
	searchSubmit.disabled = isInputEmpty;

	document.getElementById('inputBibleName').value = bibleName;
}

function doSubmit() {
	document.getElementById('inputBibleName').value = document.getElementById('selectBibleName').value;
	document.getElementById('searchForm').submit();
}

function populateSelectSearch(searchSelect) {
	// Use an array of options for cleaner code
	const options = ["Alle", "AT", "NT", ...books]; // Assuming 'books' is defined elsewhere

	options.forEach(option => {
		searchSelect.add(new Option(option, option));
	});

	if (selectedSearchValue) { // Assuming 'selectedSearchValue' is defined elsewhere
		searchSelect.value = selectedSearchValue;
	}
}

// Remove the unnecessary addOption function

const inputSearch = document.getElementById("inputSearch");

inputSearch.addEventListener("input", () => {
	const searchSelect = document.getElementById("selectSearch");
	const searchSubmit = document.getElementById("submitSearch");

	// Simplify logic using a single variable
	const isInputEmpty = !inputSearch.value;
	searchSelect.disabled = isInputEmpty;
	searchSubmit.disabled = isInputEmpty;
});