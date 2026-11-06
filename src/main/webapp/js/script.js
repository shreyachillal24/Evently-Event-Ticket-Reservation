
// Smoothly navigate to page sections.
document.querySelectorAll('a[href^="#"]').forEach(function (link) {
    link.addEventListener("click", function (event) {
        const targetId = link.getAttribute("href");

        if (targetId.length > 1) {
            const target = document.querySelector(targetId);

            if (target) {
                event.preventDefault();
                target.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            }
        }
    });
});

console.log("Evently homepage loaded successfully.");


function showLoginPopup(seatNumber) {
    const overlay = document.getElementById("loginPopupOverlay");
    const message = document.getElementById("loginPopupMessage");

    const contextPath = window.location.pathname
        .substring(0, window.location.pathname.indexOf("/event-details"));

    const currentUrl = window.location.pathname + window.location.search;

    message.textContent =
        "You selected seat " + seatNumber +
        ". Please log in or sign up to continue booking.";

    const redirect = encodeURIComponent(currentUrl);

    document.getElementById("popupLoginLink").href =
        contextPath + "/login?redirect=" + redirect;

    document.getElementById("popupSignupLink").href =
        contextPath + "/register?redirect=" + redirect;

    overlay.classList.add("show");
}

function closeLoginPopup() {
    document.getElementById("loginPopupOverlay")
        .classList.remove("show");
}


function selectSeat(eventSeatId, seatNumber) {
    if (!confirm("Reserve seat " + seatNumber + " for 10 minutes?")) {
        return;
    }

    const contextPath = window.location.pathname.split("/")[1];
    const basePath = "/" + contextPath;

    const form = document.createElement("form");
    form.method = "POST";
    form.action = basePath + "/reserve";

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "eventSeatId";
    input.value = eventSeatId;

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}


document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
        closeLoginPopup();
    }
});

document.addEventListener("click", function (event) {
    const overlay = document.getElementById("loginPopupOverlay");

    if (event.target === overlay) {
        closeLoginPopup();
    }
});
