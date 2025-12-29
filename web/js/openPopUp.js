$(document).ready(function () {
    $(document).on("click", ".openPopup", function () {
        const videoId = $(this).data("id");
        openVideoPopup(videoId);
    });

    // EVENTO: fechar popup ao clicar no X ou fora do vídeo
    $(document).on("click", "#closePopup, #popupOverlay", function (e) {
        if (e.target.id === "popupOverlay" || e.target.id === "closePopup") {
            closeVideoPopup();
        }
    });
});
function openVideoPopup(videoId) {
    const player = document.getElementById("videoPlayer");

    // 1. limpar estado anterior
    player.pause();
    player.removeAttribute("src");
    player.load();

    // 2. gerar URL para o servlet
    const videoURL = `${servlet}=play&id=${videoId}`;

    // 3. abrir popup APENAS quando o vídeo tiver carregado
    player.onloadeddata = () => {
        $("#popupOverlay").fadeIn();
    };

    // 4. carregar vídeo
    player.src = videoURL;
}
function closeVideoPopup() {
    const player = document.getElementById("videoPlayer");
    player.pause();
    player.removeAttribute("src");
    player.load();
    $("#popupOverlay").fadeOut();
}