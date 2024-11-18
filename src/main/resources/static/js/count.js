function init() {
	// Use more descriptive variable names
	const bookFromInput = document.getElementById('inputBookFrom');
	const chapterFromSelect = document.getElementById('selectChapterFrom');
	const verseFromSelect = document.getElementById('selectVerseFrom');

	// Simplify conditional logic
	chapterFromSelect.disabled = verseFromSelect.disabled = !bookFromInput.value;

	if (bookFromInput.value) {
		populateChapters(bookFromInput.value, bookFromInput.name);
		chapterFromSelect.value = chapterFrom; // Assuming 'chapterFrom' is defined elsewhere
		populateVersesFrom();
		verseFromSelect.value = verseFrom; // Assuming 'verseFrom' is defined elsewhere
	}

	// Repeat for "to" fields (consider refactoring into a function)
	const bookToInput = document.getElementById('inputBookTo');
	const chapterToSelect = document.getElementById('selectChapterTo');
	const verseToSelect = document.getElementById('selectVerseTo');

	chapterToSelect.disabled = verseToSelect.disabled = !bookToInput.value;

	if (bookToInput.value) {
		populateChapters(bookToInput.value, bookToInput.name);
		chapterToSelect.value = chapterTo; // Assuming 'chapterTo' is defined elsewhere
		populateVersesTo();
		verseToSelect.value = verseTo; // Assuming 'verseTo' is defined elsewhere
	}

	checkSubmit();
	initializeWordLists();
	checkResultSize();
	document.getElementById('inputBibleName').value = bibleName;
}

const countWords = [];
const ignoreWords = [];
const sizeDelta = 10;
let currentCountSize = 0;
let currentIgnoreSize = 0;
let countResultButton = null;
let ignoreResultButton = null;

function checkResultSize() {
	countResultButton = document.getElementById("countResultButton");
	ignoreResultButton = document.getElementById("ignoreResultButton");

	currentCountSize = Math.min(countWords.length, sizeDelta);
	currentIgnoreSize = Math.min(ignoreWords.length, sizeDelta);

	// Use a helper function to set button visibility
	setButtonVisibility(countResultButton, currentCountSize, countWords.length);
	setButtonVisibility(ignoreResultButton, currentIgnoreSize, ignoreWords.length);

	fillCountTable();
	fillIgnoreTable();

	// Use addEventListener instead of onclick for better separation of concerns
	countResultButton.addEventListener("click", countResultButtonListener);
	ignoreResultButton.addEventListener("click", ignoreResultButtonListener);
}

// Helper function to set button visibility
function setButtonVisibility(button, currentSize, totalSize) {
	button.style.visibility = currentSize < totalSize ? "visible" : "hidden";
}

function countResultButtonListener() {
	if (currentCountSize + sizeDelta < countWords.length) {
		currentCountSize += sizeDelta;
	} else {
		currentCountSize = countWords.length;
		countResultButton.classList.add("disable"); // Use classList for adding classes
	}
	setButtonVisibility(countResultButton, currentCountSize, countWords.length);
	fillCountTable();
}

function ignoreResultButtonListener() {
	// Similar logic as countResultButtonListener, consider refactoring into a single function
	if (currentIgnoreSize + sizeDelta < ignoreWords.length) {
		currentIgnoreSize += sizeDelta;
	} else {
		currentIgnoreSize = ignoreWords.length;
		ignoreResultButton.classList.add("disable");
	}
	setButtonVisibility(ignoreResultButton, currentIgnoreSize, ignoreWords.length);
	fillIgnoreTable();
}

function initializeWordLists() {
	ignoreWords.length = 0; // Clear arrays instead of reassigning
	countWords.length = 0;

	if (words.length > 0) { // Assuming 'words' is defined elsewhere
		words.forEach((word, index) => {
			word.index = index;
			(word.ignore ? ignoreWords : countWords).push(word);
		});
	}
}

function fillCountTable() {
	const table = document.getElementById('wordsTable');
	table.innerHTML = "";

	if (countWords.length > 0) {
		let tableContent = "<tr><th class=\"count\">#</th><th>Gezählt</th></tr>";

		for (let i = 0; i < currentCountSize; i++) {
			tableContent += `
		  <tr>
			<td class="count">${countWords[i].count}</td>
			<td>
			  ${countWords[i].name}
			  <a href="#" onclick="removeWord('${countWords[i].index}');" style="color: #c82c1c; font-weight:bold;">&nbsp;&#8722&nbsp</a>
			</td>
		  </tr>`;
		}

		tableContent += `<tr><td colspan="2" style="color:#aaa;">${currentCountSize} von ${countWords.length}</td></tr>`;
		table.innerHTML = tableContent;
	}
}

function fillIgnoreTable() {
	// Similar logic to fillCountTable, consider refactoring to avoid duplication
	const table = document.getElementById('ignoreTable');
	table.innerHTML = "";

	if (ignoreWords.length > 0) {
		let tableContent = "<tr><th class=\"ignore\">#</th><th>Ignoriert</th></tr>";

		for (let i = 0; i < currentIgnoreSize; i++) {
			tableContent += `
		  <tr>
			<td class="ignore">${ignoreWords[i].count}</td>
			<td>
			  ${ignoreWords[i].name}
			  <a href="#" onclick="addWord('${ignoreWords[i].index}');" style="color: green; font-weight:bold;">&nbsp;+&nbsp</a>
			</td>
		  </tr>`;
		}

		tableContent += `<tr><td colspan="2" style="color:#aaa;">${currentIgnoreSize} von ${ignoreWords.length}</td></tr>`;
		table.innerHTML = tableContent;
	}
}

function removeWord(index) {
	words[index].ignore = !words[index].ignore;
	initializeWordLists();

	// Simplify logic for updating currentCountSize and currentIgnoreSize
	if (currentCountSize === countWords.length + 1) currentCountSize--;
	if (currentIgnoreSize === ignoreWords.length - 1) currentIgnoreSize++;

	// Update button visibility and state
	updateButtonState(countResultButton, currentCountSize, countWords.length);
	updateButtonState(ignoreResultButton, currentIgnoreSize, ignoreWords.length);

	fillCountTable();
	fillIgnoreTable();
}

function addWord(index) {
	// Similar logic to removeWord, consider refactoring
	words[index].ignore = !words[index].ignore;
	initializeWordLists();

	if (currentCountSize === countWords.length - 1) currentCountSize++;
	if (currentIgnoreSize === ignoreWords.length + 1) currentIgnoreSize--;

	updateButtonState(ignoreResultButton, currentIgnoreSize, ignoreWords.length);
	updateButtonState(countResultButton, currentCountSize, countWords.length);

	fillCountTable();
	fillIgnoreTable();
}

// Helper function to update button state
function updateButtonState(button, currentSize, totalSize) {
	if (currentSize <= sizeDelta && currentSize === totalSize) {
		button.style.visibility = "hidden";
	} else if (currentSize === totalSize) {
		button.classList.add("disable");
	} else if (currentSize > sizeDelta && button.style.visibility === "hidden") {
		button.style.visibility = "visible";
		button.className = "wordsResultButton"; // Reset class name
	}
}

function populateVersesFrom() {
	const bookFromInput = document.getElementById('inputBookFrom');
	const chapterFromSelect = document.getElementById('selectChapterFrom');
	const verseFromSelect = document.getElementById('selectVerseFrom');

	verseFromSelect.disabled = false;
	verseFromSelect.options.length = 0;

	const bookValue = bookFromInput.value;
	const chapterValue = parseInt(chapterFromSelect.value, 10); // Parse to integer

	const numOfVerses = verses[books.indexOf(bookValue)][chapterValue - 1]; // Assuming 'verses' and 'books' are defined elsewhere
	for (let v = 1; v <= numOfVerses; v++) {
		verseFromSelect.options.add(new Option(v));
	}
	checkSubmit();
}

function populateVersesTo() {
	// Similar logic to populateVersesFrom, consider refactoring
	const bookToInput = document.getElementById('inputBookTo');
	const chapterToSelect = document.getElementById('selectChapterTo');
	const verseToSelect = document.getElementById('selectVerseTo');

	verseToSelect.disabled = false;
	verseToSelect.options.length = 0;

	const bookValue = bookToInput.value;
	const chapterValue = parseInt(chapterToSelect.value, 10);

	const numOfVerses = verses[books.indexOf(bookValue)][chapterValue - 1];
	for (let v = 1; v <= numOfVerses; v++) {
		verseToSelect.options.add(new Option(v));
	}
	checkSubmit();
}

function populateChapters(bookValue, inputName) {
	const chapterSelect = inputName.endsWith('From')
		? document.getElementById('selectChapterFrom')
		: document.getElementById('selectChapterTo');

	const verseSelect = inputName.endsWith('From')
		? document.getElementById('selectVerseFrom')
		: document.getElementById('selectVerseTo');

	if (chapterSelect) {
		chapterSelect.disabled = false;
		chapterSelect.options.length = 0;

		const numOfChapters = chapters[books.indexOf(bookValue)]; // Assuming 'chapters' and 'books' are defined elsewhere
		for (let c = 1; c <= numOfChapters; c++) {
			chapterSelect.options.add(new Option(c));
		}

		if (verseSelect && verseSelect.value) {
			verseSelect.disabled = true;
			verseSelect.options.length = 0;
		}
	}
	checkSubmit();
}


function autocomplete(input, possibleValues) {
	let currentFocus;

	input.addEventListener("input", function (e) {
		let a, b, i, val = this.value;
		closeAllLists();
		if (!val) { return false; }
		currentFocus = -1;
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		this.parentNode.appendChild(a);

		for (i = 0; i < possibleValues.length; i++) {
			if (possibleValues[i].toUpperCase().includes(val.toUpperCase())) {
				b = document.createElement("DIV");
				const beginIndex = possibleValues[i].toUpperCase().indexOf(val.toUpperCase());
				if (beginIndex === 0) {
					b.innerHTML = `<strong>${possibleValues[i].substring(0, val.length)}</strong>`;
					b.innerHTML += possibleValues[i].substring(val.length);
				} else {
					b.innerHTML = possibleValues[i].substring(0, beginIndex);
					b.innerHTML += `<strong>${possibleValues[i].substring(beginIndex, beginIndex + val.length)}</strong>`;
					b.innerHTML += possibleValues[i].substring(beginIndex + val.length);
				}
				b.innerHTML += `<input type='hidden' value='${possibleValues[i]}'>`;

				b.addEventListener("click", function (e) {
					input.value = this.getElementsByTagName("input")[0].value;
					populateChapters(input.value, input.name);
					if (input.name.endsWith('From')) {
						populateVersesFrom();
					} else if (input.name.endsWith('To')) {
						populateVersesTo();
					}
					closeAllLists();
				});
				a.appendChild(b);
			}
		}
	});

	input.addEventListener("keydown", function (e) {
		let x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode === 40) {
			currentFocus++;
			addActive(x);
		} else if (e.keyCode === 38) {
			currentFocus--;
			addActive(x);
		} else if (e.keyCode === 13) {
			e.preventDefault();
			if (currentFocus > -1) {
				if (x) x[currentFocus].click();
			}
		}
	});

	function addActive(x) {
		if (!x) return false;
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		x[currentFocus].classList.add("autocomplete-active");
	}

	function removeActive(x) {
		for (let i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}

	function closeAllLists(elmnt) {
		const x = document.getElementsByClassName("autocomplete-items");
		for (let i = 0; i < x.length; i++) {
			if (elmnt !== x[i] && elmnt !== input) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
	}

	document.addEventListener("click", function (e) {
		closeAllLists(e.target);
	});
}


function checkSubmit() {
	const bookFromInput = document.getElementById('inputBookFrom');
	const chapterFromSelect = document.getElementById('selectChapterFrom');
	const verseFromSelect = document.getElementById('selectVerseFrom');
	const bookToInput = document.getElementById('inputBookTo');
	const chapterToSelect = document.getElementById('selectChapterTo');
	const verseToSelect = document.getElementById('selectVerseTo');

	const bookFromIndex = books.indexOf(bookFromInput.value);
	const bookToIndex = books.indexOf(bookToInput.value);
	const chapterFromValue = parseInt(chapterFromSelect.value, 10);
	const chapterToValue = parseInt(chapterToSelect.value, 10);
	const verseFromValue = parseInt(verseFromSelect.value, 10);
	const verseToValue = parseInt(selectVerseTo.value, 10);

	// Simplify condition for enabling/disabling the submit button
	const isValid =
		bookFromInput.value && chapterFromValue && verseFromValue &&
		bookToInput.value && chapterToValue && verseToValue && (
			(bookFromIndex === bookToIndex && chapterFromValue === chapterToValue && verseFromValue <= verseToValue) ||
			(bookFromIndex === bookToIndex && chapterFromValue < chapterToValue) ||
			(bookFromIndex < bookToIndex)
		);

	document.getElementById('submitCount').disabled = !isValid;
}

function doSubmit() {
	document.getElementById('inputBibleName').value = document.getElementById('selectBibleName').value;
	document.getElementById('countForm').submit();
}