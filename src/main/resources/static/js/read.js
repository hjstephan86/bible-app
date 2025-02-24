function init() {
	const bookInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');

	chapterSelect.disabled = !bookInput.value;

	if (bookInput.value) {
		populateChapters(bookInput.value);
		chapterSelect.classList.remove("white");
		chapterSelect.value = selectedChapterValue;
	}

	initNavigation();

	const bibleNameInput = document.getElementById('inputBibleName');
	if (bibleNameInput) {
		bibleNameInput.value = bibleName;
	}

	scrollToVerseFromUrl();
}

function scrollToVerseFromUrl() {
	const urlParams = new URLSearchParams(window.location.search);
	const verse = parseInt(urlParams.get('verse'));

	if (verse && verse > 0) {
		const firstElementRect = document.getElementById('1').getBoundingClientRect();
		const verseElementRect = document.getElementById(verse).getBoundingClientRect();
		window.scrollBy(0, verseElementRect.top - firstElementRect.top);
	}
}

function initNavigation() {
	const prevSpan = document.getElementById('spanPrev');
	const nextSpan = document.getElementById('spanNext');
	const bookInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');
	const currentChapter = parseInt(chapterSelect.value, 10);
	const totalChapters = parseInt(chapters[books.indexOf(bookInput.value)], 10);

	nextSpan.classList.toggle("disable", !(bookInput.value && currentChapter < totalChapters));
	prevSpan.classList.toggle("disable", !(bookInput.value && currentChapter > 1));
}

function goToPrevChapter() {
	const bookInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');
	const currentChapter = parseInt(chapterSelect.value, 10);

	if (bookInput.value && currentChapter > 1) {
		chapterSelect.classList.add("white");
		chapterSelect.value = (currentChapter - 1).toString();
		document.getElementById('readForm').submit();
	}
}

function goToNextChapter() {
	const bookInput = document.getElementById('inputBook');
	const chapterSelect = document.getElementById('selectChapter');
	const currentChapter = parseInt(chapterSelect.value, 10);
	const totalChapters = parseInt(chapters[books.indexOf(bookInput.value)], 10);

	if (bookInput.value && currentChapter < totalChapters) {
		chapterSelect.classList.add("white");
		chapterSelect.value = (currentChapter + 1).toString();
		document.getElementById('readForm').submit();
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
					closeAllLists();
					doSubmit();
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
	document.getElementById('readForm').submit();
}

function showConcordanceEntry(id, a) {
	const concordanceWrapper = document.getElementById("concordanceWrapper");
	concordanceWrapper.style.height = 'calc(90vh - 200px)';
	concordanceWrapper.style.display = "block";

	const ids = id.split(' ');
	const item = concordance[ids[0]];
	const description = Object.values(item.description);

	description.sort((a, b) => {
		const numberA = parseInt(a.title.match(/\d+$/)[0], 10);
		const numberB = parseInt(b.title.match(/\d+$/)[0], 10);
		return numberB - numberA;
	});

	const concordanceContent = document.getElementById("concordanceContent");
	let html = "";
	html += `<b><span style="color:var(--ubuntu-brown);">${a.innerHTML}</span> ${item.title} </b><span>${item.id}</span><br><br>`;
	html += `${item.paragraph}<br><br>`;
	html += "<table style=\"width: 100%;\">";
	html += "<tr><th style=\"text-align: center;\">#</th><th style=\"text-align: left;\">Übersetzt</th><th class=\"concordance parallel\">Parallelstellen</th></tr>";

	for (let i = 0; i < description.length; i++) {
		html += "<tr>";
		const descrTitles = description[i].title.split(', ');
		html += `<td style="vertical-align: top; text-align: center;">${descrTitles[1]}</td>`;
		html += `<td style="vertical-align: top;">${descrTitles[0]}</td>`;
		html += "<td>";

		for (let j = 0; j < description[i].reflinks.length; j++) {
			const reflinks = description[i].reflinks[j].split(';');
			const book = books[reflinks[0] - 1];
			const chapter = reflinks[1];
			const verse = reflinks[2];
			const passage = getPassage(description[i].reflinks[j]);

			html += `<a href="/readStrBy?book=${book}&chapter=${chapter}&verse=${verse}">${passage}</a>`;
			if (j < description[i].reflinks.length - 1) {
				html += "; ";
			}
		}

		html += "</td>";
		html += "</tr>";
	}

	html += "</table>";
	concordanceContent.innerHTML = html;
	concordanceContent.style.display = "block";
	concordanceContent.scrollTop = 0;

	const concordanceClose = document.getElementById("concordanceClose");
	html = "<span>&#10005;</span>";
	concordanceClose.innerHTML = html;
	concordanceClose.style.display = "block";
	concordanceClose.style.marginRight = getScrollBarWidth() + "px";

	const overlay = document.getElementById("overlay");
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
	let strPassage = "";
	const reflinks = reflink.split(';');
	strPassage += books[reflinks[0] - 1] + " " + reflinks[1] + ", " + reflinks[2];
	return strPassage;
}

function hideConcordanceEntry() {
	document.getElementById("concordanceWrapper").style.display = "none";
	document.getElementById("concordanceContent").style.display = "none";
	document.getElementById("concordanceClose").style.display = "none";

	const overlay = document.getElementById("overlay");
	overlay.style.zIndex = "3";
	overlay.style.display = "none";
}