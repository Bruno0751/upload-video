function loadVideo(id) {
    fetch(`ServletVideo?opcao=streamVideo&id=${id}`, {
        method: "GET"
    })
            .then(response => {
                if (response.headers.get("Content-Type").includes('application/json;charset=UTF-8')) {
                    return response.json().then(json => {
                        if (json.status === 'error') {
                            throw new Error(json.msg);
                        }
                    });
                }
                if (response.headers.get("Content-Type").includes('video/mp4')) {
                    return response.blob();
                }
                throw new Error("Tipo de resposta inesperado:");
            })
            .then(blob => {
                const video = document.getElementById("videoPlayer");

                const videoUrl = URL.createObjectURL(blob);

                video.src = videoUrl;
                video.style.display = "block";
                video.load();
                video.play();
            })
            .catch(erro => {
                console.error("Error:", error);
                alert(erro);
            });
}