function handleTopNav() {
	var topnav = document.getElementById("topnav");
	var overlay = document.getElementById("overlay");
	if (topnav.className === "topnav") {
		topnav.className += " responsive";
		overlay.style.display = "block";
		if (overlay.style.zIndex == "1") {
			overlay.style.zIndex = "3";
		}
	} else {
		topnav.className = "topnav";
		var concordanceWrapper = document.getElementById("concordanceWrapper");
		if (concordanceWrapper != null && concordanceWrapper.style.display == "block") {
			if (overlay.style.zIndex == "3") {
				overlay.style.zIndex = "1";
			}
		}
		else {
			overlay.style.display = "none";
		}
	}
}

function hideOverlay() {
	var overlay = document.getElementById("overlay");
	var concordanceWrapper = document.getElementById("concordanceWrapper");
	if (concordanceWrapper != null && concordanceWrapper.style.display == "block" && overlay.style.zIndex == "1") {
		overlay.style.zIndex = "3";
		overlay.style.display = "none";

		hideConcordanceEntry();
	}
}
