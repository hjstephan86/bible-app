function handleTopNav() {
	const topnav = document.getElementById("topnav");
	const overlay = document.getElementById("overlay");

	// Toggle the 'responsive' class for the navigation
	topnav.classList.toggle("responsive");

	if (topnav.classList.contains("responsive")) {
		overlay.style.display = "block";

		// Adjust overlay z-index if needed (consider using classes instead)
		if (overlay.style.zIndex === "1") {
			overlay.style.zIndex = "3";
		}
	} else {
		// Check if concordance wrapper is visible before hiding the overlay
		const concordanceWrapper = document.getElementById("concordanceWrapper");
		if (concordanceWrapper && concordanceWrapper.style.display === "block") {
			if (overlay.style.zIndex === "3") {
				overlay.style.zIndex = "1";
			}
		} else {
			overlay.style.display = "none";
		}
	}
}

function hideOverlay() {
	const overlay = document.getElementById("overlay");
	const concordanceWrapper = document.getElementById("concordanceWrapper");

	// Check conditions before hiding the overlay and concordance entry
	if (
		concordanceWrapper &&
		concordanceWrapper.style.display === "block" &&
		overlay.style.zIndex === "1"
	) {
		overlay.style.zIndex = "3";
		overlay.style.display = "none";
		hideConcordanceEntry(); // Assuming this function is defined elsewhere
	}
}