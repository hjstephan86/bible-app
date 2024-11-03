function init() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value == '') {
		selectChapter.disabled = true;
	} else {
		populateChapters(inputBook.value);
		selectChapter.value = selectedChapterValue;
	}
	// Initialize the navigation
	initNavigation();
	// Set the bible name
	inputBibleName = document.getElementById('inputBibleName');
	if (inputBibleName != null) {
		inputBibleName.value = bibleName;
	}
	// Go to a specific verse, if possible, e.g., from Klagelieder 3, 8, "Gebet" to 1. Könige 8, 29
	const urlParams = new URLSearchParams(window.location.search);
	const verse = parseInt(urlParams.get('verse'));
	if (verse != null && verse > 0) {
		// For simplicity just check the lower bound here
		var rectFirstElement = document.getElementById('1').getBoundingClientRect();
		var rectScrollTo = document.getElementById(verse).getBoundingClientRect();
		window.scrollBy(0, (rectScrollTo.top - rectFirstElement.top));
	}
}

function initNavigation() {
	var spanPrev = document.getElementById('spanPrev');
	var spanNext = document.getElementById('spanNext');
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && parseInt(selectChapter.value) < parseInt(chapters[books.indexOf(inputBook.value)])) {
		spanNext.className = "navigation"
	} else {
		spanNext.className += " disable";
	}
	if (inputBook.value != '' && parseInt(selectChapter.value) > 1) {
		spanPrev.className = "navigation"
	} else {
		spanPrev.className += " disable";
	}
}

function goToPrevChapter() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && parseInt(selectChapter.value) > 1) {
		selectChapter.value = (parseInt(selectChapter.value) - 1).toString();
		document.getElementById('readForm').submit();
	}
}

function goToNextChapter() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && parseInt(selectChapter.value) < parseInt(chapters[books.indexOf(inputBook.value)])) {
		selectChapter.value = (parseInt(selectChapter.value) + 1).toString();
		document.getElementById('readForm').submit();
	}
}

function populateChapters(bookValue) {
	var selectChapter = document.getElementById('selectChapter');
	selectChapter.disabled = false;

	selectChapter.options.length = 0;
	var chapterCount = chapters[books.indexOf(bookValue)];
	for (var c = 1; c <= chapterCount; c++) {
		selectChapter.options.add(new Option(c));
	}
}

function autocomplete(input, possibleValues) {
	/*the autocomplete function takes two arguments,
	the text field element and an array of possible autocompleted values:*/
	var currentFocus;
	/*execute a function when someone writes in the text field:*/
	input.addEventListener("input", function (e) {
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
				b.addEventListener("click", function (e) {
					/*insert the value for the autocomplete text field:*/
					input.value = this.getElementsByTagName("input")[0].value;
					populateChapters(input.value);
					/*close the list of autocompleted values,
					(or any other open lists of autocompleted values:*/
					closeAllLists();
					doSubmit();
				});
				a.appendChild(b);
			}
		}
	});
	/*execute a function presses a key on the keyboard:*/
	input.addEventListener("keydown", function (e) {
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
	document.addEventListener("click", function (e) {
		closeAllLists(e.target);
	});
}

function doSubmit() {
	var selectBibleName = document.getElementById('selectBibleName');
	if (selectBibleName != null) {
		document.getElementById('inputBibleName').value = selectBibleName.value;
	}
	document.getElementById('readForm').submit();
}

function showConcordanceEntry(id, a) {
	var divConcordanceWrapper = document.getElementById("concordanceWrapper");
	divConcordanceWrapper.style.height = 'calc(90vh - 200px)';
	divConcordanceWrapper.style.display = "block";

	const ids = id.split(' ');
	var item = concordance[ids[0]];
	var divConcordanceContent = document.getElementById("concordanceContent");

	var html = "";
	html += "<b><span style=\"color:var(--ubuntu-brown);\">" + a.innerHTML + "</span> " + item.title + " </b><span>" + item.id + "</span><br><br>";
	html += item.paragraph + "<br><br>";
	html += "<table>";
	html += "<tr><th style=\"text-align: center;\">#</th><th style=\"text-align: left;\">Übersetzt</th><th style=\"text-align: left;\">Parallelstellen</th></tr>";
	for (var i = 0; i < item.description.length; i++) {
		html += "<tr>";
		const descrTitles = item.description[i].title.split(', ');
		html += "<td style=\"vertical-align: top; text-align: center;\">" + descrTitles[1] + "</td>";
		html += "<td style=\"vertical-align: top;\">" + descrTitles[0] + "</td>";
		html += "<td>";
		for (var j = 0; j < item.description[i].reflinks.length; j++) {

			const reflinks = item.description[i].reflinks[j].split(';');
			var book = books[reflinks[0] - 1];
			var chapter = reflinks[1];
			var verse = reflinks[2];

			html += "<a href=\"/readStrBy?book=" + book + "&chapter=" + chapter + "&verse=" + verse + "\">" + getPassage(item.description[i].reflinks[j]) + "</a>";
			if (j < item.description[i].reflinks.length - 1) {
				html += "; ";
			}
		}
		html += "</td>";
		html += "</tr>";
	}
	html += "</table>";
	divConcordanceContent.innerHTML = html;
	divConcordanceContent.style.display = "block";
	divConcordanceContent.scrollTop = 0;

	var divConcordanceClose = document.getElementById("concordanceClose");
	var html = "";
	html += "<span>&#10005;</span>";
	divConcordanceClose.innerHTML = html;
	divConcordanceClose.style.display = "block";
	divConcordanceClose.style.marginRight = getScrollBarWidth() + "px";

	var overlay = document.getElementById("overlay");
	overlay.style.zIndex = "1";
	overlay.style.display = "block";
}

function getScrollBarWidth() {
	let el = document.createElement("div");
	el.style.cssText = "overflow:scroll; visibility:hidden; position:absolute;";
	document.body.appendChild(el);
	let width = el.offsetWidth - el.clientWidth;
	el.remove();
	return width;
}

function getPassage(reflink) {
	var strPassage = "";
	const reflinks = reflink.split(';');
	strPassage += books[reflinks[0] - 1] + " " + reflinks[1] + ", " + reflinks[2];
	return strPassage;
}

function hideConcordanceEntry() {
	document.getElementById("concordanceWrapper").style.display = "none";
	document.getElementById("concordanceContent").style.display = "none";
	document.getElementById("concordanceClose").style.display = "none";

	var overlay = document.getElementById("overlay");
	overlay.style.zIndex = "3";
	overlay.style.display = "none";
}
