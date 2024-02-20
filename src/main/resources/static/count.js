function init() {
	// Initialize the "from" form fields
	var inputBookFrom = document.getElementById('inputBookFrom');
	var selectChapterFrom = document.getElementById('selectChapterFrom');
	var selectVerseFrom = document.getElementById('selectVerseFrom');

	if (inputBookFrom.value == '') {
		selectChapterFrom.disabled = true;
		selectChapterFrom.options.add(new Option("Kapitel"));

		selectVerseFrom.disabled = true;
		selectVerseFrom.options.add(new Option("Vers"));
	} else {
		populateChapters(inputBookFrom.value, inputBookFrom.name);
		selectChapterFrom.value = chapterFrom;
		populateVersesFrom();
		selectVerseFrom.value = verseFrom;
	}
	// Initialize the "to" form fields
	var inputBookTo = document.getElementById('inputBookTo');
	var selectChapterTo = document.getElementById('selectChapterTo');
	var selectVerseTo = document.getElementById('selectVerseTo');

	if (inputBookTo.value == '') {
		selectChapterTo.disabled = true;
		selectChapterTo.options.add(new Option("Kapitel"));

		selectVerseTo.disabled = true;
		selectVerseTo.options.add(new Option("Vers"));
	} else {
		populateChapters(inputBookTo.value, inputBookTo.name);
		selectChapterTo.value = chapterTo;
		populateVersesTo();
		selectVerseTo.value = verseTo;
	}
	// Initialize the submit button
	checkSubmit();
	// initialize word lists
	initializeWordLists();
	// Check the result size
	checkResultSize();
	// Set the bible name
	document.getElementById('inputBibleName').value = bibleName;
}

function setBibleName() {
	if (!document.getElementById('submitCount').disabled) {
		document.getElementById('inputBibleName').value = document.getElementById('selectBibleName').value;
		document.getElementById('countForm').submit();
	} else {
		document.getElementById('bibleNameForm').submit();
	}
}

var countWords = [];
var ignoreWords = [];

var sizeDelta = 10;
var currentCountSize = 0;
var currentIgnoreSize = 0;

var countResultButton = null;
var ignoreResultButton = null;

function checkResultSize() {
	countResultButton = document.getElementById("countResultButton");
	ignoreResultButton = document.getElementById("ignoreResultButton");

	currentCountSize = countWords.length < sizeDelta ? countWords.length : sizeDelta;
	currentIgnoreSize = ignoreWords.length < sizeDelta ? ignoreWords.length : sizeDelta;
	countResultButton.style.visibility = currentCountSize < countWords.length ? "visible" : "hidden";
	ignoreResultButton.style.visibility = currentIgnoreSize < ignoreWords.length ? "visible" : "hidden";

	fillCountTable();
	fillIgnoreTable();

	countResultButton.addEventListener("click", countResultButtonListener);
	ignoreResultButton.addEventListener("click", ignoreResultButtonListener);
}

function countResultButtonListener() {
	if (currentCountSize + sizeDelta < countWords.length) {
		currentCountSize += sizeDelta;
		countResultButton.style.visibility = "visible";
		countResultButton.className = "wordsResultButton";
		fillCountTable();
	} else if (currentCountSize < countWords.length) {
		currentCountSize = countWords.length;
		countResultButton.className += " disable";
		fillCountTable();
	}
}

function ignoreResultButtonListener() {
	if (currentIgnoreSize + sizeDelta < ignoreWords.length) {
		currentIgnoreSize += sizeDelta;
		ignoreResultButton.style.visibility = "visible";
		ignoreResultButton.className = "wordsResultButton";
		fillIgnoreTable();
	} else if (currentIgnoreSize < ignoreWords.length) {
		currentIgnoreSize = ignoreWords.length;
		ignoreResultButton.className += " disable";
		fillIgnoreTable();
	}
}

function initializeWordLists() {
	ignoreWords = [];
	countWords = [];
	if (words.length > 0) {
		var indexIgnore = 0;
		var indexCount = 0;
		for (var i = 0; i < words.length; i++) {
			words[i].index = i;
			if (words[i].ignore) {
				ignoreWords[indexIgnore] = words[i];
				indexIgnore++;
			} else {
				countWords[indexCount] = words[i];
				indexCount++;
			}
		}
	}
}

function fillCountTable() {
	var table = document.getElementById('wordsTable');
	table.innerHTML = "";
	if (countWords.length > 0) {
		var t = "";
		var tr = "<tr><th class=\"count\">#</th><th>Gezählt</th></tr>";
		t += tr;
		for (var i = 0; i < currentCountSize; i++) {
			var tr = "<tr><td class=\"count\">" + countWords[i].count + "</td>";
			tr += "<td>" + countWords[i].name + "<a href=\"javascript:void(0)\" onclick=\"removeWord('" + countWords[i].index + "');\"style=\"color: #c82c1c; font-weight:bold;\">&nbsp&#8722&nbsp</a></td></tr>";
			t += tr;
		}
		var tr = "<tr><td colspan=\"2\" style=\"color:#aaa;\">" + currentCountSize + " von " + countWords.length + "</td></tr>";
		t += tr;
		table.innerHTML += t;
	}
}

function fillIgnoreTable() {
	var table = document.getElementById('ignoreTable');
	table.innerHTML = "";
	if (ignoreWords.length > 0) {
		var t = "";
		var tr = "<tr><th class=\"count\">#</th><th>Ignoriert</th></tr>";
		t += tr;
		for (var i = 0; i < currentIgnoreSize; i++) {
			var tr = "<tr><td class=\"count\">" + ignoreWords[i].count + "</td>";
			tr += "<td>" + ignoreWords[i].name + "<a href=\"javascript:void(0)\" onclick=\"addWord('" + ignoreWords[i].index + "');\" style=\"color: green; font-weight:bold;\">&nbsp+&nbsp</a></td></tr>";
			t += tr;
		}
		var tr = "<tr><td colspan=\"2\" style=\"color:#aaa;\">" + currentIgnoreSize + " von " + ignoreWords.length + "</td></tr>";
		t += tr;
		table.innerHTML += t;
	}
}

function removeWord(index) {
	words[index].ignore = !words[index].ignore;
	initializeWordLists();

	if (currentCountSize == countWords.length + 1) {
		currentCountSize--;
	}
	if (currentIgnoreSize == ignoreWords.length - 1) {
		currentIgnoreSize++;
	}

	if (currentCountSize <= sizeDelta && currentCountSize == countWords.length) {
		countResultButton.style.visibility = "hidden";
	} else if (currentCountSize == countWords.length) {
		countResultButton.className += " disable";
	}
	if (currentIgnoreSize > sizeDelta && ignoreResultButton.style.visibility == "hidden") {
		ignoreResultButton.style.visibility = "visible";
		ignoreResultButton.className = "wordsResultButton";
		currentIgnoreSize = sizeDelta;
	}

	fillCountTable();
	fillIgnoreTable();
}

function addWord(index) {
	words[index].ignore = !words[index].ignore;
	initializeWordLists();

	if (currentCountSize == countWords.length - 1) {
		currentCountSize++;
	}
	if (currentIgnoreSize == ignoreWords.length + 1) {
		currentIgnoreSize--;
	}

	if (currentIgnoreSize <= sizeDelta && currentIgnoreSize == ignoreWords.length) {
		ignoreResultButton.style.visibility = "hidden";
	} else if (currentIgnoreSize == ignoreWords.length) {
		ignoreResultButton.className += " disable";
	}
	if (currentCountSize > sizeDelta && countResultButton.style.visibility == "hidden") {
		countResultButton.style.visibility = "visible";
		countResultButton.className = "wordsResultButton";
		currentCountSize = sizeDelta;
	}

	fillCountTable();
	fillIgnoreTable();
}

function populateVersesFrom() {
	var inputBookFrom = document.getElementById('inputBookFrom');
	var selectChapterFrom = document.getElementById('selectChapterFrom');
	var selectVerseFrom = document.getElementById('selectVerseFrom');

	selectVerseFrom.disabled = false;
	selectVerseFrom.options.length = 0;
	selectVerseFrom.options.add(new Option("Vers"));

	var bookValue = inputBookFrom.value;
	var chapterValue = selectChapterFrom.value;

	var numOfVerses = verses[books.indexOf(bookValue)][chapterValue - 1];
	for (var v = 1; v <= numOfVerses; v++) {
		selectVerseFrom.options.add(new Option(v));
	}
	checkSubmit();
}

function populateVersesTo() {
	var inputBookTo = document.getElementById('inputBookTo');
	var selectChapterTo = document.getElementById('selectChapterTo');
	var selectVerseTo = document.getElementById('selectVerseTo');

	selectVerseTo.disabled = false;
	selectVerseTo.options.length = 0;
	selectVerseTo.options.add(new Option("Vers"));

	var bookValue = inputBookTo.value;
	var chapterValue = selectChapterTo.value;

	var numOfVerses = verses[books.indexOf(bookValue)][chapterValue - 1];
	for (var v = 1; v <= numOfVerses; v++) {
		selectVerseTo.options.add(new Option(v));
	}
	checkSubmit();
}


function populateChapters(bookValue, inputName) {
	if (inputName.endsWith('From')) {
		var selectChapterFrom = document.getElementById('selectChapterFrom');
		if (selectChapterFrom != null) {
			selectChapterFrom.disabled = false;
			selectChapterFrom.options.length = 0;
			selectChapterFrom.options.add(new Option("Kapitel"));

			var numOfChapter = chapters[books.indexOf(bookValue)];
			for (var c = 1; c <= numOfChapter; c++) {
				selectChapterFrom.options.add(new Option(c));
			}

			// In case the chapters are populated again, disable the verse selection in case a verse was selected.
			// If the user selected a chapter, the verse selection will be activated again and verses populated.
			var selectVerseFrom = document.getElementById('selectVerseFrom');
			if (selectVerseFrom != null && selectVerseFrom.value != 'Verse') {
				selectVerseFrom.disabled = true;
				selectVerseFrom.options.length = 0;
				selectVerseFrom.options.add(new Option("Vers"));
			}
		}
	} else if (inputName.endsWith('To')) {
		var selectChapterTo = document.getElementById('selectChapterTo');
		if (selectChapterTo != null) {
			selectChapterTo.disabled = false;
			selectChapterTo.options.length = 0;
			selectChapterTo.options.add(new Option("Kapitel"));

			var numOfChapter = chapters[books.indexOf(bookValue)];
			for (var c = 1; c <= numOfChapter; c++) {
				selectChapterTo.options.add(new Option(c));
			}

			// In case the chapters are populated again, disable the verse selection in case a verse was selected.
			// If the user selected a chapter, the verse selection will be activated again and verses populated.
			var selectVerseTo = document.getElementById('selectVerseTo');
			if (selectVerseTo != null && selectVerseTo.value != 'Verse') {
				selectVerseTo.disabled = true;
				selectVerseTo.options.length = 0;
				selectVerseTo.options.add(new Option("Vers"));
			}
		}
	} else { }
	checkSubmit();
}


function autocomplete(input, possibleValues) {
	/*the autocomplete function takes two arguments,
	the text field element and an array of possible autocompleted values:*/
	var currentFocus;
	/*execute a function when someone writes in the text field:*/
	input.addEventListener("input", function(e) {
		var a, b, i, val = this.value;
		/*close any already open lists of autocompleted values*/
		closeAllLists();
		if (!val) { return false; }
		currentFocus = -1;
		/*create a DIV element that will contain the items (values):*/
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(a);
		/*for each item in the array...*/
		for (i = 0; i < possibleValues.length; i++) {
			/*check if the item starts with the same letters as the text field value:*/
			if (possibleValues[i].toUpperCase().includes(val.toUpperCase()) /*arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()*/) {
				/*create a DIV element for each matching element:*/
				b = document.createElement("DIV");
				/*make the matching letters bold:*/
				var beginIndex = possibleValues[i].toUpperCase().indexOf(val.toUpperCase());
				if (beginIndex == 0) {
					b.innerHTML = "<strong>" + possibleValues[i].substring(0, val.length) + "</strong>";
					b.innerHTML += possibleValues[i].substring(val.length);
				} else {
					b.innerHTML = possibleValues[i].substring(0, beginIndex);
					b.innerHTML += "<strong>" + possibleValues[i].substring(beginIndex, beginIndex + val.length) + "</strong>";
					b.innerHTML += possibleValues[i].substring(beginIndex + val.length);
				}
				/*insert a input field that will hold the current array item's value:*/
				b.innerHTML += "<input type='hidden' value='" + possibleValues[i] + "'>";
				/*execute a function when someone clicks on the item value (DIV element):*/
				b.addEventListener("click", function(e) {
					/*insert the value for the autocomplete text field:*/
					input.value = this.getElementsByTagName("input")[0].value;
					populateChapters(input.value, input.name);
					/*close the list of autocompleted values,
					(or any other open lists of autocompleted values:*/
					closeAllLists();
				});
				a.appendChild(b);
			}
		}
	});
	/*execute a function presses a key on the keyboard:*/
	input.addEventListener("keydown", function(e) {
		var x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode == 40) {
			/*If the arrow DOWN key is pressed,
			increase the currentFocus variable:*/
			currentFocus++;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 38) { //up
			/*If the arrow UP key is pressed,
			decrease the currentFocus variable:*/
			currentFocus--;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 13) {
			/*If the ENTER key is pressed, prevent the form from being submitted,*/
			e.preventDefault();
			if (currentFocus > -1) {
				/*and simulate a click on the "active" item:*/
				if (x) x[currentFocus].click();
			}
		}
	});

	function addActive(x) {
		/*a function to classify an item as "active":*/
		if (!x) return false;
		/*start by removing the "active" class on all items:*/
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		/*add class "autocomplete-active":*/
		x[currentFocus].classList.add("autocomplete-active");
	}
	function removeActive(x) {
		/*a function to remove the "active" class from all autocomplete items:*/
		for (var i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}
	function closeAllLists(elmnt) {
		/*close all autocomplete lists in the document,
		except the one passed as an argument:*/
		var x = document.getElementsByClassName("autocomplete-items");
		for (var i = 0; i < x.length; i++) {
			if (elmnt != x[i] && elmnt != input) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
	}
	/*execute a function when someone clicks in the document:*/
	document.addEventListener("click", function(e) {
		closeAllLists(e.target);
	});
}

function checkSubmit() {
	var inputBookFrom = document.getElementById('inputBookFrom');
	var selectChapterFrom = document.getElementById('selectChapterFrom');
	var selectVerseFrom = document.getElementById('selectVerseFrom');
	var inputBookTo = document.getElementById('inputBookTo');
	var selectChapterTo = document.getElementById('selectChapterTo');
	var selectVerseTo = document.getElementById('selectVerseTo');

	if (inputBookFrom.value != '' && selectChapterFrom.value != 'Kapitel' && selectVerseFrom.value != 'Vers' &&
		inputBookTo.value != '' && selectChapterTo.value != 'Kapitel' && selectVerseTo.value != 'Vers'
		&&
		(
			(books.indexOf(inputBookFrom.value) == books.indexOf(inputBookTo.value) && selectChapterFrom.value == selectChapterTo.value && selectVerseFrom.value <= selectVerseTo.value)
			|| (books.indexOf(inputBookFrom.value) == books.indexOf(inputBookTo.value) && selectChapterFrom.value < selectChapterTo.value)
			|| (books.indexOf(inputBookFrom.value) < books.indexOf(inputBookTo.value))
		)
	) {
		document.getElementById('submitCount').disabled = false;
	} else {
		document.getElementById('submitCount').disabled = true;
	}
}

function doSubmit() {
	var selectVerseFrom = document.getElementById('selectVerseFrom');
	var selectVerseTo = document.getElementById('selectVerseTo');

	if (/*selectChapterFrom.value != 'Kapitel' && selectChapterTo.value != 'Kapitel' &&*/
		selectVerseFrom.value != 'Vers' && selectVerseTo.value != 'Vers') {
		document.getElementById('countForm').submit();
	}
}