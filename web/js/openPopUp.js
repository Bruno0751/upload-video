$(document).ready(function () {
    // Evento delegado para qualquer botão com id openPopup
    $(document).on("click", "#openPopup", function () {
        $("#popupOverlay").fadeIn();
    });

    // Fechar o popup
    $(document).on("click", "#closePopup, #popupOverlay", function (e) {
        if (e.target.id === "popupOverlay" || e.target.id === "closePopup") {
            $("#popupOverlay").fadeOut();

            const video = document.getElementById("videoPlayer");
            video.pause();
            video.currentTime = 0;
        }
    });
});