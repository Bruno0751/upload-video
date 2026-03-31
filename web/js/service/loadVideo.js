function loadStream(id) {
    const videoUrl = "http://localhost:1010/v1/video/stream/" + id;
    document.getElementById("videoSource").src = videoUrl;
    const video = document.getElementById("videoPlayer");
    video.load();
    document.getElementById("videoPopup").style.display = "block";
}
function closePopup() {
    const video = document.getElementById("videoPlayer");
    video.pause();
    video.currentTime = 0;
    document.getElementById("videoPopup").style.display = "none";
}