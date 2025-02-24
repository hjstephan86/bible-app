function init() {
	const bookInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');
	const verseSelect = document.getElementById('selectVerse');

	chapterSelect.disabled = !bookInput.value;

	if (bookInput.value) {
		populateChapters(bookInput.value);
		chapterSelect.classList.remove("white");
		chapterSelect.value = selectedChapterValue;
		populateVerses()
		verseSelect.classList.remove("white");
		verseSelect.value = selectedVerseValue;
	}

	const bibleNameInput = document.getElementById('inputBibleName');
	if (bibleNameInput) {
		bibleNameInput.value = bibleName;
	}
}

function populateChapters(bookValue) {
	const chapterSelect = document.getElementById('selectChapter');
	chapterSelect.disabled = false;
	chapterSelect.classList.add("white");
	chapterSelect.options.length = 0;

	const chapterCount = chapters[books.indexOf(bookValue)];
	for (let c = 1; c <= chapterCount; c++) {
		chapterSelect.options.add(new Option(c));
	}
}

function populateVerses(event) {
	const bookFromInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');
	const verseSelect = document.getElementById('selectVerse');

	verseSelect.disabled = false;
	verseSelect.classList.add("white");
	verseSelect.options.length = 0;

	const bookValue = bookFromInput.value;
	const chapterValue = parseInt(chapterSelect.value, 10); // Parse to integer

	const numOfVerses = verses[books.indexOf(bookValue)][chapterValue - 1]; // Assuming 'verses' and 'books' are defined elsewhere
	for (let v = 1; v <= numOfVerses; v++) {
		verseSelect.options.add(new Option(v));
	}

	if (event == 'onchange') {
		doSubmit();
	}
}

function autocomplete(input, possibleValues) {
	let currentFocus;

	input.addEventListener("input", function (e) {
		let a, b, i, val = this.value;
		let valÄÖÜ = val.toUpperCase().replaceAll('A', 'Ä').replaceAll('O', 'Ö').replaceAll('U', 'Ü');
		closeAllLists();
		if (!val) { return false; }
		currentFocus = -1;

		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		this.parentNode.appendChild(a);

		for (i = 0; i < possibleValues.length; i++) {
			if (possibleValues[i].toUpperCase().includes(val.toUpperCase()) || possibleValues[i].toUpperCase().includes(valÄÖÜ.toUpperCase())) {
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
					populateChapters(input.value);
					populateVerses();
					doSubmit();
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

function doSubmit() {
	const bibleNameSelect = document.getElementById('selectBibleName');
	if (bibleNameSelect) {
		document.getElementById('inputBibleName').value = bibleNameSelect.value;
	}
	document.getElementById('parallelForm').submit();
}